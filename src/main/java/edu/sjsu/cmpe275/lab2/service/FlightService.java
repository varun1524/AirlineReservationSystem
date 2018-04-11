package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
public class FlightService {
    @Autowired
    FlightRepository flightRepository;

    @Autowired
    ResponseService responseService;

    /**
     * Find flight details by flight number
     * @param flightNumber Flight Number
     * @param responseType Response will be type XML if true else JSON
     * @return Response Entity Object with response
     */
    public ResponseEntity<Object> findByFlightNumber(String flightNumber, boolean responseType){
        ResponseEntity<Object> responseEntity = null;
        JSONObject jsonObject = new JSONObject();
        Flight flight = null;
        try {
            flight = flightRepository.findByFlightNumber(flightNumber);
            if(flight != null){
                if(responseType){
                    responseEntity = new ResponseEntity<Object>(XML.toString(jsonObject), HttpStatus.OK);
                }
                else {
                    responseEntity = new ResponseEntity<Object>(flight.getWholeFlightDetailsJSON().toString(), HttpStatus.OK);
//                    responseEntity = new ResponseEntity<>(flight, HttpStatus.OK);
                }
            }
            else {
                responseEntity = new ResponseEntity<>(responseService.getJSONResponse("FLight doesn not exist with number "+ flightNumber, HttpStatus.NOT_FOUND, "Bad Request"), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    /**
     * Adds flight in database and if already exists updates flight details
     * @param flightNumber Flight Number
     * @param params Flight data
     * @return ResponseEntity Object with response data
     */
    public ResponseEntity addOrUpdateFlight(String flightNumber, Map<String, String> params){
        ResponseEntity responseEntity = null;
        try {

            Flight flight = flightRepository.findByFlightNumber(flightNumber);

            Flight receivedFlight = new Flight();
            Plane p = new Plane();
            p.setModel(params.get("model"));
            p.setCapacity(Integer.parseInt(params.get("capacity")));
            p.setYear(Integer.parseInt(params.get("year")));
            p.setManufacturer(params.get("manufacturer"));

            receivedFlight.setPlane(p);
            receivedFlight.setFlightNumber(flightNumber);
            receivedFlight.setSource(params.get("from"));
            receivedFlight.setDestination(params.get("to"));
            receivedFlight.setSeatsLeft(p.getCapacity());
            receivedFlight.setPrice(Double.parseDouble(params.get("price")));
            receivedFlight.setDescription(params.get("description"));
            receivedFlight.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd-HH").parse(params.get("departureTime")));
            receivedFlight.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd-HH").parse(params.get("arrivalTime")));

            for (String key : params.keySet()) {
                System.out.println(key + "   :  " + params.get(key));
            }

            if (flight == null) {
                Flight flightObj = flightRepository.save(receivedFlight);
                if(flightObj!=null){
                    responseEntity = new ResponseEntity(flightObj, HttpStatus.OK);
                }
                else {
                    responseEntity = new ResponseEntity(responseService.getJSONResponse("Error while creating Flight", HttpStatus.NOT_FOUND, "Bad Request") ,HttpStatus.NOT_FOUND);
                }
            }
            else {
                if((flight.getPlane().getCapacity()-flight.getSeatsLeft()) < p.getCapacity()) {
                    if(isFlightUpdatable(flight)) {
                        flight = flightRepository.save(receivedFlight);
                        responseEntity = new ResponseEntity(flight, HttpStatus.OK);
                    }
                    else {
                        responseEntity = new ResponseEntity(responseService.getJSONResponse("Flight time operlaps with passenger's ", HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.OK);
                    }
                }
                else {
                    responseEntity = new ResponseEntity(responseService.getJSONResponse("Flight capacity less than already booked tickets ", HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.OK);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    /**
     * Checks whether the flight is updatable. Flight which is to be updated should not conflict
     * with its passenger's other bookings
     * @param flight Flight Object
     * @return Boolean if true flight can be updated otherwise cannot be updated
     */
    private boolean isFlightUpdatable(Flight flight){
        boolean result = true;
        for(Passenger passenger : flight.getPassengers()){
            for(Flight flight1 : passenger.getFlights()){
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

    /**
     * Delete Flight by Flight Number
     * @param flightNumber Flight Number
     * @return ResponseEntity Object with response data
     */
    @Transactional
    public ResponseEntity<String> deleteFlight(String flightNumber){
        ResponseEntity<String> responseEntity = null;
        try{
            Flight flight = flightRepository.findByFlightNumber(flightNumber);
            if(flight!=null){
                if(flight.getReservations().size()==0){
                    if(flightRepository.deleteFlightByFlightNumber(flightNumber)==1){
                        System.out.println("Flight Deleted Successfully");
                        responseEntity = new ResponseEntity<>(responseService.getXMLResponse("Flight with number "+ flightNumber +" is deleted successfully", HttpStatus.OK, "Response"), HttpStatus.OK);
                    }
                    else {
                        System.out.println("Failed to delete flight");
                        responseEntity = new ResponseEntity<>(responseService.getJSONResponse("Failed to delete Flight with number "+ flightNumber, HttpStatus.NOT_FOUND, "Bad_Request"), HttpStatus.NOT_FOUND);
                    }
                }
                else {
                    responseEntity = new ResponseEntity<>(responseService.getJSONResponse("Reservation is made on flight. So it cannot be deleted", HttpStatus.NOT_FOUND, "Bad_Request"), HttpStatus.NOT_FOUND);
                }
            }
            else {
                responseEntity = new ResponseEntity<>(responseService.getJSONResponse("Flight does not exist with flightNumber: "+ flightNumber, HttpStatus.BAD_REQUEST, "Bad_Request"), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }
}
