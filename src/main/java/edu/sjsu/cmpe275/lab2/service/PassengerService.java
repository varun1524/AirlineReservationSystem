package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;

    public Passenger findByPassengerId(int id){
        return passengerRepository.findByPassengerId(id);
    }

    public List<Passenger> findAllPassengers(){
        return passengerRepository.findAll();
    }
}
