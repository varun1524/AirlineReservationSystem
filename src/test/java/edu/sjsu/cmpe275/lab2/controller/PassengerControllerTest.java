package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

/**
 * @author Sannisth Soni
 * Reservation Controller Unit Test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PassengerControllerTest {

    static String PassengerId="";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test creating a new Passenger
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage1_createPassenger() throws UnirestException {
        String url = "http://localhost:8080/passenger?firstname=sannisth&lastname=shah&age=22&gender=male&phone=6545654510";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        JSONObject jsonObject = new JSONObject(jsonresponse.getBody());
        System.out.println(jsonObject);
        System.out.println();
        PassengerId = jsonObject.getJSONObject("passenger").getString("passengerId");
        System.out.println(PassengerId);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }


    /**
     * Test displaying passenger from database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage2_displayPassenger_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8080/passenger/"+PassengerId).asObject(String.class);
        System.out.println(PassengerId);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    /**
     * Tests updating of a passenger in database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage3_updatePassenger() throws UnirestException {
        String url = "http://localhost:8080/passenger/"+PassengerId+"?firstname=XX&lastname=YY&age=11&gender=famale&phone=123324323";
        HttpResponse<String> jsonresponse = Unirest.put(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    /**
     * Tests deleting passenger
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage4_deletePassenger_pos() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8080/passenger/"+PassengerId).asString();
        System.out.println(response);
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,200);
    }

    /**
     * Testing display of passenger not in database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage5_displayPassenger_neg() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8080/passenger/"+PassengerId).asObject(String.class);
        System.out.println(jsonresponse.getStatus());
        Assert.assertEquals(404,jsonresponse.getStatus());
    }

    /**
     * Deleting passenger not in database
     * @throws UnirestException Exception in UnitTest
     */
    @Test
    public void stage6_deletePassenger_neg() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8080/passenger/"+PassengerId).asString();
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(400,status);
    }
}