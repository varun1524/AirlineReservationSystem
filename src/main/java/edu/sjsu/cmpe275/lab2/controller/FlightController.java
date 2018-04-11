package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.view.FlightView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/flight")
public class FlightController {

    @Autowired
    FlightService flightService;

    /**
     * This API will return list containing all flights
     * @return List containing all flights in database
     */
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

    /**
     *
     * @param flightNumber FlightId of the flight
     * @param xml Will return output as XML, if true
     * @return Flight information of given flightNumber
     */
    @JsonView({FlightView.summary.class})
    @GetMapping(path = "/{flightNumber}")
    public ResponseEntity fetchFlight(@PathVariable(value = "flightNumber") String flightNumber,
                                      @RequestParam(value = "xml", required = false) String xml){
        ResponseEntity responseEntity = null;
        HttpStatus status = null;
        try{
            System.out.println("flightNumber: " + flightNumber);
            System.out.println("XML: " + xml);
            boolean isResponseTypeXML = false;
            if(xml!=null && xml.equals("true")){
                isResponseTypeXML = true;
            }
            responseEntity = flightService.findByFlightNumber(flightNumber, isResponseTypeXML);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }


    /**
     *
     * @param flightNumber flightNumber for new Flight
     * @param params map with (parameters,values) as (keys,values)
     * @return The newly created flight
     */
    @JsonView({FlightView.summary.class})
    @PostMapping(path = "/{flightNumber}")
    public ResponseEntity createFlight(@PathVariable(value = "flightNumber") String flightNumber, @RequestParam Map<String, String> params){
        ResponseEntity entity = null;
        try{
            entity = flightService.addOrUpdateFlight(flightNumber, params);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return entity;
    }

    /**
     *
     * @param flightNumber flightNumber which is wanted deleted
     * @return Details of the deleted flight
     */
    @DeleteMapping(path = "/{flightNumber}")
    public ResponseEntity deleteFlight(@PathVariable(value = "flightNumber") String flightNumber){
        ResponseEntity entity = null;
        try{
            entity = flightService.deleteFlight(flightNumber);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return entity;
    }
}
