package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.errors.UnauthorizedException;
import com.vanmanagement.vmp.security.Jwt;
import com.vanmanagement.vmp.security.JwtAuthentication;
import com.vanmanagement.vmp.security.JwtAuthenticationToken;
import com.vanmanagement.vmp.users.*;
import com.vanmanagement.vmp.utils.ApiUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.vanmanagement.vmp.utils.ApiUtils.ApiResult;
import static com.vanmanagement.vmp.utils.ApiUtils.success;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users/")
public class UserRestContoller {

    private final UserService userService;

    private final Jwt jwt;

    private final AuthenticationManager authenticationManager;

    public UserRestContoller(UserService userService, Jwt jwt, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ApiResult<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return success(userService.saveUser(registerRequest)
                .map(userEntity -> UserResponse.builder()
                        .email(userEntity.getEmail())
                        .name(userEntity.getName())
                        .phone(userEntity.getPhone())
                        .point(userEntity.getPoint())
                        .build()
                )
                .orElse(null));
    }


    @PostMapping(value = "/login")
    public ApiResult<UserResponse> Login(@Valid @RequestBody LoginRequest loginRequest,
                       HttpServletResponse response){
        try {
            ModelMapper modelMapper = new ModelMapper();
            UserEntity userEntity = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            User user = modelMapper.map((UserEntity) authentication.getDetails(), User.class);

            final String token = user.newJwt(
                    jwt,
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toArray(String[]::new)
            );
            response.setHeader("Authorization", "Bearer " + token);
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            return success(userResponse);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @PostMapping(value = "/point/{point}")
    public ApiResult<UserResponse> PurchasePoint(@PathVariable String point,
                                @AuthenticationPrincipal JwtAuthentication authentication) throws AccountNotFoundException {
        UserResponse userResponse = userService.updateUserPoint(authentication.id, point)
                .map(userEntity -> UserResponse.builder()
                        .email(userEntity.getEmail())
                        .name(userEntity.getName())
                        .phone(userEntity.getPhone())
                        .point(userEntity.getPoint())
                        .build()
                )
                .orElse(null);

        return success(userResponse);
    }

}
