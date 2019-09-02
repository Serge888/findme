package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.models.TechPostData;

public interface PostService {

    Post save(TechPostData techPostData) throws InternalServerException;
    Post delete(Post post) throws InternalServerException;
    Post findById(Long id) throws InternalServerException, NotFoundException;
}
