package com.findme.service;


import com.findme.models.Message;
import com.findme.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageDao messageDao;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public Message save(Message message) {
        message.setDateSent(new Date());
        return messageDao.save(message);
    }

    @Override
    public Message update(Message message) {
        return messageDao.update(message);
    }

    @Override
    public Message delete(Message message) {
        return messageDao.delete(message);
    }

    @Override
    public Message findById(Long id) {
        return messageDao.findById(id);
    }
}
