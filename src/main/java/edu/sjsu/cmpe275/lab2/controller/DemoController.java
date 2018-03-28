package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/")
public class DemoController {

    @Autowired
    PassengerService passengerService;

    @GetMapping(path = "test")
    public ResponseEntity test(){
        List<Passenger> passengerList = passengerService.findAllPassengers();
        System.out.println(passengerList.size());
        System.out.println(passengerList.get(0).getEmail());
        return new ResponseEntity(passengerList, HttpStatus.OK);
    }

}
