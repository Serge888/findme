package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.LoginException;
import com.findme.exception.NotFoundException;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.service.RelationshipService;
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

@Controller
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class);
    private UserService userService;
    private RelationshipService relationshipService;

    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
   public ResponseEntity loginUser(HttpSession session, @RequestParam String emailAddress,
                                   @RequestParam String password) {
        User foundUser = userService.userLogin(emailAddress, password);
        session.setAttribute("user", foundUser);
        session.setAttribute("news", 0);
        logger.info("User id " + foundUser.getId() + " was logged in");
        return new ResponseEntity<>(HttpStatus.OK);
   }


    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public ResponseEntity logoutUser(HttpSession session) {
        User user;
        if (session != null && !session.isNew() && session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
            session.removeAttribute("user");
            session.removeAttribute("news");
        } else {
            throw new LoginException("You were not logged in.");
        }
        logger.info("User id " + user.getId() + " was logged out");
        return new ResponseEntity<>("Hope see you soon.", HttpStatus.OK);
    }


   @RequestMapping(method = RequestMethod.POST, value = "/user-registration")
   public ResponseEntity registerUser(@ModelAttribute User user) {
        userService.save(user);
        logger.info("User " + user.getFirstName() + " " + user.getLastName() + " was registered.");
        return new ResponseEntity<>("User " + user.getFirstName() + " was registered.", HttpStatus.OK);
   }


    @RequestMapping(method = RequestMethod.GET, path = "/registration")
    public String home() {
        return "registration";
    }



    @RequestMapping(method = RequestMethod.GET, path = "/user/{userId}")
    public String home(HttpSession session, Model model, @PathVariable String  userId) {
        Long profileUserId = UtilString.stringToLong(userId);
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new LoginException("You should be logged in first.");
        }
        Relationship relationship = relationshipService.findByIds(loggedInUser.getId(), profileUserId);
        userService.viewProfileValidation(session, profileUserId, relationship);
        User user;
        user = userService.findById(profileUserId);
        model.addAttribute("user", user);
        return "profile";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-user", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody User newUser) {
        Long id =  newUser.getId();
        User user = userService.findById(id);
        if (user == null) {
            throw new NotFoundException("User id " + id + " was not found");
        }
        logger.info("User " + user.getFirstName() + " " + user.getLastName() + "was updated");
        return "User id " + user.getId() + " was updated " + user;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-user", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody User user) {
        userService.delete(user);
        logger.info("User " + user.getFirstName() + " " + user.getLastName() + "was deleted");
        return "User id " + user.getId() + " was deleted " + user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-user/{userId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable String userId) {
        User user = userService.findById(UtilString.stringToLong(userId));
        if (user == null) {
            throw new NotFoundException("User id " + userId + " was not found");
        }
        return "User " + user.getId() +" was found: " + user;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/findByEmailAddress-user", produces = "text/plain")
    public @ResponseBody
    String findByEmailAddress(@RequestBody String emailAddress) {
        if (UtilString.isEmail(emailAddress)) {
            User user = userService.findByEmailAddress(emailAddress);
            if (user == null) {
                throw new NotFoundException("User Email Address: " + emailAddress + " was not found");
            }
            return "User with Email Address " + user.getEmailAddress() + " was found: " + user;
        }
        throw new BadRequestException("The Email Address: " + emailAddress + " contains a mistake.");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/findByPhoneNumber-user", produces = "text/plain")
    public @ResponseBody
    String findByPhoneNumber(@RequestBody String phoneNumber) {
        String realPhoneNumber = UtilString.phoneChecker(phoneNumber);
        if (realPhoneNumber != null) {
            User user = userService.findByPhoneNumber(realPhoneNumber);
            if (user == null) {
                throw new NotFoundException("User Phone Number: " + phoneNumber + " was not found");
            }
            return "User with Phone Number " + user.getPhone() + " was found: " + user;
        }
        throw new BadRequestException("The Phone Number: " + phoneNumber + " contains a mistake.");
    }

}
