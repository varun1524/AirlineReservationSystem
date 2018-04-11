package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import edu.sjsu.cmpe275.lab2.view.FlightView;
import edu.sjsu.cmpe275.lab2.view.PassengerView;
import edu.sjsu.cmpe275.lab2.view.ReservationView;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "planeId")
/**
 * @author varunshah
 *
 * Entity Class
 */
@Embeddable
@Table(name = "plane")
public class Plane {

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
        jsonObject.put("model", this.getModel());
        jsonObject.put("capacity", this.getCapacity());
        jsonObject.put("manufacturer", this.getManufacturer());
        jsonObject.put("year", this.getYear());
        return jsonObject;
    }
}

