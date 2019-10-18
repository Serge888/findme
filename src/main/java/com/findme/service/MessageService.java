package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;

import java.util.List;

public interface MessageService {

    Message save(Message message) throws InternalServerException, BadRequestException;
    Message update(Message message) throws InternalServerException, BadRequestException;
    Message findById(Long id) throws InternalServerException, NotFoundException;
    Message updateReadDate(Message message) throws InternalServerException, BadRequestException;
    Message updateDeleteDate(Message message) throws InternalServerException, BadRequestException;
    void updateSelectedMessages(List<Message> messages) throws InternalServerException, BadRequestException;
    void updateAllDateDeleted(Long userFormId, Long userToId) throws InternalServerException, BadRequestException;
    List<Message> findMessagesByIds(Long userFromId, Long userToId, Integer messageIndexFrom)
            throws InternalServerException, NotFoundException;
}
