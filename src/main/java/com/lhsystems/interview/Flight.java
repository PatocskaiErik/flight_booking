package com.lhsystems.interview;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;
    private List<Booking> bookings;

    public Flight(String flightNumber, String origin, String destination, LocalDateTime departureTime, int availableSeats, List<Booking> bookings) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.bookings = bookings;
    }

    public Booking bookSeat(int seatNumber, Passenger passenger) throws BookingException {
        if (availableSeats <= 0) {
            throw new BookingException("No available seats on the flight.");
        }

        for (Booking booking : bookings) {
            if (booking.getSeatNumber() == seatNumber) {
                throw new BookingException("Seat " + seatNumber + " is already booked.");
            }
        }

        Booking newBooking = new Booking(this, seatNumber, passenger);
        bookings.add(newBooking);
        availableSeats--;

        return newBooking;
    }

    public void cancelBooking(Booking booking) {
        if (booking.getFlight() != this) {
            throw new IllegalArgumentException("Invalid booking for this flight.");
        }

        bookings.remove(booking);
        availableSeats++;

    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        return new EqualsBuilder().append(availableSeats, flight.availableSeats).append(flightNumber, flight.flightNumber).append(origin, flight.origin).append(destination, flight.destination).append(departureTime, flight.departureTime).append(bookings, flight.bookings).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(flightNumber).append(origin).append(destination).append(departureTime).append(availableSeats).append(bookings).toHashCode();
    }
}
