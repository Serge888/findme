package com.findme.controller;

import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import com.findme.util.UtilString;
import org.apache.log4j.Logger;
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
import java.util.List;

@Controller
public class RelationshipController {
    private static final Logger logger = Logger.getLogger(RelationshipController.class);
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
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        userService.isUserLoggedIn(session, userIdFromL);
        relationshipService.save(userIdFromL, userIdToL);
        logger.info("Request from user id " + userIdFrom + " to user id " + userIdTo + " was sent.");
        return new ResponseEntity<>("Your request to user id " + userIdTo + " was sent.",
                HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-relationship")
    public ResponseEntity updateRelationship(HttpSession session,
                                             @RequestParam String userIdFrom,
                                             @RequestParam String userIdTo,
                                             @RequestParam String status) {
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        FriendRelationshipStatus friendRelationshipStatus = FriendRelationshipStatus.valueOf(status);
        userService.isUserLoggedIn(session, userIdFromL);
        relationshipService.update(userIdFromL, userIdToL, friendRelationshipStatus);
        logger.info("Currently status between user id " + userIdTo + " and user id " + userIdFrom + " is " + status);
        return new ResponseEntity<>("Your currently status with user id " + userIdTo + " is "
                + status, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/get-income-requests/{userId}")
    public ResponseEntity<List<Relationship>> getIncomeRequests(HttpSession session, @PathVariable String userId) {
        userService.isUserLoggedIn(session, UtilString.stringToLong(userId));
        List<Relationship> relationshipList =
                new ArrayList<>(relationshipService.findByUserToId(UtilString.stringToLong(userId)));
        return new ResponseEntity<>(relationshipList, HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.GET, value = "/get-outcome-requests/{userId}")
    public ResponseEntity<List<Relationship>> getOutcomeRequests(HttpSession session, @PathVariable String userId) {
            userService.isUserLoggedIn(session, UtilString.stringToLong(userId));
        List<Relationship> relationshipList =
                    new ArrayList<>(relationshipService.findByUserFromId(UtilString.stringToLong(userId)));
        return new ResponseEntity<>(relationshipList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-relationship-status/{userIdFrom}/{userIdTo}")
    public ResponseEntity getRelationshipStatus(HttpSession session, @PathVariable  String userIdFrom,
                                          @PathVariable  String userIdTo) {
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
            userService.isUserLoggedIn(session, userIdFromL);
        Relationship relationship = relationshipService.findByIdFromAndIdTo(userIdFromL, userIdToL);
        return new ResponseEntity<>("Your relationship status is "
                + relationship.getFriendRelationshipStatus(), HttpStatus.OK);
    }

}
