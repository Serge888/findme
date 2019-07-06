package com.findme.controller;

import com.findme.exception.ResourceNotFoundException;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/{userId}")
    public String home(Model model, @PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("user", user);
        return "profile";
    }



    @RequestMapping(method = RequestMethod.POST, value = "/save-user", produces = "text/plain")
    public @ResponseBody
    String save(@RequestBody User user) {
        try {
            userService.save(user);
        } catch (Exception e) {
            e.getMessage();
            return "User id " + user.getId() + " was not saved";
        }
        return "User id " + user.getId() + " was saved";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-user", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody User newUser) {
        User user = new User();
        try {
            user = userService.findById(newUser.getId());

            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setAge(newUser.getAge());
            user.setCity(newUser.getCity());
            user.setCountry(newUser.getCountry());
            user.setDateLastActive(newUser.getDateLastActive());
            user.setPhone(newUser.getPhone());
            user.setRelationShipStatus(newUser.getRelationShipStatus());
            user.setSchool(newUser.getSchool());
            user.setUniversity(newUser.getUniversity());
            user.setReligion(newUser.getReligion());

            userService.update(user);
        } catch (Exception e) {
            e.getMessage();
            return "User id " + user.getId() + " was not updated";
        }
        return "User id " + user.getId() + " was updated " + user;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-user", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody User user) {
        try {
            userService.delete(user.getId());
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            return "User id " + user.getId() + " was not deleted";

        }
        return "User id " + user.getId() + " was deleted " + user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-user/{userId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable Long userId) {
        User user = new User();
        try {
            user = userService.findById(userId);
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            return "User " + user.getId() +" was not found.";
        }
        return "User " + user.getId() +" was found: " + user;
    }
}
