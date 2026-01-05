package org.example.barbershopbackend.controller;

import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.model.BookingDTO;
import org.example.barbershopbackend.model.BookingStatus;
import org.example.barbershopbackend.security.JwtUtil;
import org.example.barbershopbackend.service.BookingService;
import org.example.barbershopbackend.service.TimeSlotService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TimeSlotService timeSlotService;

    public BookingController(BookingService bookingService, TimeSlotService timeSlotService) {
        this.bookingService = bookingService;
        this.timeSlotService = timeSlotService;
    }

    @PostMapping("/create")
    public Booking createBooking(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestParam String time,
            @RequestParam String service,
            @RequestParam String barber
    ) throws Exception {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }

        Long userId = JwtUtil
                .validateToken(token)
                .getBody()
                .get("userId", Long.class);

        LocalDateTime dateTime = LocalDateTime.parse(time);
        return bookingService.createBooking(userId, dateTime, service, barber);
    }

    @GetMapping("/user")
    public List<Booking> getMyBookings(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }

        Long userId = JwtUtil
                .validateToken(token)
                .getBody()
                .get("userId", Long.class);

        return bookingService.getUserBookings(userId);
    }

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(
            @RequestParam String barber,
            @RequestParam String date
    ) {
        LocalDate bookingDate = LocalDate.parse(date);
        return timeSlotService.getAvailableSlots(barber, bookingDate);
    }

    @GetMapping
    public List<BookingDTO> getAllBookings(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }

        String role = JwtUtil.validateToken(token).getBody().get("role", String.class);

        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admins only");
        }

        return bookingService.getAllBookings();
    }

    @PostMapping("/{id}/cancel")
    public void cancelBooking(
            @PathVariable Long id,
            @CookieValue(name = "jwt", required = false) String token
    ) throws Exception {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }

        String role = JwtUtil.validateToken(token).getBody().get("role", String.class);

        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admins only");
        }

        bookingService.cancelBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public void cancelBookingByUser(
            @PathVariable Long id,
            @CookieValue(name = "jwt", required = false) String token
    ) throws Exception {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }

        Long userId = JwtUtil.validateToken(token)
                .getBody()
                .get("userId", Long.class);

        Booking booking = bookingService.getBookingById(id);

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You can only cancel your own bookings");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingService.updateBooking(booking);
    }

}
