package com.lhsystems.interview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingSystemTest {

    private BookingSystem bookingSystem;

    private void init() {
        List<Flight> flights = new ArrayList<>();

        flights.add(new Flight("LH123", "BUD", "FRA", LocalDateTime.of(2023, Month.FEBRUARY, 5, 10, 0), 2, new ArrayList<>()));

        flights.add(new Flight("LH234", "BUD", "FRA", LocalDateTime.of(2023, Month.FEBRUARY, 6, 10, 0), 123, new ArrayList<>()));

        flights.add(new Flight("LH345", "BUD", "FRA", LocalDateTime.of(2023, Month.FEBRUARY, 7, 10, 0), 123, new ArrayList<>()));

        bookingSystem = new BookingSystem(flights);
    }

    @Test
    public void testFindAvailableFLights() {
        init();
        bookingSystem.getFlights().add(new Flight("LH456", "BER", "FRA", LocalDateTime.of(2023, Month.FEBRUARY, 7, 10, 0), 123, new ArrayList<>()));

        bookingSystem.getFlights().add(new Flight("LH567", "BUD", "BER", LocalDateTime.of(2023, Month.FEBRUARY, 7, 10, 0), 123, new ArrayList<>()));

        bookingSystem.getFlights().add(new Flight("LH678", "BUD", "FRA", LocalDateTime.of(2023, Month.FEBRUARY, 6, 10, 0), 0, new ArrayList<>()));

        List<Flight> flights = bookingSystem.findAvailableFlights("BUD", "FRA", LocalDateTime.of(2023, Month.FEBRUARY, 5, 10, 1));

        assertEquals(2, flights.size());
        assertTrue(flights.stream().anyMatch(flight -> flight.getFlightNumber().equals("LH234")));
        assertTrue(flights.stream().anyMatch(flight -> flight.getFlightNumber().equals("LH345")));
    }

    @Test
    public void testBookFlight() throws BookingException {
        init();
        Booking booking = bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 1, new Passenger("Mr. passenger", "passenger.mister@gmail.com"));

        assertEquals(bookingSystem.getFlights().get(0).getBookings().get(0), booking);
        assertEquals(bookingSystem.getFlights().get(0), booking.getFlight());
        assertEquals("Mr. passenger", booking.getPassenger().getName());
        assertEquals(1, booking.getSeatNumber());
    }

    @Test
    public void testBookFlightSeatTaken() throws BookingException {
        init();
        bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 1, new Passenger("Mr. passenger", "passenger.mister@gmail.com"));
        assertThrows(BookingException.class, () -> bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 1, new Passenger("Mrs. passenger", "passenger.mrs@gmail.com")));
    }

    @Test
    public void testBookFlightNoSeatSeat() throws BookingException {
        init();
        bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 1, new Passenger("Mr. passenger", "passenger.mister@gmail.com"));
        bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 2, new Passenger("Jr. passenger", "passenger.junior@gmail.com"));
        assertThrows(BookingException.class, () -> bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 3, new Passenger("Mrs. passenger", "passenger.mrs@gmail.com")));
    }

    @Test
    public void testCancelBooking() throws BookingException {
        init();
        Booking booking = bookingSystem.bookFlight(bookingSystem.getFlights().get(0), 1, new Passenger("Mr. passenger", "passenger.mister@gmail.com"));
        assertEquals(1, bookingSystem.getFlights().get(0).getBookings().size());
        assertEquals(1, booking.getFlight().getAvailableSeats());
        bookingSystem.cancelBooking(booking);

        assertEquals(2, booking.getFlight().getAvailableSeats());
        assertEquals(0, bookingSystem.getFlights().get(0).getBookings().size());
    }

    @Test
    public void testGetBookingsForFlight() {
        init();
        Passenger p1 = new Passenger("Mr. passenger", "passenger.mister@gmail.com");
        Passenger p2 = new Passenger("Mrs. passenger", "passenger.mrs@gmail.com");
        Passenger p3 = new Passenger("Junior. passenger", "passenger.junior@gmail.com");
        bookingSystem.getFlights().get(0).getBookings().add(new Booking(bookingSystem.getFlights().get(0), 1, p1));
        bookingSystem.getFlights().get(1).getBookings().add(new Booking(bookingSystem.getFlights().get(1), 2, p2));
        bookingSystem.getFlights().get(2).getBookings().add(new Booking(bookingSystem.getFlights().get(2), 3, p2));
        bookingSystem.getFlights().get(2).getBookings().add(new Booking(bookingSystem.getFlights().get(2), 1, p3));

        float result = bookingSystem.getBookedPercentageForFlight("LH345");

        assertEquals(1.6f, result, 0.09f);
    }

    @Test
    public void testGetBookingsForPassenger() {
        init();
        Passenger p1 = new Passenger("Mr. passenger", "passenger.mister@gmail.com");
        Passenger p2 = new Passenger("Mrs. passenger", "passenger.mrs@gmail.com");
        Passenger p3 = new Passenger("Junior. passenger", "passenger.junior@gmail.com");
        bookingSystem.getFlights().get(0).getBookings().add(new Booking(bookingSystem.getFlights().get(0), 1, p1));
        bookingSystem.getFlights().get(1).getBookings().add(new Booking(bookingSystem.getFlights().get(1), 2, p2));
        bookingSystem.getFlights().get(2).getBookings().add(new Booking(bookingSystem.getFlights().get(2), 3, p2));
        bookingSystem.getFlights().get(2).getBookings().add(new Booking(bookingSystem.getFlights().get(2), 1, p3));

        List<Booking> bookings = bookingSystem.getBookingsForPassenger(p2.getEmail());

        assertEquals(2, bookings.size());
        assertEquals(new Booking(bookingSystem.getFlights().get(1), 2, p2), bookings.get(0));
        assertEquals(new Booking(bookingSystem.getFlights().get(2), 3, p2), bookings.get(1));
    }

    @Test
    public void testFindMostBookedPassenger() {
        init();
        Passenger p1 = new Passenger("Mr. passenger", "passenger.mister@gmail.com");
        Passenger p2 = new Passenger("Mrs. passenger", "passenger.mrs@gmail.com");
        Passenger p3 = new Passenger("Junior. passenger", "passenger.junior@gmail.com");
        bookingSystem.getFlights().get(0).getBookings().add(new Booking(bookingSystem.getFlights().get(0), 1, p1));
        bookingSystem.getFlights().get(1).getBookings().add(new Booking(bookingSystem.getFlights().get(1), 2, p2));
        bookingSystem.getFlights().get(2).getBookings().add(new Booking(bookingSystem.getFlights().get(2), 3, p2));
        bookingSystem.getFlights().get(2).getBookings().add(new Booking(bookingSystem.getFlights().get(2), 1, p3));

        Passenger passenger = bookingSystem.findMostBookedPassenger();

        assertEquals(p2, passenger);
    }

}
