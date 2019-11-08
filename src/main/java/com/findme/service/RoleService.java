package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Role;

public interface RoleService {

    Role save(Role role) throws InternalServerException;
    Role update(Role role) throws InternalServerException;
    Role delete(Role role) throws InternalServerException;
    Role findById(Long id) throws NotFoundException, InternalServerException;
}
