package com.findme.dao;

import com.findme.models.Message;

public interface MessageDao {

    Message save(Message message);
    Message update(Message message);
    Message delete(Message message);
    Message findById(Long id);


}
