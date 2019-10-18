package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;

import java.util.List;

public interface MessageDao {

    Message save(Message message) throws InternalServerException;
    Message update(Message message) throws InternalServerException;
    Message delete(Message message) throws InternalServerException;
    Message findById(Long id) throws InternalServerException, NotFoundException;
    List<Message> getAllMessages(Long userFromId, Long userToId, Integer messageIndexFrom)
            throws InternalServerException, NotFoundException;
    void updateDateRead(List<Long> messageIdList) throws InternalServerException, NotFoundException;
    void updateDateDeleted(List<Message> messageList) throws InternalServerException, NotFoundException;
    void updateAllDateDeleted(Long userFromId, Long userToId) throws InternalServerException, NotFoundException;

}
