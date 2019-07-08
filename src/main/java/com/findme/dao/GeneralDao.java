package com.findme.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class GeneralDao<Z> {

    @PersistenceContext
    public EntityManager entityManager;

    public Z save(Z z) {
        entityManager.persist(z);
        return z;
    }

    public Z update(Z z) {
        entityManager.merge(z);
        return z;
    }

    public Z delete(Z z) {
        entityManager.remove(z);
        return z;
    }
}