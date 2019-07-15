package com.findme.controller;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;
import com.findme.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MessageController {
    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-message", produces = "text/plain")
    public @ResponseBody
    String save(@RequestBody Message message) throws InternalServerException {
        messageService.save(message);
        return "Message id " + message.getId() + " was saved";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-message", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody Message newMessage) throws NotFoundException, InternalServerException {
        Long id = newMessage.getId();
        Message message = messageService.findById(id);
        message.setText(newMessage.getText());
        messageService.update(message);
        return "Message id " + message.getId() + " was updated " + message;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-message", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody Message message) throws InternalServerException {
        messageService.delete(message);
        return "Message id " + message.getId() + " was deleted " + message;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-message/{messageId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable Long messageId) throws NotFoundException, InternalServerException {
        Message message = messageService.findById(messageId);
        return "Message " + message.getId() +" was found: " + message;
    }
}
