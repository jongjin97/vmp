package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.errors.UnauthorizedException;
import com.vanmanagement.vmp.security.Jwt;
import com.vanmanagement.vmp.security.JwtAuthentication;
import com.vanmanagement.vmp.security.JwtAuthenticationToken;
import com.vanmanagement.vmp.users.LoginRequest;
import com.vanmanagement.vmp.users.RegisterRequest;
import com.vanmanagement.vmp.users.User;
import com.vanmanagement.vmp.users.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final Jwt jwt;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, Jwt jwt, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                               BindingResult result){
        if (result.hasErrors()) {
            return "signup";
        }
        System.out.println(registerRequest);
        userService.saveUser(registerRequest);
        return "redirect:/user/login";
    }

    @GetMapping(value = "/signup")
    public String signUpForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "signup";
    }

    @GetMapping(value = "/login")
    public String LogInForm(Model model){
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping(value = "/login")
    public String Login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
                        BindingResult result, HttpServletResponse response){
        if (result.hasErrors()) {
            return "login";
        }
        try {
            userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            final User user = (User) authentication.getDetails();
            final String token = user.newJwt(
                    jwt,
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toArray(String[]::new)
            );
            response.addHeader("VMP-AUTH", token);
            return "main";
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }
}
