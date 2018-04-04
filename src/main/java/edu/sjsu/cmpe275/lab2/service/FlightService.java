package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import org.json.HTTP;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
public class FlightService {
    @Autowired
    FlightRepository flightRepository;

    public List<Flight> findAll(){
        return flightRepository.findAll();
    }

    public ResponseEntity findByFlightNumber(String flightNumber, boolean responseType){
        HttpStatus status = null;
        JSONObject jsonObject = new JSONObject();
        try {
            Flight flight = flightRepository.findByFlightNumber(flightNumber);
            if(flight != null){
                status = HttpStatus.OK;
                jsonObject = flight.getWholeFlightDetailsJSON();
                if(responseType){
                    return new ResponseEntity(XML.toString(jsonObject), status);
                }
                else {
                    return new ResponseEntity(jsonObject.toString(), status);
                }
            }
            else {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("msg", "Failed to delete Flight with number "+ flightNumber);
                jsonObject1.put("code", status);
                jsonObject.put("Bad Request",jsonObject1);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity addOrUpdateFlight(String flightNumber, Map<String, String> params){
        Flight flightObj= null;
        HttpStatus status = null;
        try {
            Flight flight = flightRepository.findByFlightNumber(flightNumber);
            for (String key : params.keySet()) {
                System.out.println(key + "   :  " + params.get(key));
            }
            Plane p = new Plane();
            p.setModel(params.get("model"));
            p.setCapacity(Integer.parseInt(params.get("capacity")));
            p.setYear(1997);
            p.setManufacturer("manufacturer");

            if (flight == null) {
                flight = new Flight();
                flight.setFlightNumber(flightNumber);
                flight.setSource(params.get("from"));
                flight.setDestination(params.get("to"));
                flight.setSeatsLeft(p.getCapacity());
                flight.setPrice(Double.parseDouble(params.get("price")));
                flight.setDescription(params.get("description"));
                flight.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd-HH").parse(params.get("departureTime")));
                flight.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd-HH").parse(params.get("arrivalTime")));
                flight.setPlane(p);
                flightObj = flightRepository.save(flight);
                if(flightObj!=null){
                    status = HttpStatus.OK;
                }
                else {
                    status = HttpStatus.NOT_FOUND;
                }
            } else {

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(flightObj, status);
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
