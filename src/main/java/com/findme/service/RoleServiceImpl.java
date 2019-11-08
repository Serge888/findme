package com.findme.service;

import com.findme.dao.RoleDao;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role save(Role role) throws InternalServerException {
        return roleDao.save(role);
    }

    @Override
    public Role update(Role role) throws InternalServerException {
        return roleDao.update(role);
    }

    @Override
    public Role delete(Role role) throws InternalServerException {
        return roleDao.delete(role);
    }

    @Override
    public Role findById(Long id) throws NotFoundException, InternalServerException {
        return roleDao.findById(id);
    }
}
