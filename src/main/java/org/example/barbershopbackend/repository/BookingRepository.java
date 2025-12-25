package org.example.barbershopbackend.repository;

import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByAppointmentTimeAndStatus(LocalDateTime appointmentTime, BookingStatus status);

    List<Booking> findByAppointmentTimeAndStatus(LocalDateTime appointmentTime, BookingStatus status);

    List<Booking> findByUser_UserId(Long userId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByStatusAndBarber(BookingStatus status, String barber);

}
