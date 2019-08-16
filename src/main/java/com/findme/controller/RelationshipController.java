package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Relationship;
import com.findme.service.RelationshipService;
import com.findme.util.UtilString;
import com.findme.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RelationshipController {
    private RelationshipService relationshipService;
    private UserValidation userValidation;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, UserValidation userValidation) {
        this.relationshipService = relationshipService;
        this.userValidation = userValidation;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add-relationship")
    public ResponseEntity addRelationship(HttpSession session, @RequestParam String userIdFrom,
                                          @RequestParam String userIdTo) {
        try {
            userValidation.isUserLoggedIn(session, userIdFrom);
            relationshipService.save(userIdFrom, userIdTo);
        }  catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Your request to user id " + userIdTo + " was sent.",
                HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-relationship")
    public ResponseEntity updateRelationship(HttpSession session,
                                             @RequestParam String userIdFrom,
                                             @RequestParam String userIdTo,
                                             @RequestParam String status) {
        try {
            userValidation.isUserLoggedIn(session, userIdFrom);
            relationshipService.update(userIdFrom, userIdTo, status);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Your currently status with user id " + userIdTo + " is "
                + status, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/get-income-requests/{userId}")
    public ResponseEntity<List<Relationship>> getIncomeRequests(HttpSession session, @PathVariable String userId) {
        List<Relationship> relationshipList;
        try {
            userValidation.isUserLoggedIn(session, userId);
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
            userValidation.isUserLoggedIn(session, userId);
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

    @RequestMapping(method = RequestMethod.GET, value = "/get-relationship-status/{userIdFrom}/{userIdTo}")
    public ResponseEntity getRelationshipStatus(HttpSession session, @PathVariable  String userIdFrom,
                                          @PathVariable  String userIdTo) {
        Relationship relationship;
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        try {
            userValidation.isUserLoggedIn(session, userIdFrom);
            relationship = relationshipService.findByIdFromAndIdTo(userIdFromL, userIdToL);
        }  catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Your relationship status is "
                + relationship.getFriendRelationshipStatus(), HttpStatus.OK);
    }

}
