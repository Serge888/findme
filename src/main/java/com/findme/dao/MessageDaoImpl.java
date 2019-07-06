package com.findme.dao;

import com.findme.models.Message;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class MessageDaoImpl extends GeneralDao<Message> implements MessageDao {


    @Override
    public Message delete(Long id) {
        Message post = entityManager.find(Message.class, id);
        entityManager.remove(post);
        return post;
    }

    @Override
    public Message findById(Long id) {
        return null;
    }
}
