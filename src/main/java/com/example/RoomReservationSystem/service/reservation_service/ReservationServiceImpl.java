package com.example.RoomReservationSystem.service.reservation_service;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import com.example.RoomReservationSystem.model.Equipment;
import com.example.RoomReservationSystem.model.Reservation;
import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.model.User;
import com.example.RoomReservationSystem.repository.category_repository.CategoryRepository;
import com.example.RoomReservationSystem.repository.company_repository.CompanyRepository;
import com.example.RoomReservationSystem.repository.equipment_repository.EquipmentRepository;
import com.example.RoomReservationSystem.repository.reservation_repository.ReservationRepository;
import com.example.RoomReservationSystem.repository.room_repository.RoomRepository;
import com.example.RoomReservationSystem.repository.user_repository.UserRepository;
import com.example.RoomReservationSystem.service.EmailService;
import com.example.RoomReservationSystem.web.dto.ReservationDto;
import com.example.RoomReservationSystem.web.dto.ReservationListDto;
import com.example.RoomReservationSystem.web.dto.ReservationJsonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    EmailService emailService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<ReservationListDto> findReservationsInCompanyWithoutStatus(String username){
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return reservationRepository.findReservationsInCompanyWithoutStatus(companyName);
    }

    @Override
    public List<ReservationListDto> findReservationsInCompanyWithoutStatusWithUsername(String username) {
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return reservationRepository.findReservationsInCompanyWithoutStatusWithUsername(companyName, username);
    }

    @Override
    public List<ReservationJsonDto> findAllReservationsInCompany(String username) {
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return reservationRepository.findAllReservationsInCompany(companyName);
    }

    @Override
    public List<ReservationJsonDto> findAllReservationsInCompanyInRoom(String username, Long room_id) {
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return reservationRepository.findAllReservationsInCompanyInRoom(companyName, room_id);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<ReservationListDto> findReservationsInCompanyWithStatus(String username, Reservation_Status reservation_status) {
        String companyName = companyRepository.getCompanyNameByUsername(username);
        return reservationRepository.findReservationsInCompanyWithStatus(companyName, reservation_status);
    }

    @Override
    public String save(Long room_id, String userName, ReservationDto reservationDto) {
        String result = "success";
        Reservation_Status reservation_status = Reservation_Status.PENDING;
        User creatorUser = userRepository.findByUserName(userName);
        List<Reservation> conflictingApprovedReservations = reservationRepository.findConflictingApprovedReservationsInRoom(room_id, reservationDto.getStart(), reservationDto.getEnd());
        List<Reservation> conflictingPendingReservations = reservationRepository.findConflictingPendingReservationsInRoom(room_id, reservationDto.getStart(), reservationDto.getEnd());
        if (reservationDto.getUrgency().equals(false) && !conflictingApprovedReservations.isEmpty()) {
            result = "conflict";
            return result;
        }
        if (reservationDto.getUrgency().equals(true) && !conflictingApprovedReservations.isEmpty()) { //&& there is an approved reservation in that time range.
            //Also set the conflicting reservations' status to conflicting if they were pending before.
            reservation_status = Reservation_Status.CONFLICTING;
            result = "conflictpending";
            //TODO: Send an e-mail to an admin.
        }

        Room reservedRoom = roomRepository.findById(room_id).get();
        Reservation reservation = new Reservation(
                categoryRepository.findCategoryByName(reservationDto.getName()),
                reservationDto.getStart(),
                reservationDto.getEnd(),
                userName,
                reservationDto.getUrgency(),
                reservationDto.getCleaningService(),
                reservationDto.getFoodService(),
                reservationDto.getNotes(),
                reservation_status,
                reservedRoom
        );
//        List<Equipment> equipmentList = equipmentRepository.findEquipmentsByEquipmentNames(reservationDto.getEquipmentNames());
//        for (Equipment equipment : equipmentList){
//            reservation.addEquipment(equipment);
//        }

        String fooResult = "";
        Boolean exceededEquipment = false;
        List<Object[]> equipmentsInUse = equipmentRepository.findEquipmentsInUse(reservationDto.getEquipmentNames(), reservationDto.getStart(), reservationDto.getEnd());
        for (String equipmentName : reservationDto.getEquipmentNames()) {
            for (Object[] equipmentInUse : equipmentsInUse) {
                int count = ((BigInteger) equipmentInUse[1]).intValue();
                int amount = (int) equipmentInUse[2];
                if (equipmentName.equals(equipmentInUse[0]) && (count >= amount)) {
                    exceededEquipment = true;
                    fooResult += equipmentName + ", ";
                    //Maybe break;
                }
            }
        }
        if (exceededEquipment == true) {
            result = fooResult.substring(0, fooResult.length()-2);
            return result;
        }

        Set<Equipment> EquipmentFoo = new HashSet<Equipment>(equipmentRepository.findEquipmentsByEquipmentNames(reservationDto.getEquipmentNames()));
        reservation.setEquipments(EquipmentFoo);

        //TODO: Set assigned users to the reservation. (Maybe adjust the relationship between Users and Reservations to bi-directional!!!)
        if (reservationDto.getUserNames().size() + 1 > reservedRoom.getCapacity()) {
            result = "exceededCapacity#" + reservedRoom.getCapacity();
            return result;
        }
        Set<User> UserFoo = new HashSet<User>(userRepository.findUsersByUserNames(reservationDto.getUserNames()));
        reservation.setUsers(UserFoo);
        reservation.addUser(creatorUser);

        if (!conflictingPendingReservations.isEmpty()) {
            reservation.setReservation_status(Reservation_Status.CONFLICTING);
            for (Reservation conflictingPendingReservation : conflictingPendingReservations) {
                if (!conflictingPendingReservation.getReservation_status().equals(Reservation_Status.CONFLICTING)) {
                    conflictingPendingReservation.setReservation_status(Reservation_Status.CONFLICTING);
                    reservationRepository.save(conflictingPendingReservation);
                }
            }
            //reservationRepository.saveAll(conflictingPendingReservations);
        }

        reservationRepository.save(reservation);
        emailService.SendSuccessfulRequestMail(creatorUser.getEmail(), "Room Reservation Request Taken", "Your reservation request " +
                "between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is successfully created!");

        String notificationSubject = "New Room Reservation Request";
        if (reservation.getUrgency()) {
            notificationSubject += " (URGENT!)";
        }
        emailService.SendNotificationToReservationOfficerMail(userRepository.findAnyReservationOfficerEmail(), notificationSubject, creatorUser.getName() + " " + creatorUser.getSurname() +
                " has made a new reservation request between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName());

        return result;
    }

    @Override
    public String saveResubmit(Long room_id, Reservation reservation) {
        String result = "success";
        Reservation_Status reservation_status = Reservation_Status.PENDING;
        List<Reservation> conflictingApprovedReservations = reservationRepository.findConflictingApprovedReservationsInRoom(room_id, reservation.getStart_time(), reservation.getEnd_time());
        List<Reservation> conflictingPendingReservations = reservationRepository.findConflictingPendingReservationsInRoom(room_id, reservation.getStart_time(), reservation.getEnd_time());
        if (reservation.getUrgency().equals(false) &&
                !conflictingApprovedReservations.isEmpty()) {
            //TODO: return an error message to the user.
            result = "conflict";
            return result;
        }
        if (reservation.getUrgency().equals(true) &&
                !conflictingApprovedReservations.isEmpty()) { //&& there is an approved reservation in that time range.
            //Also set the conflicting reservations' status to conflicting if they were pending before.
            reservation_status = Reservation_Status.CONFLICTING;
            result = "conflictpending";
            //TODO: Set Reservation_Status.CONFLICTING and make pending reservations page select also conflicting reservation status. When status is conflicting let it behave like a pending reservation (Display it as pending).            result = "pending";
            //TODO: Send an e-mail to an admin.
        }
        if (!conflictingPendingReservations.isEmpty()) {
            reservation_status = Reservation_Status.CONFLICTING;
            for (Reservation conflictingPendingReservation : conflictingPendingReservations) {
                if (!conflictingPendingReservation.getReservation_status().equals(Reservation_Status.CONFLICTING)) {
                    conflictingPendingReservation.setReservation_status(Reservation_Status.CONFLICTING);
                    reservationRepository.save(conflictingPendingReservation);
                }
            }
        }
        reservation.setReservation_status(reservation_status);
        reservationRepository.save(reservation);
        User creatorUser = userRepository.findByUserName(reservation.getCreator_user());

        emailService.SendSuccessfulRequestMail(creatorUser.getEmail(), "Room Reservation Request Taken", "Your reservation request " +
                "between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is Successfully created!");

        String notificationSubject = "New Room Reservation Request";
        if (reservation.getUrgency()) {
            notificationSubject += " (URGENT!)";
        }

        emailService.SendNotificationToReservationOfficerMail(userRepository.findAnyReservationOfficerEmail(), notificationSubject, creatorUser.getName() + " " + creatorUser.getSurname() +
                " has made a new reservation request between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName());
        return result;
    }


    @Override
    public Reservation saveApproved(Reservation reservation) {
        Long room_id = reservation.getRoom().getId();
        Reservation savedReservation = reservationRepository.save(reservation);

        List<Reservation> conflictingReservations = reservationRepository.findOtherConflictingReservationsInRoom(reservation.getId(), room_id, reservation.getStart_time(), reservation.getEnd_time());
        if (!conflictingReservations.isEmpty()) {
            for (Reservation conflictingReservation : conflictingReservations) {
                conflictingReservation.setReservation_status(Reservation_Status.DECLINED);
                reservationRepository.save(conflictingReservation);
            }
            for (Reservation conflictingReservation : conflictingReservations) {
                List<Reservation> innerConflictingReservations = reservationRepository.findOtherConflictingReservationsInRoom(reservation.getId(), room_id, conflictingReservation.getStart_time(), conflictingReservation.getEnd_time());
                for (Reservation innerConflictingReservation : innerConflictingReservations) {
                    List<Reservation> mostInnerConflictingReservations = reservationRepository.findOtherConflictingReservationsInRoom(innerConflictingReservation.getId(), room_id, innerConflictingReservation.getStart_time(), innerConflictingReservation.getEnd_time());
                    if (mostInnerConflictingReservations.isEmpty()) {
                        innerConflictingReservation.setReservation_status(Reservation_Status.PENDING);
                        reservationRepository.save(innerConflictingReservation);
                    }
                }
            }
        }
        //Send mail to the creator user and the participants.

        emailService.SendApprovedReservationMail(userRepository.findByUserName(reservation.getCreator_user()).getEmail(), "Room Reservation Approved", "Your reservation request " +
                "between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is Approved.");

        List<String> emailOfParticipants = reservationRepository.findEmailsOfParticipants(reservation.getId(), reservation.getCreator_user());
        User CreatorUser = userRepository.findByUserName(reservation.getCreator_user());
        if (!emailOfParticipants.isEmpty()) {
            for (String emailOfParticipant : emailOfParticipants) {
                emailService.SendApprovedReservationMail(emailOfParticipant, "Room Reservation Approved", "The reservation request which involves you created by " + CreatorUser.getName() + " " + CreatorUser.getSurname() +
                        " between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is Approved.");
            }
        }
        return savedReservation;
    }

    @Override
    public Reservation saveEdit(Reservation reservation) {
        Long room_id = reservation.getRoom().getId();
        Reservation savedReservation = reservationRepository.save(reservation);

        List<Reservation> conflictingReservations = reservationRepository.findOtherConflictingReservationsInRoom(reservation.getId(), room_id, reservation.getStart_time(), reservation.getEnd_time());
        if (!conflictingReservations.isEmpty()) {
            for (Reservation conflictingReservation : conflictingReservations) {
                List<Reservation> innerConflictingReservations = reservationRepository.findOtherConflictingReservationsInRoom(conflictingReservation.getId(), room_id, conflictingReservation.getStart_time(), conflictingReservation.getEnd_time());
                if (innerConflictingReservations.isEmpty()) {
                    conflictingReservation.setReservation_status(Reservation_Status.PENDING);
                    reservationRepository.save(conflictingReservation);
                }
            }
        }
        return savedReservation;
    }


     @Override
     public Reservation saveFinished(Reservation reservation) {
         Long room_id = reservation.getRoom().getId();
         Reservation savedReservation = reservationRepository.save(reservation);
         return savedReservation;
     }

    @Override
    public Reservation findById(Long reservation_id) {
        return reservationRepository.findById(reservation_id).get();
    }


    @Override
    public List<ReservationListDto> findConflictingReservationsInRoom(Long room_id, Date start, Date end) {
        return reservationRepository.findConflictingReservationsInRoom(room_id, start, end);
    }


    @Override
    public List<String> findEmailsOfParticipants(Long reservation_id, String creatorName) {
        return reservationRepository.findEmailsOfParticipants(reservation_id, creatorName);
    }
}
