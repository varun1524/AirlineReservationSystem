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

import java.util.List;
import java.util.Map;

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

    public Passenger save(Passenger passenger){
        return passengerRepository.save(passenger);
    }

    public ResponseEntity<String> findByPassengerId(String id){
        Passenger pObj= null;
        HttpStatus status = null;
        Passenger passenger = null;
        JSONObject jsonObject = new JSONObject();
        try{
            passenger = passengerRepository.findByPassengerId(id);
            if(passenger!=null){
                status = HttpStatus.OK;
                jsonObject = passenger.getWholePassengerDetailsJSON();
            }
            else {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("msg", "Failed to delete Passenger with ID "+ id);
                jsonObject1.put("code", status);
                jsonObject.put("Bad Request",jsonObject1);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(jsonObject.toString(),status);
//        return new ResponseEntity(passenger, status);
    }

    public List<Passenger> findAllPassengers(){
        return passengerRepository.findAll();
    }

    public Passenger updatePassenger(String id, Map<String,String> map) {
        Passenger passenger = passengerRepository.findByPassengerId(id);
        passenger.setFirstname(map.get("firstname"));
        passenger.setLastname(map.get("lastname"));
        passenger.setAge(Integer.parseInt(map.get("age")));
        passenger.setGender(map.get("gender"));
        passenger.setPhone(map.get("phone"));

        return passenger;

    }

    public ResponseEntity createPassenger(Map<String,String> map){
        ResponseEntity responseEntity = null;
        Passenger passenger = new Passenger();
        passenger.setFirstname(map.get("firstname"));
        passenger.setLastname(map.get("lastname"));
        passenger.setGender(map.get("gender"));
        passenger.setAge(Integer.parseInt(map.get("age")));
        passenger.setPhone(map.get("phone"));
        Passenger passenger1 = passengerRepository.save(passenger);
        if(passenger1!=null){
            responseEntity = new ResponseEntity(passenger1.getPassengerJSON().toString(), HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity(responseService.getJSONResponse(
                    "Failed to create passenger with given details",
                    HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

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
            List<Flight> flightList = passenger.getFlights();
            List<Reservation> reservationList = passenger.getReservations();
            for(Flight flight : flightList){
                if(!flight.getPassengers().remove(passenger)){
                    throw new Exception("Error. Roll Back");
                }
                else {
//                    flightRepository.save(flight);
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
