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
import org.junit.*;
import org.junit.runners.MethodSorters;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlightControllerTest {
    @Autowired
    private MockMvc mockmvc;

    static int flightId = 0;
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


    /**
     * Tests existence of flight in database
     * @throws UnirestException
     */
    @Test
    // Positive test, if flight exists
    public void stage2_fetchFlight_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/flight/121").asObject(String.class);

        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    /**
     * Tests non-existence of flight
     * @throws UnirestException
     */
    @Test
    // Negative test, if flight does not exist
    public void stage3_fetchFlight_neg() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/flight/001").asObject(String.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(),jsonresponse.getStatus());
    }

    /**
     * Test if flight can be created
     * @throws UnirestException
     */
    @Test
    //Positive test, flight creation
    public void stage1_createFlight() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8888/flight/101?price=150&from=Houston&to=San%20Jose&departureTime=2018-05-16-13&arrivalTime=2018-05-16-17&description=Direct%20flight&capacity=50&model=Boeing%20747&manufacturer=airbus&year=1993")
                .header("accept","application/json")
                .queryString("apiKey","121")
                .asJson()
                ;
        Assert.assertEquals(HttpStatus.OK.value(),jsonResponse.getStatus());

    }


    /**
     * Tests deleting a flight in database
     * @throws UnirestException
     */
    @Test
    //Positive test, if flight exists
    public void stage4_deleteFlight_pos() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8888/flight/121").asString();
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,HttpStatus.OK.value());
    }

    /**
     * Tests deleting a file not in database
     * @throws UnirestException
     */
    @Test
    //Negative test, if flight doesn't exists
    public void stage5_deleteFlight_neg() throws UnirestException {
        HttpResponse response = Unirest.delete("http://localhost:8888/flight/100").asString();
        int status = response.getStatus();
        Assert.assertEquals(status,400);
    }
}