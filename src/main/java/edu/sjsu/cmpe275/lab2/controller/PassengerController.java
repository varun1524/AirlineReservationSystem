package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/passenger")
public class PassengerController {

    @Autowired
    PassengerService passengerService;

    @PostMapping
    public ResponseEntity addPassenger(@RequestParam Map params){
        Passenger p = null;
        HttpStatus status = null;
        try{
            p = passengerService.addPassenger(params);
            if(p==null){
                status = HttpStatus.NOT_FOUND;
            }
            else {
                status = HttpStatus.OK;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(p, status);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity addPassenger(@PathVariable String id){
        Passenger p = null;
        HttpStatus status = null;
        try{
            p = passengerService.findByPassengerId(id);
            if(p==null){
                status = HttpStatus.NOT_FOUND;
            }
            else {
                status = HttpStatus.OK;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(p, status);
    }
}
