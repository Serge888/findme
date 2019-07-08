package com.findme.service;

import com.findme.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User save(User user);
    User update(User user);
    User delete(User user);
    User findById(Long id);
}
