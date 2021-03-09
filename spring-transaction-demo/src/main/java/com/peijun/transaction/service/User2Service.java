package com.peijun.transaction.service;

import com.peijun.transaction.pojo.User2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-09
 */
public interface User2Service extends IService<User2> {

    void addRequired(User2 user);

    void addRequiredException(User2 user);

    void addRequiredException2(User2 user);

    void truncate();

    void addSupports(User2 user);

    void addSupportsException(User2 user);

    void addRequiresNew(User2 user);

    void addRequiresNewException(User2 user);

    void addNotSupported(User2 user);

    void addNotSupportedException(User2 user);

    void add(User2 user);

    void addException(User2 user);

    void addMandatory(User2 user);

    void addMandatoryException(User2 user);

    void addNever(User2 user);

    void addNeverException(User2 user);

    void addNested(User2 user);

    void addNestedException(User2 user);

    User2 getRequired(Integer id);

    User2 get(Integer id);

}
