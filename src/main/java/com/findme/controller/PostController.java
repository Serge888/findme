package com.findme.controller;

import com.findme.Util.UtilString;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-post", produces = "text/plain")
    public @ResponseBody
    String save(@RequestBody Post post) {
        postService.save(post);
        return "Post id " + post.getId() + " was saved";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-post", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody Post newPost) {
        Post post  = postService.findById(newPost.getId());
        post.setMessage(newPost.getMessage());
        postService.update(post);
        return "Post id " + post.getId() + " was updated " + post;
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
        return "Post " + post.getId() +" was found: " + post;
    }

}
