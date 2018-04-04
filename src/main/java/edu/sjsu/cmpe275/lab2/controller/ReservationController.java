package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;


    @PostMapping
    public ResponseEntity makeReservation(@RequestParam Map<String, String> params){
        ResponseEntity responseEntity = null;
        try{
            responseEntity = reservationService.makeReservation(params);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }


    @GetMapping(path = "/{reservationNumber}")
    public ResponseEntity fetchReservationById(@PathVariable String reservationNumber){
        ResponseEntity responseEntity = null;
        try{
            responseEntity = reservationService.findReservationsByID(reservationNumber);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }
}
