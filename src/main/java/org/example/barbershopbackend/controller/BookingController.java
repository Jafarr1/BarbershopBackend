package org.example.barbershopbackend.controller;

import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestParam Long userId,
                                 @RequestParam String time,
                                 @RequestParam String service) throws Exception {

        LocalDateTime dateTime = LocalDateTime.parse(time);
        return bookingService.createBooking(userId, dateTime, service);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }
}
