package com.findme.dao;

import com.findme.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User save(User user);
    User update(User user);
    User delete(Long id);
    User findById(Long id);
}
