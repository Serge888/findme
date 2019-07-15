package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class MessageDaoImpl extends GeneralDao<Message> implements MessageDao {

    @Override
    public Message findById(Long id) throws NotFoundException, InternalServerException {
        Message message;
        try {
            message = entityManager.find(Message.class, id);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findById postId = " + id);
        }
        if (message == null) {
            throw new NotFoundException("Post id " + id + " was not found");
        }
        return message;
    }
}
