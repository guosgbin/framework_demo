package com.peijun.transaction.service.impl;

import com.peijun.transaction.pojo.User;
import com.peijun.transaction.dao.UserDao;
import com.peijun.transaction.service.UserService;
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
