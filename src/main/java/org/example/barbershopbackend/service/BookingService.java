package org.example.barbershopbackend.service;

import org.springframework.transaction.annotation.Transactional;
import org.example.barbershopbackend.model.*;
import org.example.barbershopbackend.repository.BookingRepository;
import org.example.barbershopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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


    public List<BookingDTO> getAllBookings() {
        return bookingRepo.findAll().stream()
                .map(b -> new BookingDTO(
                        b.getBookingId(),
                        b.getUser().getName(),
                        b.getService().name(),
                        b.getBarber(),
                        b.getAppointmentTime(),
                        b.getStatus()
                ))
                .toList();
    }

    public void cancelBooking(Long bookingId) throws Exception {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));

        bookingRepo.delete(booking);
    }



}
