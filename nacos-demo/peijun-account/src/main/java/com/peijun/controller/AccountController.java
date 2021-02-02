package com.peijun.controller;

import com.peijun.rpc.UserRpcServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private UserRpcServiceClient userRpcServiceClient;

    @GetMapping("/account")
    public String getAccount() {
        String userStr = userRpcServiceClient.getUser();
        return "我有一分钱" + "===" + userStr;
    }
}
