package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Reservation reservation = null;
        try{
            reservation = reservationRepository.findByReservationNumber(reservationNumber);
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
//        return new ResponseEntity(reservation, HttpStatus.OK);
    }


    public ResponseEntity makeReservation(Map<String, String> params) throws Exception{
        ResponseEntity responseEntity = null;
        HttpStatus status = HttpStatus.NOT_FOUND;
        Reservation reservation = null;
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("msg", "Failed to make reservation for Flight");
        jsonObject1.put("code", status);
        jsonObject.put("Bad Request",jsonObject1);
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

                            /*List<Flight> overlappedFlights   = flightRepository.findOverLappedFlights(
                                    passenger.getPassengerId(),
                                    flight.getFlightNumber(),
                                    flight.getDepartureTime(),
                                    flight.getArrivalTime());
                            if(overlappedFlights.size()>0){
                                isValidFlight = true;
                                break;
                            }*/
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
                            jsonObject = reservation.getWholeReservationDetailsJSON();
                            responseEntity = new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
                        }
                        else {
                            responseEntity = new ResponseEntity(jsonObject.toString(), HttpStatus.NOT_FOUND);
                        }
                    }
                }
                else {
                    responseEntity = new ResponseEntity(jsonObject.toString(), HttpStatus.NOT_FOUND);
                }
            }
            else{
                responseEntity = new ResponseEntity(jsonObject.toString(), HttpStatus.NOT_FOUND);
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
                                    flight.getArrivalTime().compareTo(flight1.getArrivalTime())<0)
                            ){
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Reservation createReservationEntry(List<Flight> newflightList, Passenger passenger) throws Exception{
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
}
