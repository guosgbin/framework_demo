package com.peijun.remote;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用户接口
 */
public interface UserRemoteService {

    /**
     * 获取用户信息
     */
    @GetMapping("/user")
    String getUser();
}
