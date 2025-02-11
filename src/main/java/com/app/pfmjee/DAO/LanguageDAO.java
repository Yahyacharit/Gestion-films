package com.app.pfmjee.DAO;

import com.app.pfmjee.Entities.Film;
import com.app.pfmjee.Entities.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LanguageDAO {

    @Autowired
    private FilmDAO filmDAO;
    private final SessionFactory sessionFactory;

    @Autowired
    public LanguageDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public List<Language> findAll() {
        Transaction tx = getCurrentSession().beginTransaction();
        List<Language> fromLanguage = getCurrentSession().createQuery("from Language ", Language.class).getResultList();
        tx.commit();
        return fromLanguage;
    }


    public Language findById(Byte id) {
        Transaction tx = getCurrentSession().beginTransaction();
        Language language = getCurrentSession().get(Language.class, id);
        tx.commit();
        return language;
    }


    public void save(Language language) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().persist(language);
        tx.commit();
    }


    public void deleteById(int languageId) {
        Transaction tx = getCurrentSession().beginTransaction();
        Language language = getCurrentSession().get(Language.class, languageId);
        if (language != null) {
            for (Film film : filmDAO.findAll()) {
                film.setLanguage(null);
                getCurrentSession().merge(film);
            }
            getCurrentSession().remove(language);
        }
        tx.commit();
    }
}