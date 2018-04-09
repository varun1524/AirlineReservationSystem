package edu.sjsu.cmpe275.lab2;

import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FlightControllerTest {
    private final String compare = "{\"flight\":{\"departureTime\":\"2018-05-15 13:00:00.0\",\"plane\":{\"year\":1995,\"model\":\"Boeing 747\",\"capacity\":200,\"manufacturer\":\"Boeing3\"},\"passengers\":{\"passenger\":[{\"firstname\":\"varun\",\"gender\":\"male\",\"phone\":\"1235665656\",\"passengerId\":\"e8ee61e5-c567-48e2-b898-2791e27a37b1\",\"age\":23,\"lastname\":\"shah\"}]},\"price\":120,\"arrivalTime\":\"2018-05-15 17:00:00.0\",\"description\":\"Direct flight\",\"from\":\"San Jose\",\"to\":\"Houston\",\"seatsLeft\":199,\"flightNumber\":\"123\"}}";

    @TestConfiguration
    static class FlightServiceTest{
        @Bean
        public FlightService flightservice(){
            return new FlightService();
        }
    }

    @Autowired
    private FlightService flightservice;

    @MockBean
    private FlightRepository flightrepository;

    @Test
    public void test_display_flight(){
        RestTemplate restTemplate = new RestTemplate();
        String received = restTemplate.getForObject("http://localhost:8888/flight/123",String.class);
        Assert.assertEquals(received,compare);
    }
}
