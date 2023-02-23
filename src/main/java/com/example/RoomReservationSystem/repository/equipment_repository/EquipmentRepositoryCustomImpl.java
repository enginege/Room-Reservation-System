package com.example.RoomReservationSystem.repository.equipment_repository;

import com.example.RoomReservationSystem.model.Equipment;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class EquipmentRepositoryCustomImpl implements EquipmentRepositoryCustom {
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
    public List<String> findAllEquipmentNames() {
        StringBuilder queryStr = new StringBuilder("SELECT e.name FROM equipment e");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        List<String> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<Equipment> findEquipmentsByEquipmentNames(Set<String> equipmentNames) {
        StringBuilder queryStr = new StringBuilder("SELECT e.* FROM equipment e WHERE e.name IN (:equipmentNames)");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity(Equipment.class);
        query.setParameter("equipmentNames", equipmentNames);
        List<Equipment> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<Object[]> findEquipmentsInUse(Set<String> equipmentNames, Date start, Date end) {
        StringBuilder queryStr = new StringBuilder("SELECT e.name, COUNT(e.name), e.amount " +
                "FROM reservation r JOIN reservations_equipments re ON r.id=re.reservation_id JOIN equipment e ON re.equipment_id=e.id " +
                "WHERE r.active_status=true AND r.reservation_status='A' AND ((r.start_time<=:start AND r.end_time>=:start) " +
                "OR (r.start_time<=:end AND r.end_time>=:end) OR (r.start_time>=:start AND r.end_time<=:end)) " +
                "AND e.name IN (:equipmentNames) " +
                "GROUP BY e.name, e.amount");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        query.setParameter("equipmentNames", equipmentNames);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Object[]> result = query.list();
        session.close();
        return result;


    }

}
