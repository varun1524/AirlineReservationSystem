package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import org.json.JSONObject;
import org.json.XML;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class FlightControllerTest {
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
    public void fetchFlight() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/flight/121").asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    @Test
    public void createFlight() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8888/flight/121?price=150&from=Houston&to=San%20Jose&departureTime=2018-05-16-13&arrivalTime=2018-05-16-17&description=Direct%20flight&capacity=50&model=Boeing%20747&manufacturer=airbus&year=1993")
                .header("accept","application/json")
                .queryString("apiKey","121")
                .asJson()
                ;
        System.out.println(jsonResponse.getBody());

    }

    @Test
    public void deleteFlight() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8888/flight/121").asString();
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,HttpStatus.OK);



    }
}