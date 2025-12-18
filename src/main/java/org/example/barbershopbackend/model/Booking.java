package org.example.barbershopbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private ServiceType service;

    private String status;

    public Booking() {}

    public Booking(User user, LocalDateTime appointmentTime, ServiceType service) {
        this.user = user;
        this.appointmentTime = appointmentTime;
        this.service = service;
        this.status = "BOOKED";
    }

    public Long getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public ServiceType getService() {
        return service;
    }

    public String getStatus() {
        return status;
    }

}
