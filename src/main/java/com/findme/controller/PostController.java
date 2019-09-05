package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.models.TechPostData;
import com.findme.service.PostService;
import com.findme.service.UserService;
import com.findme.util.UtilString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;
    private UserService userService;
    @Value("${maxPostsAsNews:10}")
    private Integer maxPostsAsNews;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-post", produces = "text/plain")
    public ResponseEntity save(HttpSession session, @ModelAttribute TechPostData techPostData) {
        try {
            Long userPostedId = UtilString.stringToLong(techPostData.getUserPostedId());
            userService.isUserLoggedIn(session, userPostedId);
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

    @RequestMapping(method = RequestMethod.POST, value = "/findPostsByUserId", produces = "text/plain")
    public ResponseEntity<List<Post>> findPostsByUserId(HttpSession session, Model model,
                                                        @ModelAttribute PostFilter postFilter)
            throws NotFoundException, InternalServerException {
        List<Post> postList;
        try {
            Long userId = (Long) session.getAttribute("id");
            userService.isUserLoggedIn(session, userId);
            postFilter.setLoggedInUser(userId);
            postList = postService.findPostsByUserId(postFilter);
            model.addAttribute("postList", postList);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/feed", produces = "text/plain")
    public ResponseEntity<List<Post>> feedNews(HttpSession session, Model model)
            throws NotFoundException, InternalServerException {
        List<Post> postList;
        try {
            Long userId = (Long) session.getAttribute("id");
            Integer newsIndexFrom = (Integer) session.getAttribute("news");
            userService.isUserLoggedIn(session, userId);

            PostFilter postFilter = new PostFilter();
            postFilter.setLoggedInUser(userId);
            postFilter.setFriends("true");
            postFilter.setNewsIndexFrom(newsIndexFrom);

            postList = postService.getPostsAsNews(postFilter);
            model.addAttribute("postList", postList);

            newsIndexFrom += maxPostsAsNews;
            session.setAttribute("news", newsIndexFrom);

        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InternalServerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-post", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody Post post) {
        postService.delete(post);
        return "Post id " + post.getId() + " was deleted " + post;
    }


}
