package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
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

public class PassengerControllerTest {
    @Autowired
    private MockMvc mockmvc;

    private final String compare = "{\"flightNumber\":\"126\",\"price\":150.0,\"source\":\"Houston\",\"destination\":\"San Jose\",\"departureTime\":\"2018-05-16-20\",\"arrivalTime\":\"2018-05-17-00\",\"seatsLeft\":50,\"description\":\"Direct flight\",\"plane\":{\"capacity\":50,\"model\":\"Boeing 747\",\"manufacturer\":\"airbus\",\"year\":1993},\"passengers\":[]}";

    @MockBean
    @Autowired
    private PassengerService passengerservice;

    @MockBean
    private PassengerRepository passengerrepository;


    @Before
    public void setUp() throws Exception {
        this.mockmvc = standaloneSetup(new PassengerController()).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    // Positive test for making Passenger
    // There cannot be negative as URL will always have those parameters (as given)
    public void createPassenger() throws UnirestException {
        String url = "http://localhost:8888/passenger?firstname=sannisth&lastname=shah&age=22&gender=male&phone=123321";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        String str = jsonresponse.getBody();
        System.out.println(str);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());

    }

    @Test
    // Positive test for displaying passenger
    public void displayPassenger_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/passenger/26cc83af-9443-4ec7-8913-945d3f983f0d").asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());

    }

    @Test
    //Negative test if passenger doesn't exist
    public void displayPassenger_neg() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/passenger/any-random-string").asObject(String.class);
        System.out.println(jsonresponse.getStatus());
        Assert.assertEquals(500,jsonresponse.getStatus());
    }
    @Test
    public void updatePassenger() throws UnirestException {
        String url = "http://localhost:8888/passenger/ee99d5c9-bb1a-44*a9-a5ad-a4d103999db9?firstname=XX&lastname=YY&age=11&gender=famale&phone=123";
        HttpResponse<String> jsonresponse = Unirest.put(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    @Test
    // Deleting passenger if passenger exists
    public void deletePassenger_pos() throws UnirestException {
        //createPassenger();
        HttpResponse response = Unirest.delete("http://localhost:8888/passenger/aecb25dc-5d7f-49de-ba26-41eb3badc9c3").asString();
//        HttpRequestWithBody response = Unirest.delete("http://localhost:8888/flight/121");
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,400);
    }


    @Test
    // Deleting passenger if passenger does not exists
    public void deletePassenger_neg() throws UnirestException {
        //createPassenger();
        HttpResponse response = Unirest.delete("http://localhost:8888/passenger/any-random-string").asString();
//        HttpRequestWithBody response = Unirest.delete("http://localhost:8888/flight/121");
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(400,status);
    }
}