package com.findme.service;


import com.findme.dao.MessageDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Message;
import com.findme.models.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageDao messageDao;
    private final RelationshipService relationshipService;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao, RelationshipService relationshipService) {
        this.messageDao = messageDao;
        this.relationshipService = relationshipService;
    }

    @Override
    public Message save(Message message)  throws InternalServerException, BadRequestException {
        Relationship relationship =
                relationshipService.findByIdFromAndIdTo(message.getUserFrom().getId(), message.getUserTo().getId());
        validationForSave(relationship, message);
        message.setDateSent(LocalDate.now());
        return messageDao.save(message);
    }

    @Override
    public Message update(Message message) throws InternalServerException, BadRequestException {
        Message foundMessage = findById(message.getId());
        foundMessage.setText(message.getText());
        foundMessage.setDateEdited(LocalDate.now());
        validationForUpdate(foundMessage, message);
        return messageDao.update(foundMessage);
    }

    @Override
    public Message updateReadDate(Message message) throws InternalServerException, BadRequestException {
        Message foundMessage = findById(message.getId());
        validationForUpdate(foundMessage, message);
        foundMessage.setDateRead(LocalDate.now());
        return messageDao.update(foundMessage);
    }

    @Override
    public Message updateDeleteDate(Message message) throws InternalServerException, BadRequestException {
        Message foundMessage = findById(message.getId());
        foundMessage.setDateDeleted(LocalDate.now());
        validationForUpdate(foundMessage, message);
        return messageDao.update(foundMessage);
    }

    @Override
    public Message findById(Long id) throws InternalServerException, NotFoundException {
        return messageDao.findById(id);
    }



    @Override
    public void updateSelectedMessages(List<Message> messageList) throws InternalServerException, BadRequestException {
        if (messageList.size() > 10) {
            throw new BadRequestException("You can update 10 messages only at a time.");
        }
        List<Message> foundMessages = new ArrayList<>();
        for (Message message : messageList) {
            Message foundMessage = findById(message.getId());
            validationForUpdate(foundMessage, message);
            foundMessages.add(foundMessage);
        }
        messageDao.updateDateDeleted(foundMessages);

    }

    @Override
    public void updateAllDateDeleted(Long userFormId, Long userToId)
            throws InternalServerException, BadRequestException {
        messageDao.updateAllDateDeleted(userFormId, userToId);

    }

    @Override
    public List<Message> findMessagesByIds(Long userFromId, Long userToId, Integer messageIndexFrom)
            throws InternalServerException, NotFoundException {
        List<Long> messageIdsToReadUpdate = new ArrayList<>();
        List<Message> messageList = messageDao.getAllMessages(userFromId, userToId, messageIndexFrom);
        for (Message message : messageList) {
            if (message.getDateRead() == null && message.getUserTo().getId().equals(userFromId)) {
                message.setDateRead(LocalDate.now());
                messageIdsToReadUpdate.add(message.getId());
            }
        }
        if (messageIdsToReadUpdate.size() > 0)
            messageDao.updateDateRead(messageIdsToReadUpdate);
        return messageList;
    }


    private void validationForSave(Relationship relationship, @NonNull Message message) throws BadRequestException {
        if (relationship == null) {
            throw new BadRequestException("You can't send message to user id " + message.getUserTo().getId());
        }
        if (!relationship.getFriendRelationshipStatus().equals(FriendRelationshipStatus.ACCEPTED)) {
            throw new BadRequestException("You can send messages to your friends only");
        }
        messageLengthValidation(message);
    }

    private void messageLengthValidation(@NonNull Message message) throws BadRequestException {
        if (message.getText().length() > 140)  {
            throw new BadRequestException("Message can't be more then 140 characters.");
        }
    }



    private void validationForUpdate(@NonNull Message foundMessage, @NonNull Message receivedMessage) throws BadRequestException {
        messageLengthValidation(foundMessage);
        if (foundMessage.getDateRead() != null) {
            throw new BadRequestException("Message has already been read. You can't edit or delete this message any more.");
        }
        if (foundMessage.getDateEdited() != null
                && foundMessage.getDateDeleted() != null
                && foundMessage.getDateEdited().isAfter(foundMessage.getDateDeleted())) {
            throw new BadRequestException("Message was removed.");
        }
        if (!receivedMessage.getUserFrom().getId().equals(foundMessage.getUserFrom().getId())) {
            throw new BadRequestException("UserFrom or userTo doesn't match.");
        }
    }



}
