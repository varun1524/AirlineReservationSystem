package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.*;
import edu.sjsu.cmpe275.lab2.view.FlightView;
import edu.sjsu.cmpe275.lab2.view.PassengerView;
import edu.sjsu.cmpe275.lab2.view.ReservationView;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "passengerId")
@Entity
public class Passenger {


    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Column(name = "first_name")
    private String firstname;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Column(name = "last_name")
    private String lastname;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    private int age;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    private String gender;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Column(unique = true)
    private String phone; // Phone numbers must be unique

    @JsonView({PassengerView.summary.class})
    @OneToMany(mappedBy="passenger")
    private List<Reservation> reservations;

//    @JsonView({PassengerView.summary.class})
    @JsonIgnore
    @ManyToMany(mappedBy = "passengers")
    private List<Flight> flights;

    public List<Flight> getFlights() {
        return flights;
    }

    public void setPassengers(List<Flight> flights) {
        this.flights = flights;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Transient
    @JsonIgnore
    public JSONObject getPassengerJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passengerId", this.getPassengerId());
        jsonObject.put("firstname", this.getFirstname());
        jsonObject.put("lastname", this.getLastname());
        jsonObject.put("age", this.getAge());
        jsonObject.put("gender", this.getGender());
        jsonObject.put("phone", this.getPhone());
        return jsonObject;
    }

    @Transient
    @JsonIgnore
    public JSONObject getWholePassengerDetailsJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passenger", this.getPassengerJSON());
        JSONObject jsonObjectReservation = new JSONObject();
        jsonObject.getJSONObject("passenger").put("reservations", jsonObjectReservation);
        JSONArray reservationJsonArray = new JSONArray();
        this.getReservations().forEach(reservation -> {
            JSONObject tempJSONObj = reservation.getReservationJSON();
            tempJSONObj.remove("passenger");
            reservationJsonArray.put(tempJSONObj);
        });
        jsonObjectReservation.put("reservation",reservationJsonArray);
        return jsonObject;
    }
}
