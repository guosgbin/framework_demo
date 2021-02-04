package com.peijun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/account")
    public String getAccount() {
        // 获取Nacos的User端的地址
        List<ServiceInstance> instances = discoveryClient.getInstances("peijun-user");
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("找不到服务");
        }
        // 拿出一个服务地址 进行远程调用
        ServiceInstance instance = loadBalancerClient.choose("peijun-user");
        String url = instance.getUri().toString();
        String remoteStr = restTemplate.getForObject(url + "/user", String.class);
        return "我有2分钱" + "===" + remoteStr;
    }
}
