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

    /**
     *
     * @param params (key,value) pairs of parameters and values for new object
     * @return newly created record
     */
    @PostMapping(path = "")
    public ResponseEntity createPassenger(@RequestParam Map<String,String> params){
        return passengerService.createPassenger(params);
    }

    // API 1&2

    /**
     *
     * @param id passengerId of passenger to be displayed
     * @param map pass xml=true for XML format
     * @return Passenger details including reservations and flights
     */
    @JsonView({PassengerView.summary.class})
    @GetMapping(path = "/{id}")
    public ResponseEntity displayPassenger(@PathVariable("id") String id,
                                           @RequestParam(value = "xml", required = false) String xml){
        boolean isResponseTypeXML = false;
        if(xml!=null && xml.equals("true")){
            isResponseTypeXML = true;
        }
        return passengerService.findByPassengerId(id, isResponseTypeXML);
        //Return XML if map.get("xml") is true
    }

    //Update Passenger Details. API4

    /**
     *
     * @param id passengerId of passenger whose record is to change
     * @param map (key,value) pairs of updated values and parameters
     * @return newly created record
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity updatePassenger(@PathVariable("id") String id, Map<String,String> map){
        ResponseEntity responseEntity = passengerService.updatePassenger(id,map);
        return responseEntity;

    }

    //Delete passenger. API 5

    /**
     *
     *
     * @param id passengerId of passenger who is to be deleted
     * @return passenger information of the deleted record
     */
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
