package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Role;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class RoleDaoImpl extends GeneralDao<Role> implements RoleDao {


    @Override
    public Role findById(Long id) throws InternalServerException, NotFoundException {
        Role role;
        try {
            role = entityManager.find(Role.class, id);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findById roleId = " + id);
        }
        if (role == null) {
            throw new NotFoundException("Role id " + id + " was not found");
        }
        return role;
    }
}
