package com.example.RoomReservationSystem.repository.user_repository;

import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.UserDto;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    SessionFactory sessionFactory;

    public Session getSession() throws HibernateError {
        Session ses = null;
        try {
            ses = sessionFactory.getCurrentSession();
        } catch (Exception e) {
        }
        if (ses == null) {
            try {
                ses = sessionFactory.openSession();
            } catch (Exception e) {
            }
        }
        return ses;
    }

    @Override
    public List<UserDto> findAllUsersOrderedByNameInCompany(String companyName){
        StringBuilder queryStr = new StringBuilder("SELECT u.name AS firstName, u.surname AS lastName, u.username AS userName, u.email AS email, " +
                "d.name AS departmentName " +
                "FROM users u JOIN department d ON u.department_id=d.id jOIN company c ON d.company_id=c.id " +
                "WHERE u.active_status=true AND c.name=:companyName ");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("firstName")
                .addScalar("lastName")
                .addScalar("userName")
                .addScalar("email")
                .addScalar("departmentName")
                .setResultTransformer(Transformers.aliasToBean(UserDto.class));
        query.setParameter("companyName", companyName);
        List<UserDto> result = query.list();
        session.close();
        return result;
   }

   @Override
   public List<UserDto> findAllOtherUsersOrderedByNameInCompany(String userName, String companyName) {
       StringBuilder queryStr = new StringBuilder("SELECT u.name AS firstName, u.surname AS lastName, u.username AS userName, u.email AS email, " +
               "d.name AS departmentName " +
               "FROM users u JOIN department d ON u.department_id=d.id jOIN company c ON d.company_id=c.id " +
               "WHERE u.active_status=true AND c.name=:companyName AND NOT u.username=:userName ");
       Session session = getSession();
       Query query=session.createSQLQuery(queryStr.toString())
               .addScalar("firstName")
               .addScalar("lastName")
               .addScalar("userName")
               .addScalar("email")
               .addScalar("departmentName")
               .setResultTransformer(Transformers.aliasToBean(UserDto.class));
       query.setParameter("companyName", companyName);
       query.setParameter("userName", userName);
       List<UserDto> result = query.list();
       session.close();
       return result;
   }

   @Override
   public User findByUserName(String userName){
       StringBuilder queryStr = new StringBuilder("SELECT u.* FROM users u WHERE u.username=:userName");
       Session session = getSession();
       Query query=session.createSQLQuery(queryStr.toString()).addEntity("u", User.class);
       query.setParameter("userName", userName);
       User result = (User) query.uniqueResult();
       session.close();
       return result;
   }

   @Override
   public String findByUserNameOrEmail(String userName, String email) {
       StringBuilder queryStr = new StringBuilder("SELECT u.* FROM users u WHERE u.username=:userName");
       Session session = getSession();
       Query query=session.createSQLQuery(queryStr.toString()).addEntity("u", User.class);
       query.setParameter("userName", userName);
       User result = (User) query.uniqueResult();
       StringBuilder queryStr2 = new StringBuilder("SELECT u.* FROM users u WHERE u.email=:email");
       Query query2=session.createSQLQuery(queryStr2.toString()).addEntity("u", User.class);
       query2.setParameter("email", email);
       User result2 = (User) query2.uniqueResult();
       session.close();
       if (result != null && result2 != null) {
           return "bothExists";
       } else if (result != null) {
           return "usernameExists";
       } else if (result2 != null) {
           return "emailExists";
       }
       else return "notExist";
   }


    @Override
    public String findOtherByUserNameOrEmail(String userName, String email, Long user_id) {
        StringBuilder queryStr = new StringBuilder("SELECT u.* FROM users u WHERE u.username=:userName AND u.id<>:user_id");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity("u", User.class);
        query.setParameter("userName", userName);
        query.setParameter("user_id", user_id);
        User result = (User) query.uniqueResult();
        StringBuilder queryStr2 = new StringBuilder("SELECT u.* FROM users u WHERE u.email=:email AND u.id<>:user_id");
        Query query2=session.createSQLQuery(queryStr2.toString()).addEntity("u", User.class);
        query2.setParameter("email", email);
        query2.setParameter("user_id", user_id);
        User result2 = (User) query2.uniqueResult();
        session.close();
        if (result != null && result2 != null) {
            return "bothExists";
        } else if (result != null) {
            return "usernameExists";
        } else if (result2 != null) {
            return "emailExists";
        }
        else return "notExist";
    }

   @Override
    public List<User> findUsersByUserNames(Set<String> userNames) {
       StringBuilder queryStr = new StringBuilder("SELECT u.* FROM users u WHERE u.username IN (:userNames)");
       Session session = getSession();
       Query query=session.createSQLQuery(queryStr.toString()).addEntity("u", User.class);
       query.setParameter("userNames",userNames);
       List<User> result = query.list();
       session.close();
       return result;
   }


   @Override
   public String findAnyReservationOfficerEmail() {
       StringBuilder queryStr = new StringBuilder("SELECT u.email " +
               "FROM users u JOIN users_roles ur ON u.id=ur.user_id JOIN roles r ON ur.role_id=r.id JOIN roles_authorities ra ON r.id=ra.role_id JOIN authorities a ON ra.authority_id=a.id " +
               "WHERE a.name='MANAGE'");
       Session session = getSession();
       Query query=session.createSQLQuery(queryStr.toString());
       List<String> result = query.list();
       session.close();
       return result.get(0);
   }
}
