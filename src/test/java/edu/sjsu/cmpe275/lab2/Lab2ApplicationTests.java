package edu.sjsu.cmpe275.lab2;

import edu.sjsu.cmpe275.lab2.controller.PassengerController;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Lab2ApplicationTests {
	private PassengerController passengerController;
	private PassengerService passengerService;

	private static final String passengerId = "somethign goes here!";
	private static final String firstname = "";
	private static final String lastname = "";
	private static final String gender = "";
	private static final int age = 0;
	private static final String phone = "";



	private static final int planeId = 0;
	private static final int capacity = 0;
	private static final String model = "";
	private static final String manufacturer = "";
	private static final int year = 0;

	private static final String flightNumber = "";
	private double price = 0;
	private static final String source = "";
	private static final String destination = "";
	private static final Date departureTime = new Date("2017-09-03-19");
	private static final Date arrivalTime = new Date("2017-09-03-19");
	private static final int seatsLeft = 0;
	private static final String description = "";


	@Test
	public void contextLoads() {
	}

}
