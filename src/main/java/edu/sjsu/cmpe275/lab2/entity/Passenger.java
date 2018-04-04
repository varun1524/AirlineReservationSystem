package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "passengerId")
@Entity
public class Passenger {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    private int age;

    private String gender;

    @Column(unique = true)
    private String phone; // Phone numbers must be unique

//    @JsonManagedReference
    @OneToMany(mappedBy="passenger")
    private List<Reservation> reservations;

    /*@JsonIgnore
    @ManyToMany(mappedBy = "flights")
    private List<Flight> passengers;

    public List<Flight> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Flight> passengers) {
        this.passengers = passengers;
    }*/

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

    public JSONObject getPassengerJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passengerId", this.getPassengerId());
        jsonObject.put("firstname", this.getFirstname());
        jsonObject.put("lastname", this.getLastname());
        jsonObject.put("age", this.getAge());
        jsonObject.put("gender", this.getGender());
        jsonObject.put("passengerId", this.getPhone());
        return jsonObject;
    }

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
