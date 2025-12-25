package org.example.barbershopbackend.service;

import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.model.BookingStatus;
import org.example.barbershopbackend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    private final BookingRepository bookingRepo;

    public TimeSlotService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public List<String> generateAllSlots() {
        List<String> slots = new ArrayList<>();
        int startHour = 11;
        int endHour = 19;

        for (int hour = startHour; hour < endHour; hour++) {
            slots.add(String.format("%02d:00", hour));
            slots.add(String.format("%02d:30", hour));
        }

        return slots;
    }

    public List<String> getAvailableSlots(String barber) {
        List<String> allSlots = generateAllSlots();

        List<Booking> booked = bookingRepo
                .findByStatusAndBarber(BookingStatus.BOOKED, barber);

        List<String> bookedTimes = booked.stream()
                .map(b -> b.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toList());

        allSlots.removeAll(bookedTimes);

        return allSlots;
    }
}
