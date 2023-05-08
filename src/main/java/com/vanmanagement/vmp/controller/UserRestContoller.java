package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.errors.UnauthorizedException;
import com.vanmanagement.vmp.security.Jwt;
import com.vanmanagement.vmp.security.JwtAuthenticationToken;
import com.vanmanagement.vmp.users.*;
import com.vanmanagement.vmp.utils.ApiUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

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
    public ApiResult<User> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return success(
                userService.saveUser(registerRequest)
                        .map(User::new)
                        .get());
    }


    @PostMapping(value = "/login")
    public ApiResult<User> Login(@Valid @RequestBody LoginRequest loginRequest,
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
            response.setHeader("Email",  user.getEmail());
            return success(user);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

}
