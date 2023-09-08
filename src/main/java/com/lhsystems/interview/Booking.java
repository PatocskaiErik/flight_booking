package com.lhsystems.interview;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Booking {
    private Flight flight;
    private int seatNumber;
    private Passenger passenger;

    public Booking(Flight flight, int seatNumber, Passenger passenger) {
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        return new EqualsBuilder().append(seatNumber, booking.seatNumber).append(flight, booking.flight).append(passenger, booking.passenger).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(flight).append(seatNumber).append(passenger).toHashCode();
    }

}
