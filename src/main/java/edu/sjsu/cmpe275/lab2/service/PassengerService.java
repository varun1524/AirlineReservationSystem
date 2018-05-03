package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sannisth Soni
 *
 * Service for Passenger Controller
 */
@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    ResponseService responseService;

    public PassengerService(){
        super();
    }

    public PassengerService(PassengerRepository passengerRepository){
        this.passengerRepository =  passengerRepository;
    }


    /**
     *
     * @param id - passenger id
     * @param xml - if true response in XML format else json format
     * @return - ResponseEntity Object with JSON response
     */
    public ResponseEntity<String> findByPassengerId(String id, boolean xml){
        ResponseEntity<String> responseEntity = null;
        Passenger passenger = null;
        try{
            passenger = passengerRepository.findByPassengerId(id);
            if(passenger!=null){
                if(xml){
                    responseEntity = new ResponseEntity<>(XML.toString(passenger.getWholePassengerDetailsJSON()),
                            HttpStatus.OK);
                }
                else {
                    responseEntity = new ResponseEntity<>(passenger.getWholePassengerDetailsJSON().toString(),
                            HttpStatus.OK);
                }
            }
            else {
                responseEntity = new ResponseEntity<>(responseService.getJSONResponse(
                        "Sorry, the requested passenger with id " + id +" does not exist",
                        HttpStatus.NOT_FOUND,
                        "Bad Request"), HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return responseEntity;
    }

    /**
     * Update Passenger entry in Database
     * @param id  Passenger Id
     * @param map Passenger data in key-value pair
     * @return ResponseEntity Object with JSON response
     */
    public ResponseEntity updatePassenger(String id, Map<String,String> map) {
        ResponseEntity responseEntity = new ResponseEntity(responseService.getJSONResponse("Passenger record does not exist with given passengerId: " + id, HttpStatus.NOT_FOUND, "Bad Request"), HttpStatus.NOT_FOUND);
        Passenger passenger = passengerRepository.findByPassengerId(id);
        if(passenger!=null){
            passenger.setFirstname(map.get("firstname"));
            passenger.setLastname(map.get("lastname"));
            passenger.setAge(Integer.parseInt(map.get("age")));
            passenger.setGender(map.get("gender"));
            passenger.setPhone(map.get("phone"));
            responseEntity = new ResponseEntity(passenger.getWholePassengerDetailsJSON().toString(), HttpStatus.OK);
        }
        return responseEntity;

    }

    /**
     * Create Passenger entry in Database
     * @param map Passenger data in key-value pair
     * @return ResponseEntity Object with JSON response
     */
    public ResponseEntity<String> createPassenger(Map<String,String> map){
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseService.getJSONResponse(
                "Failed to create passenger with given details",
                HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.BAD_REQUEST);;
        if(passengerRepository.findByPhone(map.get("phone"))==null){
            Passenger passenger = new Passenger();

            passenger.setFirstname(map.get("firstname"));
            passenger.setLastname(map.get("lastname"));
            passenger.setGender(map.get("gender"));
            passenger.setAge(Integer.parseInt(map.get("age")));
            passenger.setPhone(map.get("phone"));
            Passenger passenger1 = passengerRepository.save(passenger);
            if(passenger1!=null){
                responseEntity = new ResponseEntity<>(passenger1.getWholePassengerDetailsJSON().toString(), HttpStatus.OK);
            }
        }
        return responseEntity;
    }

    /**
     * Delete Passenger with Passenger ID
     * @param id Passenger ID to be removed
     * @return ResponseEntity object with response
     * @throws Exception throws Exception in case of failure to perform any operation and performs roll back
     */
    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<String> deletePassenger(String id) throws Exception{
        String msg="";
        ResponseEntity<String> responseEntity = null;
        Passenger passenger = passengerRepository.findByPassengerId(id);
        if(passenger==null){
            //msg = "Passenger "+id+" not found";
            responseEntity = new ResponseEntity<>(responseService.getJSONResponse(
                    "Failed to delete Passenger with ID "+ id, HttpStatus.BAD_REQUEST, "Bad Request"),
                    HttpStatus.BAD_REQUEST);
        }
        else{
            Set<Flight> flightList = passenger.getFlights();
            List<Reservation> reservationList = passenger.getReservations();
            for(Flight flight : flightList){
                if(!flight.getPassengers().remove(passenger)){
                    throw new Exception("Error. Roll Back");
                }
            }
            for(Reservation reservation : reservationList){
                if(reservationRepository.deleteReservationByReservationNumber(reservation.getReservationNumber())!=1){
                    throw new Exception("Error. Roll Back");
                }
            }
            System.out.println(passenger);
            if(passengerRepository.deletePassengerByPassengerId(passenger.getPassengerId())==1){
                System.out.println("Deleted Passenger");
                responseEntity = new ResponseEntity<>(responseService.getXMLResponse("Passenger with ID "+ id + " removed", HttpStatus.OK, "Response" ), HttpStatus.OK);
            }
            else {
                throw new Exception("Error. Roll Back");
            }
        }
        return responseEntity;
    }
}
