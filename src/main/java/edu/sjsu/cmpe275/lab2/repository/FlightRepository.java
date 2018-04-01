package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findAll();

    Flight findByFlightNumber(String flightNumber);
}
