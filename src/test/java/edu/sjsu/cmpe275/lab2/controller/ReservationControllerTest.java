package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ReservationControllerTest {
    @Autowired
    private MockMvc mockmvc;

    @MockBean
    @Autowired
    private FlightService flightservice;

    @MockBean
    private FlightRepository flightrepository;


    @Before
    public void setUp() throws Exception {
        this.mockmvc = standaloneSetup(new FlightController()).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void makeReservation() throws UnirestException {
        String url = "http://localhost:8888/reservation?passengerId=aecb25dc-5d7f-49de-ba26-41eb3badc9c3&flightLists=121";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    @Test
    //Positive test, if reservation exists
    public void searchForReservation_pos() throws UnirestException {
        String url = "http://localhost:8888/reservation?passengerId=c4ff53b3-cf1b-46c7-b19d-10ede957dd92&origin=San%20Jose&to=Houston&flightNumber=121";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());

    }

    @Test
    //Negative test, if reservation does not exists
    public void searchForReservation_neg() throws UnirestException {
        String url = "http://localhost:8888/reservation?passengerId=c4ff53b32&origin=Mars&to=Houston&flightNumber=121";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());

    }

    @Test
    // Positive test, if reservation exists
    public void fetchReservationById_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/reservation/c4ff53b3-cf1b-46c7-b19d-10ede957dd92").asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    @Test
    // Positive test, if reservation exists
    public void fetchReservationById_neg() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/reservation/f1b-46c7-b19d-10ede957dd92").asObject(String.class);
        Assert.assertEquals(400,jsonresponse.getStatus());
    }

    @Test
    //Positive test, if reservation exists
    public void deleteReservation_pos() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8888/reservation/b09a626d-0e0c-4ada-95a6-7cbfd6c1ec2c").asString();
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,HttpStatus.OK.value());
    }

    @Test
    //Negative test, if reservation exists
    public void deleteReservation_neg() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8888/reservation/a-95a6-7cbfd6c1ec2c").asString();
        int status = response.getStatus();
        Assert.assertEquals(status,400);
    }
}