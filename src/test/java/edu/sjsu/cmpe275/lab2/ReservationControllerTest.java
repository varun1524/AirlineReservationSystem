package edu.sjsu.cmpe275.lab2;

import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationControllerTest {

    private final String compare = "{\"reservationNumber\":\"60c0912d-4298-4cfc-a95f-a54768d687ba\",\"price\":240.0,\"passenger\":{\"passengerId\":\"e8ee61e5-c567-48e2-b898-2791e27a37b1\",\"firstname\":\"varun\",\"lastname\":\"shah\",\"age\":23,\"gender\":\"male\",\"phone\":\"1235665656\"},\"flights\":[{\"flightNumber\":\"123\",\"price\":120.0,\"source\":\"San Jose\",\"destination\":\"Houston\",\"departureTime\":\"2018-05-15-20\",\"arrivalTime\":\"2018-05-16-00\",\"seatsLeft\":199,\"description\":\"Direct flight\",\"plane\":{\"capacity\":200,\"model\":\"Boeing 747\",\"manufacturer\":\"Boeing3\",\"year\":1995}},{\"flightNumber\":\"125\",\"price\":120.0,\"source\":\"Houston\",\"destination\":\"San Jose\",\"departureTime\":\"2018-05-14-18\",\"arrivalTime\":\"2018-05-14-22\",\"seatsLeft\":198,\"description\":\"Direct flight\",\"plane\":{\"capacity\":200,\"model\":\"Boeing 747\",\"manufacturer\":\"Boeing1\",\"year\":1993}}]}";
    private final String FIRSTNAME = "harry";
    private final String LASTNAME = "potter";
    private final String PASSENGER_ID = "e8ee61e5-c567-48e2-b898-2791e27a37b1";
    private final int age = 23;
    private final String gender = "male";
    private final String phone = "1223456789";

    @TestConfiguration
    static class ReservationServiceTest{
        @Bean
        public ReservationService reservationservice(){
            return new ReservationService();
        }
    }

    @Autowired
    private ReservationService reservationservice;

    @MockBean
    private ReservationRepository reservationrepository;

    @Test
    public void test_display_passenger(){
    RestTemplate restTemplate = new RestTemplate();
        //System.out.println(restTemplate.getForObject("http://localhost:8080/passenger/9357d248-8b52-48f9-a5ae-f574282d22f4",String.class));
        String received = restTemplate.getForObject("http://localhost:8888/reservation/60c0912d-4298-4cfc-a95f-a54768d687ba",String.class);
        Assert.assertEquals(received,compare);
    }
//
//    @Test
//    public void test_create_passenger(){
//        RestTemplate restTemplate = new RestTemplate();
//        String url = new String("http://localhost:8080/passenger?"+phone);
//
//        MultiValueMap<String,String> map = new LinkedMultiValueMap<String,String>() ;
//        map.add("firstname",FIRSTNAME);
//        map.add("lastname",LASTNAME);
//        map.add("gender",gender);
//        map.add("phone",phone);
//        map.add("age",String.valueOf(age));
//        //String received = restTemplate.postForObject(url,String.class);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<MultiValueMap<String,String>>(map,httpHeaders);
//        //Passenger returns = restTemplate.postForObject(url,,String.class);
//        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);
//        System.out.println(response);
//    }




}
