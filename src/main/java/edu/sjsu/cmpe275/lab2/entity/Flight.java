package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.*;
import edu.sjsu.cmpe275.lab2.view.FlightView;
import edu.sjsu.cmpe275.lab2.view.PassengerView;
import edu.sjsu.cmpe275.lab2.view.ReservationView;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "flightNumber")
@Entity
@Table(name = "flight")
public class Flight {

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Id
    @Column(name = "flight_number", nullable = false)
    private String flightNumber; // Each flight has a unique flight number.

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    private double price;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    private String source;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    private String destination;

    /*
    ** Date format: yy-mm-dd-hh, do not include minutes and seconds.
    ** Example: 2018-03-22-19
    ** The system only needs to supports PST. You can ignore other time zones.
    */

    //    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @JsonFormat(pattern="yyyy-MM-dd-HH")
    @Column(name = "departure_time")
    @NotNull
    private Date departureTime;

    //  @Temporal(TemporalType.TIMESTAMP)
    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @JsonFormat(pattern="yyyy-MM-dd-HH")
    @Column(name = "arrival_time")
    @NotNull
    private Date arrivalTime;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Column(name = "seatsLeft")
    @NotNull
    private int seatsLeft;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    private String description;


    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "plane_id", nullable = false)
    @Embedded
    private Plane plane;  // Embedded

//    @JsonBackReference
    @JsonIgnore
    @ManyToMany(mappedBy = "flights")
    private List<Reservation> reservations;

    @JsonView({FlightView.summary.class})
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Passenger.class, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "passenger_id", nullable = false)
    private List<Passenger> passengers;

    public Flight(){}

    public Flight(String flightNumber){
        this.flightNumber = flightNumber;
    }


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

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Transient
    @JsonIgnore
    public JSONObject getFlightJSON(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flightNumber", this.getFlightNumber());
        jsonObject.put("price", this.getPrice());
        jsonObject.put("from", this.getSource());
        jsonObject.put("to", this.getDestination());
        jsonObject.put("departureTime", simpleDateFormat.format(this.getDepartureTime()));
        jsonObject.put("arrivalTime", simpleDateFormat.format(this.getArrivalTime()));
        jsonObject.put("seatsLeft", this.getSeatsLeft());
        jsonObject.put("description", this.getDescription());
        jsonObject.put("plane", this.getPlane().getPlaneJSON());
        return jsonObject;
    }

    @Transient
    @JsonIgnore
    public JSONObject getWholeFlightDetailsJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flight", this.getFlightJSON());
        JSONObject passengerJsonObject = new JSONObject();
        jsonObject.getJSONObject("flight").put("passengers", passengerJsonObject);
        JSONArray passengerJsonArray = new JSONArray();
        this.getPassengers().forEach(passenger -> {
            passengerJsonArray.put(passenger.getPassengerJSON());
        });
        passengerJsonObject.put("passenger",passengerJsonArray);
        return jsonObject;
    }
}
