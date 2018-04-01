package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Passenger findByPassengerId(int id);

    List<Passenger> findAll();
}
