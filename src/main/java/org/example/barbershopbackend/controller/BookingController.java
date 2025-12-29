package org.example.barbershopbackend.controller;

import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.model.BookingDTO;
import org.example.barbershopbackend.model.BookingStatus;
import org.example.barbershopbackend.service.BookingService;
import org.example.barbershopbackend.service.TimeSlotService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;
    private final TimeSlotService timeSlotService;

    public BookingController(BookingService bookingService, TimeSlotService timeSlotService) {
        this.bookingService = bookingService;
        this.timeSlotService = timeSlotService;
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestParam Long userId,
                                 @RequestParam String time,
                                 @RequestParam String service,
                                 @RequestParam String barber) throws Exception {

        LocalDateTime dateTime = LocalDateTime.parse(time);
        return bookingService.createBooking(userId, dateTime, service, barber);
    }


    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(@RequestParam String barber) {
        return timeSlotService.getAvailableSlots(barber);
    }

    @GetMapping
    public List<BookingDTO> getAllBookings(
            @RequestHeader(value = "X-ROLE", required = false) String role
    ) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Admins only"
            );
        }
        return bookingService.getAllBookings();
    }


    @PostMapping("/{id}/cancel")
    public void cancelBooking(
            @PathVariable Long id,
            @RequestHeader(value = "X-ROLE", required = false) String role
    ) throws Exception {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admins only");
        }
        bookingService.cancelBooking(id);
    }


}
