package com.app.pfmjee.DAO;

import com.app.pfmjee.Entities.Actor;
import com.app.pfmjee.Entities.Category;
import com.app.pfmjee.Entities.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class FilmDAO {

    private SessionFactory sessionFactory;

    public FilmDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Film> findAll() {
        Transaction tx = getCurrentSession().beginTransaction();
        List<Film> films = getCurrentSession()
                .createQuery("from Film", Film.class)
                .getResultList();
        tx.commit();
        return films;
    }


    public Film findById(int theId) {
        Transaction tx = getCurrentSession().beginTransaction();
        Film film = getCurrentSession().get(Film.class, theId);
        tx.commit();
        return film;
    }

    public void save(Film theFilm) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().merge(theFilm);
        tx.commit();
    }

    public void deleteById(int filmId) {
        Transaction tx = getCurrentSession().beginTransaction();
        Film film = getCurrentSession().get(Film.class, filmId);
        getCurrentSession().remove(film);
        tx.commit();
    }

    public void update(Film film) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().merge(film);
        tx.commit();
    }

    public List<Film> findFilmsPaginated(int page, int size) {
        String query = "SELECT f FROM Film f ORDER BY f.filmId";
        Transaction tx = getCurrentSession().beginTransaction();
        List<Film> resultList = getCurrentSession().createQuery(query, Film.class)
                .setFirstResult(page * size) // Définir l'offset
                .setMaxResults(size) // Définir la limite
                .getResultList();
        tx.commit();
        return resultList;
    }

    public int countFilms() {
        Transaction tx = getCurrentSession().beginTransaction();
        int i = ((Number) getCurrentSession().createQuery("SELECT COUNT(f) FROM Film f").getSingleResult()).intValue();
        tx.commit();
        return i;
    }

}