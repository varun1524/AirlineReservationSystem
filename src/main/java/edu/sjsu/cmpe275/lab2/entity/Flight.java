package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "flightNumber")
@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @Column(name = "flight_number", nullable = false)
    private String flightNumber; // Each flight has a unique flight number.

    private double price;

    private String source;

    private String destination;

    /*
    ** Date format: yy-mm-dd-hh, do not include minutes and seconds.
    ** Example: 2018-03-22-19
    ** The system only needs to supports PST. You can ignore other time zones.
    */

    //    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_time")
    @NotNull
    private Date departureTime;

    //  @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrival_time")
    @NotNull
    private Date arrivalTime;

    @Column(name = "seatsLeft")
    @NotNull
    private int seatsLeft;

    private String description;

//    @JsonIgnore
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "plane_id", nullable = false)
    private Plane plane;  // Embedded

    @JsonBackReference
    @ManyToMany(mappedBy = "flights")
    private List<Reservation> reservations;

//    @OneToMany
//    @JoinColumn(name = "passenger_id")
//    private List<Passenger> passengers;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

//    public List<Passenger> getPassengerList() {
//        return passengers;
//    }
//
//    public void setPassengerList(List<Passenger> passengers) {
//        this.passengers = passengers;
//    }
}
