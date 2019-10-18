package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class MessageDaoImpl extends GeneralDao<Message> implements MessageDao {

    private String updateDateReadHql = "update Message m set m.dateRead = :readDate where m.id in :messageIdList";
    private String updateDateDeletedHql = "update Message m set m.dateDeleted = :deleteDate where m in :messageIdList";


    private String getAllMessagesHql = "select m from Message m " +
            "where ((m.userFrom.id = :userFromId and m.userTo.id = :userToId)" +
            " or (m.userFrom.id = :userToIdInv and m.userTo.id = :userFromIdInv)) and m.dateDeleted is null" +
            " order by case when m.dateRead is null then 1 end asc, m.dateSent asc";

    private String updateAllDateDeletedHql = "update Message m set m.dateDeleted = :deletedDate " +
            "where ((m.userFrom.id = :userFromId and m.userTo.id = :userToId) " +
            "or (m.userFrom.id = :userToIdInv and m.userTo.id = :userFromIdInv)) " +
            "and m.dateDeleted is null";


    @Override
    public Message findById(Long id) throws NotFoundException, InternalServerException {
        Message message;
        try {
            message = entityManager.find(Message.class, id);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findById messageId = " + id);
        }
        if (message == null) {
            throw new NotFoundException("Message id " + id + " was not found");
        }
        return message;
    }


    @Override
    public void updateDateRead(List<Long> messageIdList) throws InternalServerException {
        try {
            entityManager.createQuery(updateDateReadHql)
                    .setParameter("readDate", LocalDate.now())
                    .setParameter("messageIdList", messageIdList)
                    .executeUpdate();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with updateDateRead.");
        }
    }

    @Override
    public void updateDateDeleted(List<Message> messageList) throws InternalServerException {
        try {
            entityManager.createQuery(updateDateDeletedHql)
                    .setParameter("deleteDate", LocalDate.now())
                    .setParameter("messageIdList", messageList)
                    .executeUpdate();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with updateDateDeleted.");
        }
    }


    @Override
    public List<Message> getAllMessages(Long userFromId, Long userToId, Integer messageIndexFrom)
            throws InternalServerException, NotFoundException {
        List<Message> messageList;
        try {
            messageList = new ArrayList<>(entityManager.createQuery(getAllMessagesHql, Message.class)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .setParameter("userFromIdInv", userFromId)
                    .setParameter("userToIdInv", userToId)
                    .setFirstResult(messageIndexFrom)
                    .setMaxResults(20)
                    .getResultList());
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with getAllMessages userFromId = "
                    + userFromId + " and userToId = " + userToId);
        }

        return messageList;
    }

    @Override
    public void updateAllDateDeleted(Long userFromId, Long userToId) throws InternalServerException {
        try {
            entityManager.createQuery(updateAllDateDeletedHql)
                    .setParameter("deletedDate", LocalDate.now())
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .setParameter("userFromIdInv", userFromId)
                    .setParameter("userToIdInv", userToId)
                    .executeUpdate();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with updateAllDateDeleted.");
        }
    }

}
