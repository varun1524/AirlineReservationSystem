package edu.sjsu.cmpe275.lab2.service;

import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    @Autowired
    ResponseService responseService;


    public Reservation save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    public ResponseEntity findReservationsByID(String reservationNumber){
        ResponseEntity responseEntity = null;
        Reservation reservation = null;
        try{
            reservation = reservationRepository.findByReservationNumber(reservationNumber);
            if(reservation!=null){
//                responseEntity = new ResponseEntity<>(reservation.getWholeReservationDetailsJSON().toString(), HttpStatus.OK);
                responseEntity = new ResponseEntity<>(reservation, HttpStatus.OK);
            }
            else {
                responseEntity = new ResponseEntity<>(responseService.getErrorJSONResponse("No Reservation record exist with reservation number: "+ reservationNumber, HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    public ResponseEntity makeReservation(Map<String, String> params) throws Exception{
        ResponseEntity responseEntity = null;
        HttpStatus status = HttpStatus.NOT_FOUND;
        Reservation reservation = null;
        JSONObject jsonObject = null;
        String errorJSONResponse = responseService.getErrorJSONResponse("Failed to make reservation for Flight", HttpStatus.NOT_FOUND, "Bad Request");
        try{
            params.entrySet().forEach(stringStringEntry -> {
                System.out.println(stringStringEntry.getKey() + "  :  " + stringStringEntry.getValue());
            });

            String passengerId = params.get("passengerId");
            String[] flightArr = params.get("flightLists").split(",");
            Passenger passenger = passengerRepository.findByPassengerId(passengerId);

            if(passenger!=null){
                List<Flight> newFlightForReservation = new LinkedList<>();
                boolean isValidFlight = false;
                for (String flightNum : flightArr) {
                    Flight flight = flightRepository.findByFlightNumber(flightNum);
                    if(flight!=null){
                        newFlightForReservation.add(flight);
                        if(flight.getSeatsLeft()>0) {
                            List<Flight> previousBookings = passenger.getFlights();
                            for(Flight flight1 : previousBookings){
                                if(flight.getFlightNumber().equals(flight1.getFlightNumber())){
                                    isValidFlight = true;
                                    break;
                                }
                                else {
                                    if((flight.getDepartureTime().compareTo(flight1.getDepartureTime())>=0 && flight.getDepartureTime().compareTo(flight1.getArrivalTime())<=0)
                                            ||
                                            (flight.getArrivalTime().compareTo(flight1.getDepartureTime())>=0 && flight.getArrivalTime().compareTo(flight1.getArrivalTime())<=0)){
                                        isValidFlight = true;
                                        break;
                                    }
                                }
                            }
                        }
                        else {
                            isValidFlight = true;
                            break;
                        }
                    }
                    else {
                        isValidFlight = true;
                        break;
                    }
                }
                if(!isValidFlight){
                    if(checkInterBetweenOverLapping(newFlightForReservation)) {
                        reservation = createReservationEntry(newFlightForReservation, passenger);
                        if(reservation!=null){
//                            responseEntity = new ResponseEntity(reservation.getWholeReservationDetailsJSON().toString(), HttpStatus.OK);
                            responseEntity = new ResponseEntity(reservation, HttpStatus.OK);

                        }
                        else {
                            responseEntity = new ResponseEntity(errorJSONResponse, HttpStatus.NOT_FOUND);
                        }
                    }
                }
                else {
                    responseEntity = new ResponseEntity(errorJSONResponse, HttpStatus.NOT_FOUND);
                }
            }
            else{
                responseEntity = new ResponseEntity(errorJSONResponse, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    private boolean checkInterBetweenOverLapping(List<Flight> flights){
        boolean result = true;
        for(Flight flight : flights){
            for(Flight flight1 : flights){
                if(!flight.getFlightNumber().equals(flight1.getFlightNumber())){
                    if((flight.getDepartureTime().compareTo(flight1.getDepartureTime())>0
                            &&
                            flight.getDepartureTime().compareTo(flight1.getArrivalTime())<0)
                            ||
                            (flight.getArrivalTime().compareTo(flight1.getDepartureTime())>0
                                    &&
                                    flight.getArrivalTime().compareTo(flight1.getArrivalTime())<0)){
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    protected Reservation createReservationEntry(List<Flight> newflightList, Passenger passenger) throws Exception{
        double price = 0;
        Flight tempFlight = null;
        List<Flight> flightList = new LinkedList<>();
        for(Flight flight : newflightList){
            if((tempFlight = flightRepository.findByFlightNumber(flight.getFlightNumber()))!=null){
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
        Reservation reservation = reservationRepository.save(reservation1);

        if(reservation!=null){
//                return new ResponseEntity(XML.toString(reservation.), HttpStatus.OK);
            System.out.println("Reservation Successful");
        }
        else {
//                return new ResponseEntity(null, HttpStatus.NOT_FOUND);
            System.out.println("Reservation Unsuccessful");
            throw new Exception("Error. Perform RollBack");
        }
        return reservation;
    }

    //Search Reservation

    public ResponseEntity searchForReservation(Map<String, String> params){
        ResponseEntity responseEntity = null;
        try{
            //User Input
            String passengerId = params.get("passengerId");
            String origin = params.get("origin");
            String to = params.get("to");
            String flightNumber = params.get("flightNumber");

            Reservation reservation = null;

            responseEntity = new ResponseEntity(responseService.getErrorJSONResponse("No Reservations available for given input", HttpStatus.NOT_FOUND, "Bad_Request"), HttpStatus.NOT_FOUND);

            //region <Passenger Id Exists>
            if(passengerId!=null){
                Passenger passenger = passengerRepository.findByPassengerId(passengerId);
                if(passenger!=null) {
                    if (flightNumber != null) {
                        Flight flight = flightRepository.findByFlightNumber(flightNumber);
                        if(flight!=null){

                            if (origin != null) {
                                if (to != null) {
                                    //passenger, flightno, origin, to
                                    if (flight.getDestination().equals(to) && flight.getSource().equals(origin)) {
                                        //successful return
                                        reservation = reservationRepository.findByFlightsAndPassenger(flight, passenger);
                                        if (reservation != null) {
                                            responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                                        }
                                    }
                                } else {
                                    //passenger, flightno, origin
                                    if (flight.getSource().equals(origin)) {
                                        //successful return
                                        reservation = reservationRepository.findByFlightsAndPassenger(flight, passenger);
                                        if (reservation != null) {
                                            responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                                        }
                                    }
                                }
                            } else if (to != null) {
                                if (flight.getDestination().equals(to)) {
                                    reservation = reservationRepository.findByFlightsAndPassenger(flight, passenger);
                                    if (reservation != null) {
                                        responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                                    }
                                }
                            } else {
                                //passenger, flightno
                                reservation = reservationRepository.findByFlightsAndPassenger(flight, passenger);
                                if (reservation != null) {
                                    responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                                }
                            }
                        }
                    }
                }
            }
            //endregion
            //region <Passenger Id does not exists, FlightNumber Exists>
            else if(flightNumber!=null){
                Flight flight = flightRepository.findByFlightNumber(flightNumber);
                List<Reservation> reservations = null;
                if(flight!=null){
                    if(origin!=null){
                        if(to!=null){
                            //passenger, flightno, origin, to
                            if(flight.getDestination().equals(to) && flight.getSource().equals(origin)){
                                //successful return
                                reservations = reservationRepository.findAllByFlights(flight);
                                if(reservations.size()>0){
                                    responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                                }
                            }
                        }
                        else {
                            if(flight.getSource().equals(origin)){
                                reservations = reservationRepository.findAllByFlights(flight);
                                if(reservations.size()>0){
                                    responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                                }
                            }
                        }
                    }
                    else if(to!=null) {
                        if(flight.getDestination().equals(to)){
                            reservations = reservationRepository.findAllByFlights(flight);
                            if(reservations.size()>0){
                                responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                            }
                        }
                    }
                    else {
                        //passenger, flightno
                        reservations = reservationRepository.findAllByFlights(flight);
                        if(reservations.size()>0){
                            responseEntity = new ResponseEntity(reservation, HttpStatus.OK);
                        }
                    }
                }
            }
            //endregion
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }
}
