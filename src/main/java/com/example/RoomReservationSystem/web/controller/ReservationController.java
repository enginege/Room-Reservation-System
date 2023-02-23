package com.example.RoomReservationSystem.web.controller;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import com.example.RoomReservationSystem.helper.Compare;
import com.example.RoomReservationSystem.model.Reservation;
import com.example.RoomReservationSystem.model.Room;
import com.example.RoomReservationSystem.repository.user_repository.UserRepository;
import com.example.RoomReservationSystem.service.EmailService;
import com.example.RoomReservationSystem.service.category_service.CategoryService;
import com.example.RoomReservationSystem.service.equipment_service.EquipmentService;
import com.example.RoomReservationSystem.service.reservation_service.ReservationService;
import com.example.RoomReservationSystem.service.room_service.RoomService;
import com.example.RoomReservationSystem.service.user_service.UserService;
import com.example.RoomReservationSystem.web.dto.ReservationDto;
import com.example.RoomReservationSystem.web.dto.ReservationJsonDto;
import com.example.RoomReservationSystem.web.dto.ReservationListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    EmailService emailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReservationService reservationService;
    @Autowired
    RoomService roomService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;


    @ResponseBody
    @GetMapping("/overview_json_feed")
    public List<ReservationJsonDto> OverviewCalendarJson(Authentication authentication) {
        String username = authentication.getName();
        return reservationService.findAllReservationsInCompany(username);
    }

    @ResponseBody
    @GetMapping("/room_reservation_json_feed/{room_id}")
    public List<ReservationJsonDto> RoomReservationCalendarJson(Authentication authentication, @PathVariable("room_id") Long room_id) {
        String username = authentication.getName();
        return reservationService.findAllReservationsInCompanyInRoom(username, room_id);
    }

    @ResponseBody
    @GetMapping("/test")
    public List<Reservation> ReservationJson() {
        return reservationService.findAll();
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @GetMapping("/calendar")
    public String Calender(){
        return "overview_calendar";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @GetMapping("/my")
    public String myReservations( Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("reservations", reservationService.findReservationsInCompanyWithoutStatusWithUsername(username));
        return "reservation_my";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE')")
    @GetMapping("/pending")
    public String PendingReservationsOfCompany(Authentication authentication, HttpServletRequest req, Model model) {
        List<ReservationListDto> conflictingReservations = (List<ReservationListDto>) req.getSession().getAttribute("ConflictingReservations");
        req.getSession().removeAttribute("ConflictingReservations"); //Important, you don't want to keep useless objects in your session
        if (conflictingReservations != null){
            model.addAttribute("ConflictingReservations", conflictingReservations);
        }

        String username = authentication.getName();
        model.addAttribute("reservations", reservationService.findReservationsInCompanyWithStatus(username, Reservation_Status.PENDING));
        //TODO: Add conflicting reservations to model to display them in a popup when pressed on the conflicting reservations.
        return "reservation_pending";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'DELETE', 'MANAGE')")
    @PostMapping("/delete/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> deleteReservation(Authentication authentication, @PathVariable("reservation_id") Long reservation_id, @RequestParam(value = "conflictCount", required = false) Integer conflictCount) {
        Reservation reservation = reservationService.findById(reservation_id);
        reservation.setActive_status(false);
        reservationService.saveEdit(reservation);
        String username = authentication.getName();
        List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithoutStatus(username);
        return  reservations;
    }


    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE')")
    @GetMapping("/history")
    public String ReservationHistoryOfCompany(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("reservations", reservationService.findReservationsInCompanyWithoutStatus(username));
        return "reservation_history";
    }


    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @GetMapping("/rooms")
    public String displayRooms(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("rooms", roomService.findAllRoomsInCompany(username));
        return "room_list";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @GetMapping("/new/{room_id}")
    public String newReservation(HttpServletRequest req, @PathVariable("room_id") Long room_id, Authentication authentication, Model model) {
        Room room = roomService.findByRoomId(room_id);
        if (room == null){
            return "redirect:/reservation/rooms";
        }
        String username = authentication.getName();
        ReservationDto reservationDtoTmp = (ReservationDto) req.getSession().getAttribute("ReservationDto");
        req.getSession().removeAttribute("ReservationDto"); //Important, you don't want to keep useless objects in your session
        model.addAttribute("ReservationDto", new ReservationDto());
        if (reservationDtoTmp != null){
            model.addAttribute("ReservationDto", reservationDtoTmp);
        }
        model.addAttribute("CategoryNames", categoryService.findAllCategoryNames());
        model.addAttribute("EquipmentNames", equipmentService.findAllEquipmentNames());
        model.addAttribute("room_id", room_id);
        model.addAttribute("users", userService.findAllOtherUsersOrderedByNameInCompany(username));
        return "reservation_add";
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @PostMapping("new/add/{room_id}")
    public String addReservation(HttpSession session, @PathVariable("room_id") Long room_id,
                                 @ModelAttribute("ReservationDto") ReservationDto reservationDto,
                                 Authentication authentication,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) throws ParseException {


        Date currentDate = new Date();
        String username = authentication.getName();


        //TODO: Check for the empty or wrongly written fields.
        if (reservationDto.getName().equals("")) {
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_NullSubject", "Subject section must not be left empty!");
            return "redirect:/reservation/new/{room_id}";
        }
        if (reservationDto.getNotes().length() > 100){ //Check if the notes section is not more than 100 characters.
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_maxCharNote", "Notes section must not be more than 100 characters!");
            return "redirect:/reservation/new/{room_id}";
        }

        if(Compare.compareDates(reservationDto.getStart(), reservationDto.getEnd()).equals("null")) { //Check if start or end date is empty.
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_NullDate", "The start or end date of the reservation must not be left blank!");
            return "redirect:/reservation/new/{room_id}";
        }
        if (Compare.compareDates(currentDate, reservationDto.getStart()).equals("false")) { //Check if the start of the reservation is after the current date and time.
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_CurrentDate", "The start date of the reservation must be after the current date and time!");
            return "redirect:/reservation/new/{room_id}";
        }
        if(Compare.compareDates(reservationDto.getStart(), reservationDto.getEnd()).equals("false")) { //Check if the end time is greater than start time.
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_Date", "The start date of the reservation must be before the end date!");
            return "redirect:/reservation/new/{room_id}";
        }
        if(Compare.compareDates(reservationDto.getStart(), reservationDto.getEnd()).equals("equal")) { //Check if the end time is equal to the start time.
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_EqualDate", "The start and end date of the reservation must not be equal!");
            return "redirect:/reservation/new/{room_id}";
        }
        //TODO: Maybe pass the reservation automatically if urgency is selected, and set it as pending if not.(Rather than directly declining!!!)
        if (Compare.subtractDates(reservationDto.getStart(), reservationDto.getEnd()) >= 4L && reservationDto.getUrgency().equals(false)) { //Check if the reservation is equal or more than 4 hours.
            //TODO: return an error message to the user.
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_ExceededTime", "The reservation must not be longer than 4 hours unless it is urgent!");
            return "redirect:/reservation/new/{room_id}";
        }

        String result = reservationService.save(room_id, username, reservationDto);

        if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("success", "Your reservation request is successfully created!");
            return "redirect:/reservation/rooms";
        }
        else if(result.equals("conflict")) {
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_Conflict", "Your reservation conflicts with another reservation; therefore, it has been declined!");
            return "redirect:/reservation/new/{room_id}";
        }
        else if(result.equals("conflictpending")) {
            redirectAttributes.addFlashAttribute("success_PendingUrgency", "Your reservation conflicts with another reservation, but it has been put on hold due to urgency!");
            return "redirect:/reservation/rooms";
        }
        else if (result.contains("exceededCapacity")) {
            session.setAttribute("ReservationDto", reservationDto);
            String[] arrOfStr = result.split("#");
            redirectAttributes.addFlashAttribute("error_ExceededCapacity", "Your reservation must not contain more than " + arrOfStr[1] + " participants!");
            return "redirect:/reservation/new/{room_id}";
        }
        else {
            session.setAttribute("ReservationDto", reservationDto);
            redirectAttributes.addFlashAttribute("error_ExceededEquipment", "Equipments: \"" + result + "\" are not available between the selected time range!");
            return "redirect:/reservation/new/{room_id}";
        }

    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'DELETE', 'MANAGE')")
    @PostMapping("/approve/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> ApproveReservation(Authentication authentication, @PathVariable("reservation_id") Long reservation_id) {
        //TODO:Check if the reservation date time is after the current date time!
        Reservation reservation = reservationService.findById(reservation_id);
        Reservation_Status reservationStatus = Reservation_Status.APPROVED;
        reservation.setReservation_status(reservationStatus);
        reservationService.saveApproved(reservation);
        String username = authentication.getName();
        List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithStatus(username, Reservation_Status.PENDING);
        return reservations;
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'DELETE', 'MANAGE')")
    @PostMapping("/decline/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> DeclineReservation(Authentication authentication, @PathVariable("reservation_id") Long reservation_id){
        Reservation reservation = reservationService.findById(reservation_id);
        Reservation_Status reservationStatus = Reservation_Status.DECLINED;
        reservation.setReservation_status(reservationStatus);
        reservationService.saveEdit(reservation);
        //Send email to creator.
        emailService.SendApprovedReservationMail(userRepository.findByUserName(reservation.getCreator_user()).getEmail(), "Room Reservation Declined", "Your reservation request " +
                "between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is Declined!");
        String username = authentication.getName();
        List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithStatus(username, Reservation_Status.PENDING);
        return reservations;
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @PostMapping("/revoke/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> RevokeReservation(Authentication authentication, @PathVariable("reservation_id") Long reservation_id){
        Reservation reservation = reservationService.findById(reservation_id);
        String username = authentication.getName();
        if (!(reservation.getReservation_status().equals(Reservation_Status.APPROVED) || reservation.getReservation_status().equals(Reservation_Status.PENDING) || reservation.getReservation_status().equals(Reservation_Status.CONFLICTING))) {
            List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithoutStatusWithUsername(username);
            return reservations;
        }
        Reservation_Status reservationStatus = Reservation_Status.REVOKED;
        reservation.setReservation_status(reservationStatus);
        reservationService.saveEdit(reservation);
        //send email to creator.
        emailService.SendApprovedReservationMail(userRepository.findByUserName(reservation.getCreator_user()).getEmail(), "Room Reservation Revoked", "Your reservation request " +
                "between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is Revoked!");


        List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithoutStatusWithUsername(username);
        return reservations;
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @PostMapping("/finish/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> FinishReservation(Authentication authentication, @PathVariable("reservation_id") Long reservation_id){
        Reservation reservation = reservationService.findById(reservation_id);
        String username = authentication.getName();
        if (!reservation.getReservation_status().equals(Reservation_Status.APPROVED)){
            List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithoutStatusWithUsername(username);
            return reservations;
        }
        Reservation_Status reservationStatus = Reservation_Status.FINISHED;
        reservation.setReservation_status(reservationStatus);
        reservationService.saveFinished(reservation);
        //send email to creator.
        emailService.SendApprovedReservationMail(userRepository.findByUserName(reservation.getCreator_user()).getEmail(), "Room Reservation Finished", "Your reservation " +
                "between " + reservation.getStart_time().toString().substring(0,16) + " and " + reservation.getEnd_time().toString().substring(0,16) + " for Room: " + reservation.getRoom().getName() + " is labeled as Finished!");


        List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithoutStatusWithUsername(username);
        return reservations;
    }

    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @PostMapping("/resubmit/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> ResubmitReservation(Authentication authentication, @PathVariable("reservation_id") Long reservation_id, RedirectAttributes redirectAttributes) throws ParseException {
        Reservation reservation = reservationService.findById(reservation_id);

        Date currentDate = new Date();

        if (Compare.compareDates(currentDate, reservation.getStart_time()).equals("false")) { //Check if the start of the reservation is after the current date and time.
            String username = authentication.getName();
            List<ReservationListDto> reservations = new ArrayList<>();
            ReservationListDto a = new ReservationListDto();
            a.setName("error_CurrentDate");
            reservations.add(a);
            return reservations;
        }
        String result = reservationService.saveResubmit(reservation.getRoom().getId(), reservation);
        if(result.equals("conflict")) {
            List<ReservationListDto> reservations = new ArrayList<>();
            ReservationListDto a = new ReservationListDto();
            a.setName("error_Conflict");
            reservations.add(a);
            return reservations;
        }
        String username = authentication.getName();
        List<ReservationListDto> reservations = reservationService.findReservationsInCompanyWithoutStatusWithUsername(username);
        return reservations;
    }


    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'DELETE', 'MANAGE', 'READ')")
    @PostMapping("/conflicts/{reservation_id}")
    @ResponseBody
    public List<ReservationListDto> conflictingReservations(HttpSession session, @PathVariable("reservation_id") Long reservation_id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.findById(reservation_id);
        List<ReservationListDto> conflictingReservations = reservationService.findConflictingReservationsInRoom(reservation.getRoom().getId(), reservation.getStart_time(), reservation.getEnd_time());
        session.setAttribute("ConflictingReservations", conflictingReservations);
        return conflictingReservations;
    }

}
