package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;

public interface MessageService {

    Message save(Message message) throws InternalServerException, BadRequestException;
    Message update(Message message) throws InternalServerException, BadRequestException;
    Message findById(Long id) throws InternalServerException, NotFoundException;
    Message updateReadDate(Message message) throws InternalServerException, BadRequestException;
    Message updateDeleteDate(Message message) throws InternalServerException, BadRequestException;

}
