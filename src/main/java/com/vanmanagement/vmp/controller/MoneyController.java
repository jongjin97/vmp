package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.security.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/money")
public class MoneyController {

    @GetMapping(value = "/deposit")
    public String depositForm(@AuthenticationPrincipal JwtAuthentication authentication){

        return "deposit";
    }
}
