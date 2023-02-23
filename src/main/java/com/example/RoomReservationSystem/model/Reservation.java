package com.example.RoomReservationSystem.model;

import com.example.RoomReservationSystem.enums.Reservation_Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "start_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date start_time;
    @Column(name = "end_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date end_time;
    @Column(name = "creator_user")
    private String creator_user;
    @Column(name = "urgency")
    private Boolean urgency;
    @Column(name = "cleaning_service")
    private Boolean cleaning_service;
    @Column(name = "food_service")
    private Boolean food_service;
    @Column(name = "notes")
    private String notes;
    @Column(name = "reservation_status")
    private Reservation_Status reservation_status;
    @Column(name = "active_status")
    private Boolean active_status = true;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reservations_equipments",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id", referencedColumnName = "id"))
    private Set<Equipment> equipments = new HashSet<>();

//    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "reservations", cascade = CascadeType.MERGE)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_reservations",
        joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    public Reservation() {
    }

    public Reservation(Category category, Date start_time, Date end_time, String creator_user, Boolean urgency, Boolean cleaning_service, Boolean food_service, String notes, Reservation_Status reservation_status, Room room) {
        this.category = category;
        this.start_time = start_time;
        this.end_time = end_time;
        this.creator_user = creator_user;
        this.urgency = urgency;
        this.cleaning_service = cleaning_service;
        this.food_service = food_service;
        this.notes = notes;
        this.reservation_status = reservation_status;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getCreator_user() {
        return creator_user;
    }

    public void setCreator_user(String creator_user) {
        this.creator_user = creator_user;
    }

    public Boolean getUrgency() {
        return urgency;
    }

    public void setUrgency(Boolean urgency) {
        this.urgency = urgency;
    }

    public Boolean getCleaning_service() {
        return cleaning_service;
    }

    public void setCleaning_service(Boolean cleaning_service) {
        this.cleaning_service = cleaning_service;
    }

    public Boolean getFood_service() {
        return food_service;
    }

    public void setFood_service(Boolean food_service) {
        this.food_service = food_service;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Reservation_Status getReservation_status() {
        return reservation_status;
    }

    public void setReservation_status(Reservation_Status reservation_status) {
        this.reservation_status = reservation_status;
    }

    public Boolean getActive_status() {
        return active_status;
    }

    public void setActive_status(Boolean active_status) {
        this.active_status = active_status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipments) {
        this.equipments = equipments;
    }
    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        this.equipments.remove(equipment);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}


