package com.findme.dao;

import com.findme.models.User;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class UserDaoImpl extends GeneralDao<User> implements UserDao {

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
}
