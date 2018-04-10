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
    public void createPassenger() throws UnirestException {
        String url = "http://localhost:8888/passenger?firstname=sannisth&lastname=shah&age=22&gender=male&phone=91343232";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK,jsonresponse.getStatus());

    }

    @Test
    public void displayPassenger() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/passenger/aecb25dc-5d7f-49de-ba26-41eb3badc9c3").asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());

    }

    @Test
    public void updatePassenger() throws UnirestException {
        String url = "http://localhost:8888/passenger/aecb25dc-5d7f-49de-ba26-41eb3badc9c3?firstname=sannisth&lastname=shah&age=25&gender=female&phone=4352343232";
        HttpResponse<String> jsonresponse = Unirest.get(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    @Test
    public void deletePassenger() throws UnirestException {
        createPassenger();
        HttpResponse response = Unirest.delete("http://localhost:8888/passenger/").asString();
//        HttpRequestWithBody response = Unirest.delete("http://localhost:8888/flight/121");
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,HttpStatus.OK);
    }
}