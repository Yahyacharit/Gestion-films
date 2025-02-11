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
public class CategoryDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public CategoryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public List<Category> findAll() {
        Transaction tx = getCurrentSession().beginTransaction();
        List<Category> fromCategory = getCurrentSession().createQuery("from Category", Category.class).getResultList();
        tx.commit();
        return fromCategory;
    }

    public List<Category> findAllById(List<Byte> ids) {
        Transaction tx = getCurrentSession().beginTransaction();
        List<Category> categories = getCurrentSession().createQuery(
                        "FROM Category WHERE categoryId IN :ids", Category.class)
                .setParameter("ids", ids)
                .getResultList();
        tx.commit();
        return categories;
    }



    public Category findById(Byte id) {
        Transaction tx = getCurrentSession().beginTransaction();
        Category category = getCurrentSession().get(Category.class, id);
        tx.commit();
        return category;
    }


    public void save(Category category) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().persist(category);
        tx.commit();
    }


    public void deleteById(int categoryId) {
        Transaction tx = getCurrentSession().beginTransaction();
        Category category = getCurrentSession().get(Category.class, categoryId);

        if (category != null) {
            for (Film film : category.getFilms()) {
                film.getCategories().remove(category);
                getCurrentSession().merge(film);
            }
            getCurrentSession().remove(category);
        }
        tx.commit();
    }

    public void update(Category category) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().merge(category);
        tx.commit();
    }
}