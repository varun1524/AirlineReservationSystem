package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.sjsu.cmpe275.lab2.view.FlightView;
import edu.sjsu.cmpe275.lab2.view.PassengerView;
import edu.sjsu.cmpe275.lab2.view.ReservationView;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "planeId")
@Entity
@Table(name = "plane")
public class Plane {

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "plane_id", nullable = false)
    private int planeId;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @NotNull
    private int capacity;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @NotNull
    private String model;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @NotNull
    private String manufacturer;

    @JsonView({PassengerView.summary.class, ReservationView.summary.class, FlightView.summary.class})
    @NotNull
    private int year;

    public int getPlaneId() {
        return planeId;
    }

    public void setPlaneId(int planeId) {
        this.planeId = planeId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Transient
    @JsonIgnore
    public JSONObject getPlaneJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("planeId", this.getPlaneId());
        jsonObject.put("model", this.getModel());
        jsonObject.put("manufacturer", this.getManufacturer());
        jsonObject.put("year", this.getYear());
        return jsonObject;
    }
}

