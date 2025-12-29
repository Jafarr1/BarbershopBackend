package org.example.barbershopbackend.model;

import java.time.LocalDateTime;

public record BookingDTO(
        Long bookingId,
        String userName,
        String service,
        String barber,
        LocalDateTime appointmentTime,
        BookingStatus status
) {}
