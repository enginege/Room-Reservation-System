package com.example.RoomReservationSystem.repository.department_repository;

import com.example.RoomReservationSystem.model.Department;
import com.example.RoomReservationSystem.web.dto.DepartmentListDto;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DepartmentRepositoryCustomImpl implements DepartmentRepositoryCustom {

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
    public String companyNameByDepartmentName(String departmentName){
        StringBuilder queryStr = new StringBuilder("SELECT c.name FROM department d JOIN company c ON d.company_id = c.id WHERE d.name =:name");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        query.setParameter("name",departmentName);
        String result = (String) query.uniqueResult();
        session.close();
        return result;
     }

    @Override
    public  List<DepartmentListDto> findAllDepartmentsOrdered(){
        StringBuilder querystr = new StringBuilder("SELECT d.name AS departmentName,c.name AS companyName "
            +"FROM department d JOIN company c ON c.id=d.company_id "+
            "WHERE d.active_status=true "+
            "ORDER BY d.name");
        Session session = getSession();
        Query query=session.createSQLQuery(querystr.toString())
                .addScalar("departmentName")
                .addScalar("companyName")
                .setResultTransformer(Transformers.aliasToBean(DepartmentListDto.class));
        List<DepartmentListDto> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<String> findAllDepartmentNames() {
        StringBuilder queryStr = new StringBuilder("SELECT d.name FROM department d");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        List<String> result = query.list();
        session.close();
        return result;
    }

    @Override
    public Department findByDepartmentName(String departmentName) {
        StringBuilder queryStr = new StringBuilder("SELECT d.* FROM department d WHERE d.name=:departmentName");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity("d", Department.class);
        query.setParameter("departmentName",departmentName);
        Department result = (Department) query.uniqueResult();
        session.close();
        return result;
    }

    @Override
    public List<Department> findAllDepartments() {
        StringBuilder queryStr = new StringBuilder("SELECT d.* FROM department d WHERE d.active_status=true");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity("d", Department.class);
        List<Department> result = query.list();
        session.close();
        return result;
    }

}
