package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reservationNumber")
//@JsonIgnoreProperties(allowSetters = true, value = { "passenger" })
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "reservation_number", nullable = false)
    private int reservationNumber;

    private double price; // sum of each flight’s price.

//    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "flight_id", nullable = false)
    private List<Flight> flights;

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
