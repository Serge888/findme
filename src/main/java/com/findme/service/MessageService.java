package com.findme.service;

import com.findme.models.Message;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {

    Message save(Message message);
    Message update(Message message);
    Message delete(Message message);
    Message findById(Long id);

}
