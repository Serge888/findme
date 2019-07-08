package com.findme.exception;

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
    String save(@RequestBody Message message) {
        try {
            messageService.save(message);
        } catch (Exception e) {
            e.getMessage();
            return "Message id " + message.getId() + " was not saved";
        }
        return "Message id " + message.getId() + " was saved";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-message", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody Message newMessage) {
        Message message = new Message();
        try {
            message = messageService.findById(newMessage.getId());
            message.setText(newMessage.getText());
            messageService.update(message);
        } catch (Exception e) {
            e.getMessage();
            return "Message id " + message.getId() + " was not updated";
        }
        return "Message id " + message.getId() + " was updated " + message;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-message", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody Message message) {
        try {
            messageService.delete(message.getId());
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            return "Message id " + message.getId() + " was not deleted";

        }
        return "Message id " + message.getId() + " was deleted " + message;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-message/{messageId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable Long messageId) {
        Message message = new Message();
        try {
            message = messageService.findById(messageId);
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            return "Message " + message.getId() +" was not found.";
        }
        return "Message " + message.getId() +" was found: " + message;
    }
}
