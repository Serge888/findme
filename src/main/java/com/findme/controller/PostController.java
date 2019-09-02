package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.models.TechPostData;
import com.findme.service.PostService;
import com.findme.service.UserService;
import com.findme.util.UtilString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-post", produces = "text/plain")
    public ResponseEntity save(HttpSession session, @ModelAttribute TechPostData techPostData) {
        try {
            userService.isUserLoggedIn(session, techPostData.getUserPostedId());
            postService.save(techPostData);

        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Post was saved", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-post", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody Post post) {
        postService.delete(post);
        return "Post id " + post.getId() + " was deleted " + post;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-post/{postId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable String postId) throws NotFoundException, InternalServerException {
        Post post = postService.findById(UtilString.stringToLong(postId));
        return "Post " + post.getId() + " was found: " + post;
    }

}
