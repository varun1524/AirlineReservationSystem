package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PassengerControllerTest {
    @Autowired
    private MockMvc mockmvc;

    public String PassengerId="";

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
        String url = "http://localhost:8888/passenger?firstname=sannisth&lastname=shah&age=22&gender=male&phone=1013";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        JSONObject jsonObject = new JSONObject(jsonresponse.getBody());
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("passengerId"));
        PassengerId = jsonObject.get("passengerId").toString();
        System.out.println(PassengerId);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());

    }

    @Test
    // Positive test for displaying passenger
    public void displayPassenger_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8888/passenger/"+PassengerId).asObject(String.class);
        System.out.println(PassengerId);
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
        String url = "http://localhost:8888/passenger/"+PassengerId+"?firstname=XX&lastname=YY&age=11&gender=famale&phone=123";
        HttpResponse<String> jsonresponse = Unirest.put(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    @Test
    // Deleting passenger if passenger exists
    public void deletePassenger_pos() throws UnirestException {
        //createPassenger();

        HttpResponse response = Unirest.delete("http://localhost:8888/passenger/962d6980-c980-4cf3-85a3-73e7e6e85c67").asString();
//        HttpRequestWithBody response = Unirest.delete("http://localhost:8888/flight/121");
        System.out.println(response);
        int status = response.getStatus();
        System.out.println(status);
        System.out.println(PassengerId);
        Assert.assertEquals(status,200);
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