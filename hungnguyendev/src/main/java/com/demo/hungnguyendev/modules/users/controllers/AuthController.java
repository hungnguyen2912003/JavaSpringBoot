package com.demo.hungnguyendev.modules.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.hungnguyendev.modules.users.requests.LoginRequest;
import com.demo.hungnguyendev.modules.users.resources.LoginResource;
import com.demo.hungnguyendev.modules.users.services.interfaces.UserServiceInterface;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("v1/auth")
public class AuthController {
    
    private final UserServiceInterface userService;

    public AuthController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResource> login(@Valid @RequestBody LoginRequest request){
        LoginResource auth = userService.login(request);
        return ResponseEntity.ok(auth);
    }
}
