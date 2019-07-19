package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;

public interface MessageService {

    Message save(Message message) throws InternalServerException;
    Message update(Message message) throws InternalServerException;
    Message delete(Message message) throws InternalServerException;
    Message findById(Long id) throws InternalServerException, NotFoundException;

}
