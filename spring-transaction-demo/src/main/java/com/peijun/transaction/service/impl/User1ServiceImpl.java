package com.peijun.transaction.service.impl;

import com.peijun.transaction.pojo.User1;
import com.peijun.transaction.dao.User1Dao;
import com.peijun.transaction.service.User1Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-09
 */
@Service
public class User1ServiceImpl extends ServiceImpl<User1Dao, User1> implements User1Service {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User1 user) {
        this.save(user);
    }


    @Override
    public void addRequiredException(User1 user) {
        this.save(user);
        throw new RuntimeException("===========爷抛异常了===========");
    }

    @Override
    public void truncate() {

    }

    @Override
    public void addSupports(User1 user) {

    }

    @Override
    public void addSupportsException(User1 user) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User1 user) {
        this.save(user);
    }

    @Override
    public void addRequiresNewException(User1 user) {

    }

    @Override
    public void addNotSupported(User1 user) {

    }

    @Override
    public void addNotSupportedException(User1 user) {

    }

    @Override
    public void add(User1 user) {

    }

    @Override
    public void addException(User1 user) {

    }

    @Override
    public void addMandatory(User1 user) {

    }

    @Override
    public void addMandatoryException(User1 user) {

    }

    @Override
    public void addNever(User1 user) {

    }

    @Override
    public void addNeverException(User1 user) {

    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User1 user) {
        this.save(user);
    }

    @Override
    public void addNestedException(User1 user) {

    }

    @Override
    public User1 get(Integer id) {
        return null;
    }

    @Override
    public User1 getRequired(Integer id) {
        return null;
    }

    @Override
    public User1 getRequiresNew(Integer id) {
        return null;
    }

    @Override
    public User1 getNested(Integer id) {
        return null;
    }

    @Override
    public User1 getNotSupported(Integer id) {
        return null;
    }
}
