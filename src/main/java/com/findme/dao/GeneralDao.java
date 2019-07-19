package com.findme.dao;

import com.findme.exception.InternalServerException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class GeneralDao<Z> {

    @PersistenceContext
    public EntityManager entityManager;

    public Z save(Z z) throws InternalServerException {
        try {
            entityManager.persist(z);
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong with save entity: " + z);
        }

        return z;
    }

    public Z update(Z z) throws InternalServerException {
        try {
            entityManager.merge(z);
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong with update entity: " + z);
        }
        return z;
    }

    public Z delete(Z z) throws InternalServerException {
        try {
            entityManager.remove(z);
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong with delete entity: " + z);
        }
        return z;
    }
}