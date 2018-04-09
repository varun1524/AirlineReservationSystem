package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {

    Passenger findByPassengerId(String id);

    List<Passenger> findAll();

    @Transactional
    int deletePassengerByPassengerId(String id);
}
