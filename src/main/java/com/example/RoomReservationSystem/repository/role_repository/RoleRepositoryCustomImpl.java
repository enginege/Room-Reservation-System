package com.example.RoomReservationSystem.repository.role_repository;

import com.example.RoomReservationSystem.model.Role;
import com.example.RoomReservationSystem.model.User;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoleRepositoryCustomImpl implements RoleRepositoryCustom {

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
    public List<Role> getUserNotRoles(Long id) {
        StringBuilder queryStr = new StringBuilder("SELECT r.* FROM roles r WHERE r.id NOT IN(SELECT ur.role_id FROM users_roles ur WHERE ur.user_id=:userId)");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity("r", Role.class);
        query.setParameter("userId", id);
        List<Role> result = query.list();
        session.close();
        return result;
    }
}
