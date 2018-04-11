package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
import edu.sjsu.cmpe275.lab2.view.ReservationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    /**
     *
     * @param params (key,value) pairs of paramenters and values to be passed
     * @return Reservation information of newly created object
     */
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


    /**
     *
     * @param params(key,value) pairs of paramenters and values to be passed for searching
     * @return Reservation according to passed parameters
     */
    @JsonView({ReservationView.summary.class})
    @GetMapping
    public ResponseEntity searchForReservation(@RequestParam Map<String, String> params){
        ResponseEntity responseEntity = null;
        try{
            responseEntity = reservationService.searchForReservation(params);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    /**
     *
     * @param reservationNumber reservationNumber which needs to be retrieved
     * @return Newly searched result     */
    @JsonView({ReservationView.summary.class})
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


    /**
     *
     * @param reservationNumber reservationNumber of the reservation to be deleted.
     * @return Newly deleted record
     */
    @DeleteMapping(path = "/{reservationNumber}")
    public ResponseEntity deleteReservation(@PathVariable("reservationNumber") String reservationNumber){
        ResponseEntity responseEntity = null;
        try{
            responseEntity = reservationService.cancelReservation(reservationNumber);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }
}
