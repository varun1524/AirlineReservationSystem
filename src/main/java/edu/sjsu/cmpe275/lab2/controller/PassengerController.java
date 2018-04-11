package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.view.PassengerView;
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


    // API 3
    @PostMapping(path = "")
    public ResponseEntity createPassenger(@RequestParam Map<String,String> params){
        return passengerService.createPassenger(params);
    }

    // API 1&2
    @JsonView({PassengerView.summary.class})
    @GetMapping(path = "/{id}")
    public ResponseEntity displayPassenger(@PathVariable("id") String id, Map<String,String> map){
        return passengerService.findByPassengerId(id);
        //Return XML if map.get("xml") is true
    }

    //Update Passenger Details. API4
    @PutMapping(path = "/{id}")
    public ResponseEntity updatePassenger(@PathVariable("id") String id, Map<String,String> map){
        Passenger passenger = passengerService.updatePassenger(id,map);
        return new ResponseEntity(passenger,HttpStatus.OK);

    }

    //Delete passenger. API 5
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletePassenger(@PathVariable("id") String id){
        ResponseEntity responseEntity = null;
        try{
            responseEntity = passengerService.deletePassenger(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }
}
