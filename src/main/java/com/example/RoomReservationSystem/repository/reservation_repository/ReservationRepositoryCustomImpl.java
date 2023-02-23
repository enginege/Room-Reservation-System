package com.example.RoomReservationSystem.repository.reservation_repository;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import com.example.RoomReservationSystem.model.Reservation;
import com.example.RoomReservationSystem.web.dto.ReservationDto;
import com.example.RoomReservationSystem.web.dto.ReservationListDto;
import com.example.RoomReservationSystem.web.dto.ReservationJsonDto;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.ArrayType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;

import java.util.Date;
import java.util.List;

public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

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

    @Override  //replace "u.name" with "r.creator_user"  //add "Reservation_status = Approved" condition
    public List<ReservationJsonDto> findAllReservationsInCompany(String companyName){
        StringBuilder queryStr = new StringBuilder("SELECT u.name AS userName, u.surname AS userSurName, ro.name AS roomName, ca.name AS title, re.start_time AS start, re.end_time AS end " +
                "FROM category ca JOIN reservation re ON ca.id=re.category_id JOIN room ro ON re.room_id=ro.id JOIN company c ON ro.company_id=c.id JOIN users_reservations ur ON ur.reservation_id=re.id " +
                "JOIN users u ON ur.user_id=u.id " +
                "WHERE re.active_status=true AND c.name=:companyName AND re.reservation_status='A' AND re.creator_user=u.username AND re.creator_user=u.username");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("title")
                .addScalar("userName")
                .addScalar("userSurName")
                .addScalar("roomName")
                .addScalar("start")
                .addScalar("end")
                .setResultTransformer(Transformers.aliasToBean(ReservationJsonDto.class));
        query.setParameter("companyName", companyName);
        List<ReservationJsonDto> result = (List<ReservationJsonDto>) query.list();
        for(ReservationJsonDto dto : result){
            dto.setTitle(dto.getTitle() + " - " + dto.getRoomName() + " - " + dto.getUserName() + " " + dto.getUserSurName());
            dto.setDescription(dto.getTitle() + " - " + dto.getRoomName() + " - " + dto.getUserName() + " " + dto.getUserSurName());
        }
        session.close();
        return result;
    }

    @Override  //replace "u.name" with "r.creator_user"  //add "Reservation_status = Approved" condition
    public List<ReservationJsonDto> findAllReservationsInCompanyInRoom(String companyName, Long room_id){
        StringBuilder queryStr = new StringBuilder("SELECT u.name AS userName, u.surname AS userSurName, ca.name AS title, re.start_time AS start, re.end_time AS end " +
                "FROM category ca JOIN reservation re ON ca.id=re.category_id JOIN room ro ON re.room_id=ro.id JOIN company c ON ro.company_id=c.id JOIN users_reservations ur ON ur.reservation_id=re.id " +
                "JOIN users u ON ur.user_id=u.id " +
                "WHERE re.active_status=true AND c.name=:companyName AND re.reservation_status='A' AND ro.id=:room_id AND re.creator_user=u.username");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("title")
                .addScalar("userName")
                .addScalar("userSurName")
                .addScalar("start")
                .addScalar("end")
                .setResultTransformer(Transformers.aliasToBean(ReservationJsonDto.class));
        query.setParameter("companyName", companyName);
        query.setParameter("room_id", room_id);
        List<ReservationJsonDto> result = (List<ReservationJsonDto>) query.list();
        for(ReservationJsonDto dto : result){
            dto.setTitle(dto.getTitle() + " - " + dto.getUserName() + " " + dto.getUserSurName());
            dto.setDescription(dto.getTitle() + " - " + dto.getUserName() + " " + dto.getUserSurName());
        }
        session.close();
        return result;
    }

    @Override
    public List<ReservationListDto> findReservationsInCompanyWithStatus(String companyName, Reservation_Status reservationStatus) {
        String reservationStatusCode = reservationStatus.getCode();
        StringBuilder queryStr = new StringBuilder("SELECT ARRAY_AGG(distinct CONCAT(u.name, ' ', u.surname)) AS userNames, ARRAY_AGG(distinct e.name) AS equipmentNames, " +
                "re.id AS reservation_id, ca.name AS name, re.start_time AS start, re.end_time AS end, " +
                "re.urgency AS urgency, re.cleaning_service AS cleaningService, re.food_service AS foodService, re.notes AS notes, re.reservation_status AS status, " + //"re.reservation_status AS status, " +
                "ro.name AS roomName, CONCAT(u2.name, ' ', u2.surname) AS creator " +
                "FROM category ca JOIN reservation re ON ca.id=re.category_id JOIN room ro ON re.room_id=ro.id JOIN company c ON ro.company_id=c.id LEFT JOIN users_reservations ur ON re.id=ur.reservation_id LEFT JOIN users u ON ur.user_id=u.id LEFT JOIN reservations_equipments re2 ON re.id=re2.reservation_id LEFT JOIN equipment e ON re2.equipment_id=e.id JOIN users u2 on re.creator_user=u2.username " +
                "WHERE re.active_status=true AND c.name=:companyName AND (re.reservation_status=:Status OR re.reservation_status='C' OR re.reservation_status='A') " +
                "GROUP BY re.id, ca.name, ro.name, u2.name, u2.surname");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("userNames")
                .addScalar("equipmentNames")
                .addScalar("reservation_id", StandardBasicTypes.LONG)
                .addScalar("name")
                .addScalar("start")
                .addScalar("end")
                .addScalar("status")
                .addScalar("roomName")
                .addScalar("creator")
                .addScalar("urgency")
                .addScalar("cleaningService")
                .addScalar("foodService")
                .addScalar("notes")
                .setResultTransformer(Transformers.aliasToBean(ReservationListDto.class));
        query.setParameter("companyName", companyName);
        query.setParameter("Status", reservationStatusCode);
        List<ReservationListDto> result = query.list();
        session.close();
        return result;
    }

//    AND (re.reservation_status=:Status OR re.reservation_status='C')
//    query.setParameter("Status", reservationStatusCode);

    @Override
    public List<ReservationListDto> findReservationsInCompanyWithoutStatus(String companyName) {
        StringBuilder queryStr = new StringBuilder("SELECT ARRAY_AGG(distinct CONCAT(u.name, ' ', u.surname)) AS userNames, ARRAY_AGG(distinct e.name) AS equipmentNames, " +
                "re.id AS reservation_id, ca.name AS name, re.start_time AS start, re.end_time AS end, " +
                "re.urgency AS urgency, re.cleaning_service AS cleaningService, re.food_service AS foodService, re.notes AS notes, re.reservation_status AS status, " + //"re.reservation_status AS status, " +
                "ro.name AS roomName, CONCAT(u2.name, ' ', u2.surname) AS creator " +
                "FROM category ca JOIN reservation re ON ca.id=re.category_id JOIN room ro ON re.room_id=ro.id JOIN company c ON ro.company_id=c.id LEFT JOIN users_reservations ur ON re.id=ur.reservation_id LEFT JOIN users u ON ur.user_id=u.id LEFT JOIN reservations_equipments re2 ON re.id=re2.reservation_id LEFT JOIN equipment e ON re2.equipment_id=e.id JOIN users u2 on re.creator_user=u2.username " +
                "WHERE re.active_status=true AND c.name=:companyName " +
                "GROUP BY re.id, ca.name, ro.name, u2.name, u2.surname");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("userNames")
                .addScalar("equipmentNames")
                .addScalar("reservation_id", StandardBasicTypes.LONG)
                .addScalar("name")
                .addScalar("start")
                .addScalar("end")
                .addScalar("status")
                .addScalar("roomName")
                .addScalar("creator")
                .addScalar("urgency")
                .addScalar("cleaningService")
                .addScalar("foodService")
                .addScalar("notes")
                .setResultTransformer(Transformers.aliasToBean(ReservationListDto.class));
        query.setParameter("companyName", companyName);
        List<ReservationListDto> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<ReservationListDto> findReservationsInCompanyWithoutStatusWithUsername(String companyName, String userName) {
        StringBuilder queryStr = new StringBuilder("SELECT ARRAY_AGG(distinct CONCAT(u.name, ' ', u.surname)) AS userNames, ARRAY_AGG(distinct e.name) AS equipmentNames, " +
                "re.id AS reservation_id, ca.name AS name, re.start_time AS start, re.end_time AS end, " +
                "re.urgency AS urgency, re.cleaning_service AS cleaningService, re.food_service AS foodService, re.notes AS notes, re.reservation_status AS status, " + //"re.reservation_status AS status, " +
                "ro.name AS roomName, CONCAT(u2.name, ' ', u2.surname) AS creator " +
                "FROM category ca JOIN reservation re ON ca.id=re.category_id JOIN room ro ON re.room_id=ro.id JOIN company c ON ro.company_id=c.id LEFT JOIN users_reservations ur ON re.id=ur.reservation_id LEFT JOIN users u ON ur.user_id=u.id LEFT JOIN reservations_equipments re2 ON re.id=re2.reservation_id LEFT JOIN equipment e ON re2.equipment_id=e.id JOIN users u2 on re.creator_user=u2.username " +
                "WHERE re.active_status=true AND c.name=:companyName AND re.creator_user=:userName " +
                "GROUP BY re.id, ca.name, ro.name, u2.name, u2.surname");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("userNames")
                .addScalar("equipmentNames")
                .addScalar("reservation_id", StandardBasicTypes.LONG)
                .addScalar("name")
                .addScalar("start")
                .addScalar("end")
                .addScalar("status")
                .addScalar("roomName")
                .addScalar("creator")
                .addScalar("urgency")
                .addScalar("cleaningService")
                .addScalar("foodService")
                .addScalar("notes")
                .setResultTransformer(Transformers.aliasToBean(ReservationListDto.class));
        query.setParameter("companyName", companyName);
        query.setParameter("userName", userName);
        List<ReservationListDto> result = query.list();
        session.close();
        return result;
    }


    @Override
    public List<Reservation> findConflictingApprovedReservationsInRoom(Long room_id, Date start, Date end) {
        StringBuilder queryStr = new StringBuilder("SELECT re.* " +
                "FROM reservation re JOIN room ro ON re.room_id=ro.id " +
                "WHERE ro.id=:room_id AND re.active_status=true AND re.reservation_status='A' AND ((re.start_time<=:start AND re.end_time>=:start) " +
                "OR (re.start_time<=:end AND re.end_time>=:end) OR (re.start_time>=:start AND re.end_time<=:end))");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addEntity(Reservation.class);
        query.setParameter("room_id", room_id);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Reservation> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<Reservation> findConflictingPendingReservationsInRoom(Long room_id, Date start, Date end) {
        StringBuilder queryStr = new StringBuilder("SELECT re.* " +
                "FROM reservation re JOIN room ro ON re.room_id=ro.id " +
                "WHERE ro.id=:room_id AND re.active_status=true AND (re.reservation_status='P' OR re.reservation_status='C') AND ((re.start_time<=:start AND re.end_time>=:start) " +
                "OR (re.start_time<=:end AND re.end_time>=:end) OR (re.start_time>=:start AND re.end_time<=:end))");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addEntity(Reservation.class);
        query.setParameter("room_id", room_id);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Reservation> result = query.list();
        session.close();
        return result;
    }


    @Override
    public List<Reservation> findOtherConflictingReservationsInRoom(Long reservation_id, Long room_id, Date start, Date end) {
        StringBuilder queryStr = new StringBuilder("SELECT re.* " +
                "FROM reservation re JOIN room ro ON re.room_id=ro.id " +
                "WHERE re.id<>:reservation_id AND ro.id=:room_id AND re.active_status=true AND (re.reservation_status='A' OR re.reservation_status='C') AND ((re.start_time<=:start AND re.end_time>=:start) " +
                "OR (re.start_time<=:end AND re.end_time>=:end) OR (re.start_time>=:start AND re.end_time<=:end))");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addEntity(Reservation.class);
        query.setParameter("reservation_id", reservation_id);
        query.setParameter("room_id", room_id);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Reservation> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<ReservationListDto> findConflictingReservationsInRoom(Long room_id, Date start, Date end) {
        StringBuilder queryStr = new StringBuilder("SELECT ARRAY_AGG(distinct CONCAT(u.name, ' ', u.surname)) AS userNames, ARRAY_AGG(distinct e.name) AS equipmentNames, " +
                "re.id AS reservation_id, ca.name AS name, re.start_time AS start, re.end_time AS end, " +
                "re.urgency AS urgency, re.cleaning_service AS cleaningService, re.food_service AS foodService, re.notes AS notes, re.reservation_status AS status, " + //"re.reservation_status AS status, " +
                "ro.name AS roomName, CONCAT(u2.name, ' ', u2.surname) AS creator " +
                "FROM category ca JOIN reservation re ON ca.id=re.category_id JOIN room ro ON re.room_id=ro.id JOIN company c ON ro.company_id=c.id LEFT JOIN users_reservations ur ON re.id=ur.reservation_id LEFT JOIN users u ON ur.user_id=u.id LEFT JOIN reservations_equipments re2 ON re.id=re2.reservation_id LEFT JOIN equipment e ON re2.equipment_id=e.id JOIN users u2 on re.creator_user=u2.username " +
                "WHERE re.active_status=true AND ro.id=:room_id " +
                "AND (re.reservation_status='A' OR re.reservation_status='C') AND ((re.start_time<=:start AND re.end_time>=:start) " +
                "OR (re.start_time<=:end AND re.end_time>=:end) OR (re.start_time>=:start AND re.end_time<=:end)) " +
                "GROUP BY re.id, ca.name, ro.name, u2.name, u2.surname");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString())
                .addScalar("userNames")
                .addScalar("equipmentNames")
                .addScalar("reservation_id", StandardBasicTypes.LONG)
                .addScalar("name")
                .addScalar("start")
                .addScalar("end")
                .addScalar("status")
                .addScalar("roomName")
                .addScalar("creator")
                .addScalar("urgency")
                .addScalar("cleaningService")
                .addScalar("foodService")
                .addScalar("notes")
                .setResultTransformer(Transformers.aliasToBean(ReservationListDto.class));
        query.setParameter("room_id", room_id);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<ReservationListDto> result = query.list();
        session.close();
        return result;
    }


    @Override
    public List<String> findEmailsOfParticipants(Long reservation_id, String creatorName) {
        StringBuilder queryStr = new StringBuilder("SELECT u.email " +
                "FROM reservation re JOIN users_reservations ur ON re.id=ur.reservation_id JOIN users u ON ur.user_id=u.id " +
                "WHERE re.id=:reservation_id and u.username<>:creatorName");
        Session session = getSession();
        Query query=session.createSQLQuery(queryStr.toString());
        query.setParameter("reservation_id", reservation_id);
        query.setParameter("creatorName", creatorName);
        List<String> result = query.list();
        session.close();
        return result;
    }
}
