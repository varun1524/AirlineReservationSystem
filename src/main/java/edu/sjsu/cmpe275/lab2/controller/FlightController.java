package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.view.FlightView;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
