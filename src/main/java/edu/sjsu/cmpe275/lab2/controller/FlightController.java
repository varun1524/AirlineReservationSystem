package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(path = "/flight")
public class FlightController {

    @Autowired
    FlightService flightService;

    @GetMapping(path = "/fetchAllFlights")
    public ResponseEntity fetchAllFlights(){
        List<Flight>  flightList= Collections.emptyList();
        try{
            flightList = flightService.findAll();
            System.out.println("Output");
            System.out.println(flightList);
            System.out.println(flightList.size());
            System.out.println(flightList.get(0).getPlane().getManufacturer());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(flightList, HttpStatus.OK);
    }

    @PostMapping(path = "createFlight")
    public ResponseEntity createFlight(){
        List<Flight>  flightList= Collections.emptyList();
        try{

            String departure = "2018-04-13-17";
            String arrival = "2018-04-13-21";

            Date departureDate = new SimpleDateFormat("yyyy-MM-dd-HH").parse(departure);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(departureDate));

            Date arrivalDate = new SimpleDateFormat("yyyy-MM-dd-HH").parse(arrival);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arrivalDate));

//            Flight flight = new Flight();
//            flight.setFlightNumber("123");
//            flight.setDepartureTime(departureDate);
//            flight.setArrivalTime(arrivalDate);
//            flight.setSource("San Jose, CA");
//            flight.setDestination("Houston, TX");
//            flight.setPlane(planeService.findPlane(1));
//            flight.setDescription("This is a Boeing Plane");
//            flight.setPrice(100);
//            flight.setSeatsLeft(planeService.findPlane(1).getCapacity());

//            Flight flight = new Flight();
//            flight.setFlightNumber("225");
//            flight.setDepartureTime(departureDate);
//            flight.setArrivalTime(arrivalDate);
//            flight.setSource("Houston, TX");
//            flight.setDestination("Orlando, FL");
//            flight.setPlane(planeService.findPlane(2));
//            flight.setDescription("This is a Airbus Plane");
//            flight.setPrice(200);
//            flight.setSeatsLeft(planeService.findPlane(2).getCapacity());
//            flightService.save(flight);

            flightList = flightService.findAll();
            System.out.println("Output");
            System.out.println(flightList);
            System.out.println(flightList.size());
            System.out.println(flightList.get(0).getPlane().getManufacturer());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(flightList, HttpStatus.OK);
    }

}
