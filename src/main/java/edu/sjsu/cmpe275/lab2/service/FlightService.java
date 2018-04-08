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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
public class FlightService {
    @Autowired
    FlightRepository flightRepository;

    @Autowired
    ResponseService responseService;

    public List<Flight> findAll(){
        return flightRepository.findAll();
    }

    public ResponseEntity findByFlightNumber(String flightNumber, boolean responseType){
        ResponseEntity responseEntity = null;
        JSONObject jsonObject = new JSONObject();
        Flight flight = null;
        try {
            flight = flightRepository.findByFlightNumber(flightNumber);
            if(flight != null){
                jsonObject = flight.getWholeFlightDetailsJSON();
                if(responseType){
                    responseEntity = new ResponseEntity(XML.toString(jsonObject), HttpStatus.OK);
                }
                else {
                    responseEntity = new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
//                    responseEntity = new ResponseEntity(flight, status);
                }
            }
            else {
                responseEntity = new ResponseEntity(responseService.getErrorJSONResponse("Failed to delete Flight with number "+ flightNumber, HttpStatus.NOT_FOUND, "Bad Request"), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }


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
                    responseEntity = new ResponseEntity(responseService.getErrorJSONResponse("Error while creating Flight", HttpStatus.NOT_FOUND, "Bad Request") ,HttpStatus.NOT_FOUND);
                }
            }
            else {
                if((flight.getPlane().getCapacity()-flight.getSeatsLeft()) < p.getCapacity()) {
                    if(isFlightUpdatable(flight)) {
                        flight = flightRepository.save(receivedFlight);
                        responseEntity = new ResponseEntity(flight, HttpStatus.OK);
                    }
                    else {
                        responseEntity = new ResponseEntity(responseService.getErrorJSONResponse("Flight time operlaps with passenger's ", HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.OK);
                    }
                }
                else {
                    responseEntity = new ResponseEntity(responseService.getErrorJSONResponse("Flight capacity less than already booked tickets ", HttpStatus.BAD_REQUEST, "Bad Request"), HttpStatus.OK);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

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

    public ResponseEntity deleteFlight(String flightNumber){
        HttpStatus status = null;
        JSONObject jsonObject = new JSONObject();
        try{
            Flight flight = flightRepository.findByFlightNumber(flightNumber);
            if(flight!=null){
                if(flight.getReservations().size()==0){
                    if(flightRepository.deleteFlightByFlightNumber(flightNumber)==1){
                        System.out.println("Flight Deleted Successfully");
                        status = HttpStatus.OK;
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("msg", "Flight with number "+ flightNumber +" is deleted successfully");
                        jsonObject1.put("code", status);
                        jsonObject.put("response",jsonObject1);
                    }
                    else {
                        System.out.println("Failed to delete flight");
                        status = HttpStatus.NOT_FOUND;
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("msg", "Failed to delete Flight with number "+ flightNumber);
                        jsonObject1.put("code", status);
                        jsonObject.put("Bad Request",jsonObject1);
                    }
                }
                else {
                    System.out.println("Reservation is made on flight. So it cannot be deleted");
                    status = HttpStatus.NOT_FOUND;
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("msg", "Failed to delete Flight with number "+ flightNumber + "because Reservation is made on flight. So it cannot be deleted");
                    jsonObject1.put("code", status);
                    jsonObject.put("Not Found",jsonObject1);
                }
            }
            else {
                status = HttpStatus.BAD_REQUEST;
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("msg", "Failed to delete Flight with number "+ flightNumber);
                jsonObject1.put("code", status);
                jsonObject.put("Bad Request",jsonObject1);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity(XML.toString(jsonObject.toString()), status);
    }
}
