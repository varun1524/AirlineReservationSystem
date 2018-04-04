package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;

    public Passenger addPassenger(Map params){
        Passenger passenger = null;
        try{
            Passenger passenger1 = new Passenger();
            passenger1.setPhone(params.get("phone").toString());
            passenger1.setAge(Integer.parseInt(params.get("age").toString()));
            passenger1.setGender(params.get("gender").toString());
            passenger1.setFirstname(params.get("firstname").toString().toLowerCase());
            passenger1.setLastname(params.get("lastname").toString().toLowerCase());
            passenger = passengerRepository.save(passenger1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return passenger;
    }

    public Passenger findByPassengerId(String id){
        return passengerRepository.findByPassengerId(id);
    }

    public List<Passenger> findAllPassengers(){
        return passengerRepository.findAll();
    }
}
