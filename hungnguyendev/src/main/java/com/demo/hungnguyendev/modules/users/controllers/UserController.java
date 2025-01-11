package com.demo.hungnguyendev.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.hungnguyendev.modules.users.entities.User;
import com.demo.hungnguyendev.modules.users.repositories.UserRepository;
import com.demo.hungnguyendev.modules.users.resources.SuccessResource;
import com.demo.hungnguyendev.modules.users.resources.UserResource;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));

        UserResource userResource = UserResource.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();

        SuccessResource<UserResource> response = new SuccessResource<>("SUCCESS", userResource);

        return ResponseEntity.ok(response);
    }
}
