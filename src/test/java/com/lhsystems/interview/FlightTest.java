package com.lhsystems.interview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlightTest {

    private Flight underTest;

    private void init() {
        List<Booking> bookings = new ArrayList<>();
        underTest = new Flight("LH123", "BUD", "FRA", LocalDateTime.of(2023, 1, 1, 13, 0), 100, bookings);
        bookings.add(new Booking(underTest, 1, new Passenger("Mr. Passenger", "passenger.mister@gmail.com")));
    }

    @Test
    public void testBookSeat() {
        init();
        Passenger passenger = new Passenger("Mrs. Passenger", "passenger.mrs@gmail.com");
        try {
            underTest.bookSeat(2, passenger);
        } catch (BookingException be) {

        }
        assertEquals(99, underTest.getAvailableSeats());
    }

    @Test
    public void testBookSeatNoSeatsLeft() {
        init();
        underTest.setAvailableSeats(0);
        Passenger passenger = new Passenger("Mrs. Passenger", "passenger.mrs@gmail.com");
        assertThrows(BookingException.class, () -> underTest.bookSeat(2, passenger));
    }

    @Test
    public void testBookSeatPlaceIsTaken() {
        init();
        Passenger passenger = new Passenger("Mrs. Passenger", "passenger.mrs@gmail.com");
        assertThrows(BookingException.class, () -> underTest.bookSeat(1, passenger));
    }

    @Test
    public void testCancelTicket() {
        init();
        Booking booking = underTest.getBookings().get(0);
        underTest.cancelBooking(booking);
        assertEquals(101, underTest.getAvailableSeats());
        assertTrue(underTest.getBookings().isEmpty());
        assertFalse(underTest.getBookings().contains(booking));
    }


}
