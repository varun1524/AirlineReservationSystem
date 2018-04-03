package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/passenger")
public class PassengerController {

    @Autowired
    PassengerService passengerService;

    @GetMapping(path = "test")
    public ResponseEntity test(){
        Passenger p = null;
        try{
            p = passengerService.findByPassengerId(1);
            System.out.println(p.getFirstname());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(p, HttpStatus.OK);
    }
}
