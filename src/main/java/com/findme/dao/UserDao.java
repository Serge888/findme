package com.findme.dao;

import com.findme.models.User;

public interface UserDao {

    User save(User user);
    User update(User user);
    User delete(User user);
    User findById(Long id);
}
