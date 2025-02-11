package com.app.pfmjee.DAO;

import com.app.pfmjee.Entities.Actor;
import com.app.pfmjee.Entities.Category;
import com.app.pfmjee.Entities.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActorDAO {

    @Autowired
    private final SessionFactory sessionFactory;

    public ActorDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public List<Actor> findAll() {
        Transaction tx = getCurrentSession().beginTransaction();
        List<Actor> fromActor = getCurrentSession().createQuery("from Actor", Actor.class).getResultList();
        tx.commit();
        return fromActor;
    }


    public Actor findById(int id) {
        Transaction tx = getCurrentSession().beginTransaction();
        Actor actor = getCurrentSession().get(Actor.class, id);
        tx.commit();
        return actor;
    }

    public List<Actor> findAllById(List<Short> ids) {
        Transaction tx = getCurrentSession().beginTransaction();
        List<Actor> actors = getCurrentSession().createQuery(
                        "FROM Actor WHERE actorId IN :ids", Actor.class)
                .setParameter("ids", ids)
                .getResultList();
        tx.commit();
        return actors;
    }


    public void save(Actor actor) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().persist(actor);
        tx.commit();
    }

    public void update(Actor actor) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().merge(actor);
        tx.commit();
    }


    public void deleteById(int actorId) {
        Transaction tx = getCurrentSession().beginTransaction();
        Actor actor = getCurrentSession().get(Actor.class, actorId);

        if (actor != null) {
            for (Film film : actor.getFilms()) {
                film.getActors().remove(actor);
                getCurrentSession().merge(film);
            }
            getCurrentSession().remove(actor);
        }
        tx.commit();
    }


}