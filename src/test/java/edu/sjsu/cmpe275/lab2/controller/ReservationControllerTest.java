package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReservationControllerTest {
    static String reservationId = "ab756195-4514-4419-ba12-d17ab6f4393e";
    static String passengerId = "26cc83af-9443-4ec7-8913-945d3f983f0d";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Testing of Make Reservation Positive
     * @throws UnirestException
     */
    @Test
    public void stage1_makeReservation() throws UnirestException {
        String url = "http://localhost:8080/reservation?passengerId="+ passengerId +"&flightLists=108";
        HttpResponse<String> jsonResponse = Unirest.post(url).asObject(String.class);
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        if(jsonObject.has("reservation")){
            reservationId = jsonObject.getJSONObject("reservation").getString("reservationNumber");
        }
        Assert.assertEquals(HttpStatus.OK.value(),jsonResponse.getStatus());
    }

    /**
     * Testing of Make Reservation Negative
     * @throws UnirestException
     */
    @Test
    public void stage2_makeReservation_neg() throws UnirestException {
        String url = "http://localhost:8080/reservation?passengerId="+passengerId+"&flightLists=108";
        HttpResponse<String> jsonResponse = Unirest.post(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(),jsonResponse.getStatus());
    }

    /**
     * Testing of fetch Reservation By ID Positive
     * @throws UnirestException
     */
    @Test
    public void stage3_fetchReservationById_pos() throws UnirestException {
        HttpResponse<String> jsonResponse = Unirest.get("http://localhost:8080/reservation/"+reservationId).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(), jsonResponse.getStatus());
    }

    /**
     * Testing of searching reservation positive
     * @throws UnirestException
     */
    @Test
    public void stage4_searchForReservation_pos() throws UnirestException {
        String url = "http://localhost:8080/reservation?passengerId="+ passengerId +"&origin=Houston&to=San%20Jose&flightNumber=108";
        HttpResponse<String> jsonResponse = Unirest.get(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(), jsonResponse.getStatus());
    }

    /**
     * Testing of Deleting Reservation Positive
     * @throws UnirestException
     */
    @Test
    public void stage5_deleteReservation_pos() throws UnirestException {
        HttpResponse jsonResponse = Unirest.delete("http://localhost:8080/reservation/"+reservationId).asString();
        int status = jsonResponse.getStatus();
        System.out.println(status);
        Assert.assertEquals(HttpStatus.OK.value(), status);
    }

    /**
     * Testing of Searching Reservation Negative
     * @throws UnirestException
     */
    @Test
    public void stage6_searchForReservation_neg() throws UnirestException {
        String url = "http://localhost:8080/reservation?passengerId="+ passengerId +"&origin=Houston&to=San%20Jose&flightNumber=108";
        HttpResponse<String> jsonResponse = Unirest.get(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), jsonResponse.getStatus());

    }

    /**
     * Testing of fetch Reservation By ID Negative
     * @throws UnirestException
     */
    @Test
    public void stage7_fetchReservationById_neg() throws UnirestException {
        HttpResponse<String> jsonResponse = Unirest.get("http://localhost:8080/reservation/"+reservationId).asObject(String.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), jsonResponse.getStatus());
    }

    /**
     * Testing of Deleting Reservation Negative
     * @throws UnirestException
     */
    @Test
    public void stage8_deleteReservation_neg() throws UnirestException {
        HttpResponse jsonResponse = Unirest.delete("http://localhost:8080/reservation/"+reservationId).asString();
        int status = jsonResponse.getStatus();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }
}