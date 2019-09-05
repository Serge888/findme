package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.dao.UserDao;
import com.findme.util.UtilString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;


    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) throws InternalServerException {
        if (user == null
                || user.getPhone() == null
                || user.getEmailAddress() == null) {
             throw new BadRequestException("User can't be saved without Phone Number or Email Address.");
        }
        String emailAddress = user.getEmailAddress().trim();
        if (!UtilString.isEmail(emailAddress)) {
            throw new BadRequestException("Email Address contains a mistake");
        }
        String password = user.getPassword();
        if (password == null || password.length() < 6) {
            throw new BadRequestException("Your password must be longer than 5 symbols.");
        }

        String realPhoneNumber = UtilString.phoneChecker(user.getPhone());
        if (realPhoneNumber == null) {
            throw new BadRequestException("Phone Number contains a mistake");
        }

        if (userDao.findByEmailAddress(emailAddress) != null) {
            throw new BadRequestException("User with email address: " + emailAddress + " already exists.");
        }
        if (userDao.findByPhoneNumber(realPhoneNumber) != null) {
            throw new BadRequestException("User with phone number: " + realPhoneNumber + " already exists.");
        }
        user.setPassword(password);
        user.setDateRegistered(new Date());
        return userDao.save(user);
    }

    @Override
    public User update(User user) throws InternalServerException {
        return userDao.update(user);
    }

    @Override
    public User delete(User user) throws InternalServerException {
        return userDao.delete(user);
    }

    @Override
    public User findById(Long id) throws InternalServerException {
        return userDao.findById(id);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws InternalServerException {
        return userDao.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findByEmailAddress(String emailAddress) throws InternalServerException {
        return userDao.findByEmailAddress(emailAddress);
    }

    @Override
    public User userLogin(String emailAddress, String password) throws NotFoundException, InternalServerException {
        if (emailAddress == null || password == null) {
            throw new BadRequestException("Password and login can't be null.");
        }

        emailAddress = emailAddress.trim();
        if (!UtilString.isEmail(emailAddress)) {
            throw new BadRequestException("Login contains a mistake");
        }

        User user = userDao.findByEmailAddress(emailAddress);
        if (user == null) {
            throw new NotFoundException("User with email address: " + emailAddress + " was not found.");
        }

        if (user.getPassword() == null || !user.getPassword().equals(password)) {
            throw new BadRequestException("Incorrect password.");
        }
        return user;
    }


    @Override
    public void viewProfileValidation(HttpSession session, Long profileUserId, Relationship relationship) throws BadRequestException {
        if (session.getAttribute("id") == null) {
            throw new BadRequestException("First you should login.");
        }
        Long loggedInUserId = UtilString.stringToLong(session.getAttribute("id").toString());
        if ((relationship == null && !loggedInUserId.equals(profileUserId))
                || (!loggedInUserId.equals(profileUserId)
                && !FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus()))) {
            throw new BadRequestException("You can view your profile or your friends only.");
        }
    }

    @Override
    public void isUserLoggedIn(HttpSession session, Long userIdFrom) throws BadRequestException {
        if (session.getAttribute("id") != userIdFrom) {
            throw new BadRequestException("First you should login.");
        }
    }


}
