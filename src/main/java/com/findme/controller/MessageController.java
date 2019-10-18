package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;
import com.findme.models.User;
import com.findme.service.MessageService;
import com.findme.service.UserService;
import com.findme.util.UtilString;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = Logger.getLogger(MessageController.class);
    private MessageService messageService;
    private UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping(path = "/send-message")
    public ResponseEntity sendMessage(HttpSession session, @RequestParam String userToId, @RequestParam String text)
            throws BadRequestException, InternalServerException {
        User userLoggedIn = (User) session.getAttribute("user");
        userService.isUserLoggedIn(session, userLoggedIn.getId());
        Message message = new Message();
        message.setText(text);
        message.setUserFrom(userLoggedIn);
        message.setUserTo(userService.findById(UtilString.stringToLong(userToId)));
        messageService.save(message);
        logger.info("Message was sent.");
        return new ResponseEntity<>("Message was sent", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/edit-message")
    public ResponseEntity editMessage(HttpSession session, @ModelAttribute Message message)
            throws BadRequestException, InternalServerException {
        User userLoggedIn = (User) session.getAttribute("user");
        userService.isUserLoggedIn(session, userLoggedIn.getId());
        message.setUserFrom(userLoggedIn);
        messageService.update(message);
        logger.info("Message was edited.");
        return new ResponseEntity<>("Message was edited", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/read-message")
    public ResponseEntity readMessage(HttpSession session, @ModelAttribute Message message)
            throws BadRequestException, InternalServerException {
        User userLoggedIn = (User) session.getAttribute("user");
        userService.isUserLoggedIn(session, userLoggedIn.getId());
        message.setUserFrom(userLoggedIn);
        messageService.updateReadDate(message);
        logger.info("Message was read.");
        return new ResponseEntity<>("Message was read", HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.PUT, value = "/delete-message")
    public ResponseEntity deleteMessage(HttpSession session, @ModelAttribute Message message)
            throws BadRequestException, InternalServerException {
        User userLoggedIn = (User) session.getAttribute("user");
        userService.isUserLoggedIn(session, userLoggedIn.getId());
        message.setUserFrom(userLoggedIn);
        messageService.updateDeleteDate(message);
        logger.info("Message was deleted.");
        return new ResponseEntity<>("Message was deleted", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/remove-selected-messages")
    public ResponseEntity removeSelected(HttpSession session, @ModelAttribute List<Message> messageList)
            throws BadRequestException, InternalServerException {
        User userLoggedIn = (User) session.getAttribute("user");
        for (Message message : messageList) {
            message.setUserFrom(userLoggedIn);
        }
        messageService.updateSelectedMessages(messageList);
        logger.info("Messages are deleted.");
        return new ResponseEntity<> ("Messages are deleted.", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/remove-chat")
    public ResponseEntity removeAll(HttpSession session, @RequestParam String userId)
            throws BadRequestException, InternalServerException {
        User userLoggedIn = (User) session.getAttribute("user");
        messageService.updateAllDateDeleted(userLoggedIn.getId(), UtilString.stringToLong(userId));
        logger.info("Chat was removed.");
        return new ResponseEntity<> ("Chat was deleted.", HttpStatus.OK);
    }


    @GetMapping(path = "/chat/{userToIdStr}")
    public String getMessages(HttpSession session, Model model,
                              @PathVariable String userToIdStr,
                              @RequestParam(required = false) String curMessagesIndexFrom)
            throws BadRequestException, InternalServerException, NotFoundException {
        Integer messagesIndexFrom = 0;
        List<Message> messages;
        User userLoggedIn = (User) session.getAttribute("user");
        Long userToId = UtilString.stringToLong(userToIdStr);

        if (curMessagesIndexFrom == null) {
            messages = messageService.findMessagesByIds(userLoggedIn.getId(),userToId, messagesIndexFrom);
            messagesIndexFrom = 20;
        } else {
            messagesIndexFrom = Integer.valueOf(curMessagesIndexFrom);
            messages = messageService.findMessagesByIds(userLoggedIn.getId(),userToId, messagesIndexFrom);
            messagesIndexFrom += 20;
        }
        model.addAttribute("messages", messages);
        model.addAttribute("messagesIndexFrom", messagesIndexFrom);
        logger.info("Chat was opened.");
        return "message";
    }



    @GetMapping("/findById-message/{messageId}")
    public @ResponseBody
    String findById(@PathVariable String messageId) {
        Message message = messageService.findById(UtilString.stringToLong(messageId));
        return "Message " + message.getId() +" was found: " + message;
    }


}
