package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;

    public Reservation save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    public ResponseEntity findReservationsByID(String reservationNumber){
        ResponseEntity responseEntity = null;
        try{
            Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);
            if(reservation!=null){
                responseEntity = new ResponseEntity<>(reservation.getWholeReservationDetailsJSON().toString(), HttpStatus.OK);
            }
            else {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("msg", "No Reservation record exist with reservation number: "+ reservationNumber);
                jsonObject1.put("code", HttpStatus.BAD_REQUEST.value());
                jsonObject.put("Bad Request",jsonObject1);
                responseEntity = new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    public ResponseEntity makeReservation(Map<String, String> params) {
        Reservation reservation = null;
        try{
            params.entrySet().forEach(stringStringEntry -> {
                System.out.println(stringStringEntry.getKey() + "   :  " + stringStringEntry.getValue());
            });
            String passengerId = params.get("passengerId");
            String[] flightArr = params.get("flightLists").split(",");
            List<Flight> flightList = new LinkedList<>();

            Passenger passenger = passengerRepository.findByPassengerId(passengerId);

            double price = 0;
            Flight tempFlight = null;
            for(String s : flightArr){
                if((tempFlight = flightRepository.findByFlightNumber(s))!=null){
                    price+=tempFlight.getPrice();
                    tempFlight.setSeatsLeft(tempFlight.getSeatsLeft()-1);
                    List<Passenger> passengers = tempFlight.getPassengers();
                    passengers.add(passenger);
                    tempFlight.setPassengers(passengers);
                }
                flightList.add(tempFlight);
            }

            Reservation reservation1 = new Reservation();
            reservation1.setFlights(flightList);
            reservation1.setPrice(price);
            reservation1.setPassenger(passenger);
            reservation = reservationRepository.save(reservation1);
            if(reservation!=null){
                return new ResponseEntity(XML.toString(reservation), HttpStatus.OK);
            }
            else {
                return new ResponseEntity(null, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
