package com.findme.controller;

import com.findme.Util.UtilString;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.findme.models.FriendRelationshipStatus.*;

@Controller
public class RelationshipController {
    private RelationshipService relationshipService;
    private UserService userService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, UserService userService) {
        this.relationshipService = relationshipService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add-relationship")
    public ResponseEntity addRelationship(HttpSession session, @RequestParam String userIdFrom,
                                          @RequestParam String userIdTo) {
        Relationship relationship = new Relationship();
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        User userFrom;
        User userTo;
        try {
            isUserLoggedIn(session, userIdFrom);
            userFrom = userService.findById(userIdFromL);
            userTo = userService.findById(userIdToL);
            relationship.setUserFromId(userFrom.getId());
            relationship.setUserToId(userTo.getId());
            relationshipService.save(relationship);

        }  catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Your request to " + userTo.getFirstName() + " was sent.",
                HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/update-relationship")
    public ResponseEntity updateRelationship(HttpSession session,
                                             @RequestParam String userIdFrom,
                                             @RequestParam String userIdTo,
                                             @RequestParam String status) {
        Relationship relationship = new Relationship();
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        User userFrom;
        User userTo;
        try {
            isUserLoggedIn(session, userIdTo);
            userFrom = userService.findById(userIdFromL);
            userTo = userService.findById(userIdToL);
            relationship.setUserFromId(userFrom.getId());
            relationship.setUserToId(userTo.getId());
            relationship.setFriendRelationshipStatus(findFriendRelationshipStatus(status));
            relationshipService.update(relationship);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Your request to " + userTo.getFirstName() + " was sent." +
                " The currently status is " + relationship.getFriendRelationshipStatus(),
                HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/cancel-relationship")
    public ResponseEntity cancelRelationship(HttpSession session,
                                             @RequestParam String userIdFrom,
                                             @RequestParam String userIdTo) {
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        try {
            isUserLoggedIn(session, userIdFrom);
            relationshipService.cancelRelationship(userIdFromL, userIdToL);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/get-income-requests/{userId}")
    public ResponseEntity<List<Relationship>> getIncomeRequests(HttpSession session, @PathVariable String userId) {
        List<Relationship> relationshipList;
        try {
            isUserLoggedIn(session, userId);
            relationshipList =
                    new ArrayList<>(relationshipService.findByUserToId(UtilString.stringToLong(userId)));

        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(relationshipList, HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.GET, value = "/get-outcome-requests/{userId}")
    public ResponseEntity<List<Relationship>> getOutcomeRequests(HttpSession session, @PathVariable String userId) {
        List<Relationship> relationshipList;

        try {
            isUserLoggedIn(session, userId);
            relationshipList =
                    new ArrayList<>(relationshipService.findByUserFromId(UtilString.stringToLong(userId)));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(relationshipList, HttpStatus.OK);
    }





    private void isUserLoggedIn(HttpSession session, String userIdFrom) throws BadRequestException {
        if (session.getAttribute("id") != UtilString.stringToLong(userIdFrom)) {
            throw new BadRequestException("First you should login.");
        }
    }

    private FriendRelationshipStatus findFriendRelationshipStatus(String status) {
        switch (status) {
            case "FRIEND":
                return FRIEND;
            case "NO_LONGER_FRIENDS":
                return NO_LONGER_FRIENDS;
            case "REQUEST_SENT":
                return REQUEST_SENT;
            case "REQUEST_DENIED":
                return REQUEST_DENIED;
        }
        throw new BadRequestException("Unknown Friend Relationship Status.");
    }
}
