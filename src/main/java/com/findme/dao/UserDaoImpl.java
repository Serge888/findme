package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Repository
public class UserDaoImpl extends GeneralDao<User> implements UserDao {

    @Override
    public User findById(Long id) throws InternalServerException {
        User user;
        try {
            user =  entityManager.find(User.class, id);
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findById userId = " + id);
        }
        return user;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws InternalServerException {
        User user;
        try {
            user =  entityManager.createNamedQuery("User.FindByPhone", User.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        }  catch (NoResultException ex) {
            user = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByPhoneNumber Phone Number: "
                    + phoneNumber);
        }
        return user;
    }





    @Override
    public User findByEmailAddress(String emailAddress) throws InternalServerException {
        User user;
        try {
            user = entityManager.createNamedQuery("User.findByEmailAddress", User.class)
                    .setParameter("emailAddress", emailAddress)
                    .getSingleResult();
        } catch (NoResultException ex) {
            user = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByEmailAddress Email Address: "
                    + emailAddress);
        }
        return user;
    }
}
