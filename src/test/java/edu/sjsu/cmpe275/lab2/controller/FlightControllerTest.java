package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

/**
 * @author Sannisth Soni
 * Reservation Controller Unit Test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlightControllerTest {

    static int flightId = 0;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    /**
     * Test if flight can be created
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage1_createFlight() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8080/flight/131?price=150&from=Houston&to=San%20Jose&departureTime=2018-05-16-13&arrivalTime=2018-05-16-17&description=Direct%20flight&capacity=50&model=Boeing%20747&manufacturer=airbus&year=1993")
                .header("accept","application/json")
                .queryString("apiKey","131")
                .asJson()
                ;
        Assert.assertEquals(HttpStatus.OK.value(),jsonResponse.getStatus());

    }


    /**
     * Tests existence of flight in database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage2_fetchFlight_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8080/flight/131").asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }


    /**
     * Tests deleting a flight in database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage3_deleteFlight_pos() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8080/flight/131").asString();
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,HttpStatus.OK.value());
    }

    /**
     * Tests non-existence of flight
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage4_fetchFlight_neg() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8080/flight/131").asObject(String.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(),jsonresponse.getStatus());
    }

    /**
     * Tests deleting a file not in database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage5_deleteFlight_neg() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8080/flight/131").asString();
        int status = response.getStatus();
        Assert.assertEquals(status,400);
    }
}