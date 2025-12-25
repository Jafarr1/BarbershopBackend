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

    private String barber;

    @Enumerated(EnumType.STRING)
    private ServiceType service;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking() {}

    public Booking(User user, LocalDateTime appointmentTime, ServiceType service, String barber) {
        this.user = user;
        this.appointmentTime = appointmentTime;
        this.service = service;
        this.barber = barber;
        this.status = BookingStatus.BOOKED;

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

    public String getBarber() {
        return barber;
    }

    public ServiceType getService() {
        return service;
    }

    public BookingStatus getStatus() {
        return status;
    }

}
