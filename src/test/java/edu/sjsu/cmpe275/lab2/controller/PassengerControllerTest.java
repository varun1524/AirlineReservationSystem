package edu.sjsu.cmpe275.lab2.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PassengerControllerTest {
    @Autowired
    private MockMvc mockmvc;

    static String PassengerId="";

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

    /**
     * Test creating a new Passenger
     * @throws UnirestException
     */
    @Test
    // Positive test for making Passenger
    // There cannot be negative as URL will always have those parameters (as given)
    public void stage1_createPassenger() throws UnirestException {
        String url = "http://localhost:8080/passenger?firstname=sannisth&lastname=shah&age=22&gender=male&phone=45656543";
        HttpResponse<String> jsonresponse = Unirest.post(url).asObject(String.class);
        JSONObject jsonObject = new JSONObject(jsonresponse.getBody());
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("passengerId"));
        PassengerId = jsonObject.get("passengerId").toString();
        System.out.println(PassengerId);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }


    /**
     * Test displaying passenger from database
     * @throws UnirestException
     */
    @Test
    // Positive test for displaying passenger
    public void stage2_displayPassenger_pos() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8080/passenger/"+PassengerId).asObject(String.class);
        System.out.println(PassengerId);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    /**
     * Tests updating of a passenger in database
     * @throws UnirestException
     */
    @Test
    public void stage3_updatePassenger() throws UnirestException {
        String url = "http://localhost:8080/passenger/"+PassengerId+"?firstname=XX&lastname=YY&age=11&gender=famale&phone=123";
        HttpResponse<String> jsonresponse = Unirest.put(url).asObject(String.class);
        Assert.assertEquals(HttpStatus.OK.value(),jsonresponse.getStatus());
    }

    /**
     * Tests deleting passenger
     * @throws UnirestException
     */
    @Test
    // Deleting passenger if passenger exists
    public void stage4_deletePassenger_pos() throws UnirestException {
        //createPassenger();
        HttpResponse response = Unirest.delete("http://localhost:8080/passenger/"+PassengerId).asString();
//        HttpRequestWithBody response = Unirest.delete("http://localhost:8888/flight/121");
        System.out.println(response);
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(status,200);
    }

    /**
     * Testing display of passenger not in database
     * @throws UnirestException
     */
    @Test
    //Negative test if passenger doesn't exist
    public void stage5_displayPassenger_neg() throws UnirestException {
        HttpResponse<String> jsonresponse = Unirest.get("http://localhost:8080/passenger/"+PassengerId).asObject(String.class);
        System.out.println(jsonresponse.getStatus());
        Assert.assertEquals(404,jsonresponse.getStatus());
    }

    /**
     * Deleting passenger not in database
     * @throws UnirestException
     */
    @Test
    // Deleting passenger if passenger does not exists
    public void stage6_deletePassenger_neg() throws UnirestException {
        //createPassenger();
        HttpResponse response = Unirest.delete("http://localhost:8080/passenger/"+PassengerId).asString();
//        HttpRequestWithBody response = Unirest.delete("http://localhost:8888/flight/121");
        int status = response.getStatus();
        System.out.println(status);
        Assert.assertEquals(400,status);
    }
}