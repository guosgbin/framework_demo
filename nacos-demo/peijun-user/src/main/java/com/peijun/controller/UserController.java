package com.peijun.controller;

import com.peijun.remote.UserRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 */
@RestController
public class UserController implements UserRemoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public String getUser() {
        LOGGER.debug("我进来了...");
        return "wo shi yi ge user";
    }
}
