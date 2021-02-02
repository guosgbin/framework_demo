package com.peijun.rpc;

import com.peijun.constant.UserConstant;
import com.peijun.remote.UserRemoteService;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name = UserConstant.SERVICE_NAME, fallbackFactory = UserRpcServiceClient.HystrixClientFallback.class)
public interface UserRpcServiceClient extends UserRemoteService {

    @Component
    class HystrixClientFallback implements FallbackFactory<UserRpcServiceClient> {
        @Override
        public UserRpcServiceClient create(Throwable cause) {
            return new UserRpcServiceClient() {
                /**
                 * 获取用户信息
                 */
                @Override
                public String getUser() {
                    System.out.println("错误是：" + cause.getMessage());
                    return "爷是服务降级方法";
                }
            };
        }
    }
}
