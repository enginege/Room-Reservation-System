package com.example.RoomReservationSystem.repository.room_repository;

import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.web.dto.RoomDto;
import org.hibernate.HibernateError;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

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
    public List<RoomDto> findAllRoomsInCompany(String companyName) { //Add a company parameter to display the rooms in users' company.(After implementing the login feature.)
        StringBuilder queryStr = new StringBuilder("SELECT ro.id AS roomId, ro.name AS roomName, ro.capacity AS capacity, ro.availability AS availability, c.name AS companyName " +
                "FROM room ro jOIN company c ON ro.company_id=c.id " +
                "WHERE ro.active_status=true AND c.name=:companyName " +
                "ORDER BY ro.name, c.name");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("roomId", StandardBasicTypes.LONG)
                .addScalar("roomName")
                .addScalar("capacity")
                .addScalar("availability")
                .addScalar("companyName")
                .setResultTransformer(Transformers.aliasToBean(RoomDto.class));
        query.setParameter("companyName", companyName);
        List<RoomDto> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<RoomDto> findAllRoomsOrderedByName() {
        StringBuilder queryStr = new StringBuilder("SELECT ro.name AS roomName, ro.capacity AS capacity, ro.availability AS availability, c.name AS companyName " +
                "FROM room ro jOIN company c ON ro.company_id=c.id " +
                "WHERE ro.active_status=true AND c.name=:companyName " +
                "ORDER BY ro.name, c.name");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("roomName")
                .addScalar("capacity")
                .addScalar("availability")
                .addScalar("companyName")
                .setResultTransformer(Transformers.aliasToBean(RoomDto.class));
        List<RoomDto> result = query.list();
        session.close();
        return result;
    }

    @Override
    public Room findByRoomName(String name){
        StringBuilder queryStr = new StringBuilder("SELECT ro.* FROM room ro WHERE ro.name=:name");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString()).addEntity("u", Room.class);
        query.setParameter("name",name);
        Room result = (Room) query.uniqueResult();
        session.close();
        return result;
    }

}
