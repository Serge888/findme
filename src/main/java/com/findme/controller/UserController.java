package com.findme.controller;

import com.findme.Util.UtilString;
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
    public String home(Model model, @PathVariable String  userId) {
        User user;
        user = userService.findById(UtilString.stringToLong(userId));
        model.addAttribute("user", user);
        return "profile";
    }



    @RequestMapping(method = RequestMethod.POST, value = "/save-user", produces = "text/plain")
    public @ResponseBody
    String save(@RequestBody User user) {
        userService.save(user);
        return "User id " + user.getId() + " was saved";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-user", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody User newUser) {
        Long id =  newUser.getId();
        User user = userService.findById(id);
        return "User id " + user.getId() + " was updated " + user;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-user", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody User user) {
        userService.delete(user);
        return "User id " + user.getId() + " was deleted " + user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-user/{userId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable String userId) {
        User user = userService.findById(UtilString.stringToLong(userId));
        return "User " + user.getId() +" was found: " + user;
    }


}
