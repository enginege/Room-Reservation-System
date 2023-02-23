package com.example.RoomReservationSystem.repository.company_repository;

import com.example.RoomReservationSystem.model.Company;
import com.example.RoomReservationSystem.model.User;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

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
    public List<String> findAllCompanyNames() {
        StringBuilder queryStr = new StringBuilder("SELECT c.name FROM company c");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        List<String> result = query.list();
        session.close();
        return result;
    }

    @Override
    public Company getCompanyByCompanyName(String companyName) {
        StringBuilder queryStr = new StringBuilder("SELECT c.* FROM company c WHERE c.name=:companyName");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity("c", Company.class);;
        query.setParameter("companyName",companyName);
        Company result = (Company) query.uniqueResult();
        session.close();
        return result;
    }

    @Override
    public String getCompanyNameByUsername(String username) {
        StringBuilder queryStr = new StringBuilder("SELECT c.name " +
                "FROM users u JOIN department d ON u.department_id=d.id JOIN company c ON d.company_id=c.id " +
                "WHERE u.username=:username");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        query.setParameter("username", username);
        String result = (String) query.uniqueResult();
        session.close();
        return result;
    }
}
