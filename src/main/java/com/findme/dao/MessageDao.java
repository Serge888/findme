package com.findme.dao;

import com.findme.models.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao {

    Message save(Message message);
    Message update(Message message);
    Message delete(Long id);
    Message findById(Long id);


}
