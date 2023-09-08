package com.lhsystems.interview;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingSystem {

    private final List<Flight> flights;

    public BookingSystem(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Flight> findAvailableFlights(String origin, String destination, LocalDateTime departureTime) {
        List<Flight> availableFlights = new ArrayList<>();

        for (Flight flight : flights) {
            if (flight.getOrigin().equals(origin)
                    && flight.getDestination().equals(destination)
                    && flight.getDepartureTime().compareTo(departureTime) >= 0
                    && flight.getAvailableSeats() > 0) {
                availableFlights.add(flight);
            }
        }

        return availableFlights;
    }

    public Booking bookFlight(Flight flight, int seatNumber, Passenger passenger) throws BookingException {
        flight.bookSeat(seatNumber, passenger);
        Booking newBooking = new Booking(flight, seatNumber, passenger);
        return newBooking;
    }

    public void cancelBooking(Booking booking) {
        Flight flight = booking.getFlight();
        flight.cancelBooking(booking);

    }

    public float getBookedPercentageForFlight(String flightNumber) {

        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                int totalSeats = flight.getBookings().size() + flight.getAvailableSeats();
                if (totalSeats > 0) {
                    float bookedPercentage = (float) flight.getBookings().size() / totalSeats * 100;
                    return bookedPercentage;
                }
            }
        }
        return 0;
    }

    public List<Booking> getBookingsForPassenger(String email) {

        List<Booking> passengerBookings = new ArrayList<>();
        for (Flight flight : flights) {
            for (Booking booking : flight.getBookings()) {
                if (booking.getPassenger().getEmail().equals(email)) {
                    passengerBookings.add(booking);
                }
            }
        }
        return passengerBookings;
    }

    public Passenger findMostBookedPassenger() {

        Map<Passenger, Integer> passengerBookingsCount = new HashMap<>();

        for (Flight flight : flights) {
            for (Booking booking : flight.getBookings()) {
                Passenger passenger = booking.getPassenger();
                passengerBookingsCount.put(passenger, passengerBookingsCount.getOrDefault(passenger, 0) + 1);
            }
        }

        Passenger mostBookedPassenger = null;
        int maxBookings = 0;
        for (Map.Entry<Passenger, Integer> entry : passengerBookingsCount.entrySet()) {
            if (entry.getValue() > maxBookings) {
                mostBookedPassenger = entry.getKey();
                maxBookings = entry.getValue();
            }
        }

        return mostBookedPassenger;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}
