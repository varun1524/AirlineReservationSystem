package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.service.PlaneService;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(path = "/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;



    @GetMapping(path = "fetchReservationById")
    public ResponseEntity fetchReservationById(){
        List<Reservation> list = Collections.emptyList();
        try{

            /*List<Flight> flightList = new LinkedList<>();
            flightList.add(flightService.findByFlightNumber("123"));
            flightList.add(flightService.findByFlightNumber("124"));

            Reservation reservation = new Reservation();
            reservation.setFlights(flightList);
            reservation.setPassenger(passengerService.findByPassengerId(1));
            double price=0;
            for(Flight flight : flightList){
                price += flight.getPrice();
            }
            System.out.println("price: " + price);
            reservation.setPrice(price);
            Reservation reservation1 = reservationService.save(reservation);
            System.out.println(reservation1);*/
            list = reservationService.findReservationsByID(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
