package com.demo.hungnguyendev;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("v1/api")
public class BaseController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
