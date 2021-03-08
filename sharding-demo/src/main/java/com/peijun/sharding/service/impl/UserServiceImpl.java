package com.peijun.sharding.service.impl;

import com.peijun.sharding.pojo.User;
import com.peijun.sharding.dao.UserDao;
import com.peijun.sharding.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}
