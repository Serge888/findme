package com.findme.service;

import com.findme.models.Post;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    Post save(Post post);
    Post update(Post post);
    Post delete(Post post);
    Post findById(Long id);
}
