package com.peijun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "wo shi yi ge user";
    }
}
