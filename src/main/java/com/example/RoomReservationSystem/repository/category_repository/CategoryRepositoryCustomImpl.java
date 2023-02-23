package com.example.RoomReservationSystem.repository.category_repository;

import com.example.RoomReservationSystem.model.Category;
import com.example.RoomReservationSystem.web.dto.CategoryDto;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    SessionFactory sessionFactory;



    public Session getSession() throws HibernateError {
        Session ses = null;
        try {ses = sessionFactory.getCurrentSession();}
        catch (Exception e){

        }
        if(ses ==null){
            ses=sessionFactory.openSession();
        }
        return ses;
    }

    @Override
    public Category findCategoryByName(String name) {
        StringBuilder queryStr = new StringBuilder("SELECT ca.* FROM category ca WHERE ca.name=:name");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                        .addEntity(Category.class);
        query.setParameter("name", name);
        Category result = (Category) query.uniqueResult();
        session.close();
        return result;
    }



    @Override
    public List<String> findAllCategoryNames() {
        StringBuilder queryStr = new StringBuilder("SELECT ca.name FROM category ca WHERE ca.active_status=true");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        List<String> result = query.list();
        session.close();
        return result;
    }

}
