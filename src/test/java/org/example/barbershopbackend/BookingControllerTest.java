package org.example.barbershopbackend;

import org.example.barbershopbackend.controller.BookingController;
import org.example.barbershopbackend.model.Booking;
import org.example.barbershopbackend.model.ServiceType;
import org.example.barbershopbackend.model.User;
import org.example.barbershopbackend.service.BookingService;
import org.example.barbershopbackend.service.TimeSlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private TimeSlotService timeSlotService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void getUserBookingsSuccess() throws Exception {
        User user = new User("Alice", "12345678", "alice@example.com", "pass");
        Booking booking = new Booking(user, LocalDateTime.of(2025,12,31,12,30), ServiceType.HAARKLIP, "Mujji");
        when(bookingService.getUserBookings(1L)).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].barber").value("Mujji"))
                .andExpect(jsonPath("$[0].service").value("HAARKLIP"));

        verify(bookingService, times(1)).getUserBookings(1L);
    }
}
