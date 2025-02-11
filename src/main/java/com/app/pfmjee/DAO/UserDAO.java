package com.app.pfmjee.DAO;

import com.app.pfmjee.Entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<User> findAll() {
        Transaction tx = getCurrentSession().beginTransaction();
        List<User> users = getCurrentSession().createQuery("from User", User.class).getResultList();
        tx.commit();
        return users;
    }

    public User findById(Long id) {
        Transaction tx = getCurrentSession().beginTransaction();
        User user = getCurrentSession().get(User.class, id);
        tx.commit();
        return user;
    }

    public User findByUsername(String username) {
        Transaction tx = getCurrentSession().beginTransaction();
        User username1 = getCurrentSession().createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
        tx.commit();
        return username1;
    }

    public void save(User user) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().persist(user);
        tx.commit();
    }

    public void update(User user) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().merge(user);
        tx.commit();
    }

    public void deleteById(Long userId) {
        Transaction tx = getCurrentSession().beginTransaction();
        User user = getCurrentSession().get(User.class, userId);
        if (user != null) {
            getCurrentSession().remove(user);
        }
        tx.commit();
    }
}

