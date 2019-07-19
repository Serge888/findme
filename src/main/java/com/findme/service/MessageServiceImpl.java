package com.findme.service;


import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;
import com.findme.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageDao messageDao;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public Message save(Message message)  throws InternalServerException {
        message.setDateSent(new Date());
        return messageDao.save(message);
    }

    @Override
    public Message update(Message message) throws InternalServerException {
        return messageDao.update(message);
    }

    @Override
    public Message delete(Message message) throws InternalServerException {
        return messageDao.delete(message);
    }

    @Override
    public Message findById(Long id) throws InternalServerException, NotFoundException {
        return messageDao.findById(id);
    }
}
