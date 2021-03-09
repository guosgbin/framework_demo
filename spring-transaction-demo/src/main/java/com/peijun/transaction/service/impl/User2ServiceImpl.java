package com.peijun.transaction.service.impl;

import com.peijun.transaction.pojo.User2;
import com.peijun.transaction.dao.User2Dao;
import com.peijun.transaction.service.User2Service;
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
public class User2ServiceImpl extends ServiceImpl<User2Dao, User2> implements User2Service {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User2 user) {
        this.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequiredException(User2 user) {
        this.save(user);
        throw new RuntimeException("============爷抛异常了============");
    }

    @Override
    public void addRequiredException2(User2 user) {
        this.save(user);
        throw new RuntimeException("============爷抛异常了============");
    }

    @Override
    public void truncate() {

    }

    @Override
    public void addSupports(User2 user) {

    }

    @Override
    public void addSupportsException(User2 user) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User2 user) {
        this.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(User2 user) {
        this.save(user);
        throw new RuntimeException("========爷抛异常了==========");
    }

    @Override
    public void addNotSupported(User2 user) {

    }

    @Override
    public void addNotSupportedException(User2 user) {

    }

    @Override
    public void add(User2 user) {

    }

    @Override
    public void addException(User2 user) {

    }

    @Override
    public void addMandatory(User2 user) {

    }

    @Override
    public void addMandatoryException(User2 user) {

    }

    @Override
    public void addNever(User2 user) {

    }

    @Override
    public void addNeverException(User2 user) {

    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User2 user) {
        this.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNestedException(User2 user) {
        this.save(user);
        throw new RuntimeException();
    }

    @Override
    public User2 getRequired(Integer id) {
        return null;
    }

    @Override
    public User2 get(Integer id) {
        return null;
    }
}
