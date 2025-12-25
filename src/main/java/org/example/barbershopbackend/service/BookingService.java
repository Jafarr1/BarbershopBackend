package org.example.barbershopbackend.service;

import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.model.BookingStatus;
import org.example.barbershopbackend.model.ServiceType;
import org.example.barbershopbackend.model.User;
import org.example.barbershopbackend.repository.BookingRepository;
import org.example.barbershopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;

    public BookingService(BookingRepository bookingRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    public Booking createBooking(Long userId, LocalDateTime time, String service, String barber) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        ServiceType serviceType;
        try {
            serviceType = ServiceType.valueOf(service.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid service type");
        }

        if (bookingRepo.existsByAppointmentTimeAndStatus(time, BookingStatus.BOOKED)) {
            throw new Exception("Time slot already booked");
        }

        Booking booking = new Booking(user, time, serviceType, barber);
        return bookingRepo.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepo.findByUser_UserId(userId);
    }
}
