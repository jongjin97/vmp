package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.errors.UnauthorizedException;
import com.vanmanagement.vmp.jwt.UserTokenDto;
import com.vanmanagement.vmp.payment.Payment;
import com.vanmanagement.vmp.jwt.Jwt;
import com.vanmanagement.vmp.jwt.JwtAuthentication;
import com.vanmanagement.vmp.jwt.JwtAuthenticationToken;
import com.vanmanagement.vmp.users.*;
import org.modelmapper.ModelMapper;
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

    public UserRestContoller(UserService userService) {
        this.userService = userService;
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
            UserTokenDto userTokenDto = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            User user = modelMapper.map(userTokenDto.getUserEntity(), User.class);

            response.setHeader("Authorization", "Bearer " + userTokenDto.getToken());
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            return success(userResponse);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @PostMapping(value = "/point")
    public ApiResult<UserResponse> PurchasePoint(@AuthenticationPrincipal JwtAuthentication authentication
            , @RequestBody Payment payment) throws AccountNotFoundException {
        UserResponse userResponse = userService.updateUserPoint(authentication.id, payment)
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
