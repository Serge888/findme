package com.findme.exception;

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
        try {
            postService.save(post);
        } catch (Exception e) {
            e.getMessage();
            return "Post id " + post.getId() + " was not saved";
        }
        return "Post id " + post.getId() + " was saved";
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-post", produces = "text/plain")
    public @ResponseBody
    String update(@RequestBody Post newPost) {
        Post post = new Post();
        try {
            post = postService.findById(newPost.getId());
            post.setMessage(newPost.getMessage());
            postService.update(post);
        } catch (Exception e) {
            e.getMessage();
            return "Post id " + post.getId() + " was not updated";
        }
        return "Post id " + post.getId() + " was updated " + post;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-post", produces = "text/plain")
    public @ResponseBody
    String delete(@RequestBody Post post) {
        try {
            postService.delete(post.getId());
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            return "Post id " + post.getId() + " was not deleted";

        }
        return "Post id " + post.getId() + " was deleted " + post;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findById-post/{postId}", produces = "text/plain")
    public @ResponseBody
    String findById(@PathVariable Long postId) {
        Post post = new Post();
        try {
            post = postService.findById(postId);
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            return "Post " + post.getId() +" was not found.";
        }
        return "Post " + post.getId() +" was found: " + post;
    }

}
