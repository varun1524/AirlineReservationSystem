package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {

    /**
     * Return Passenger corresponding to the given PassengerId
     * @param id PassengerId by which to search the Passenger
     * @return Passenger corresponding to the given PassengerId
     */
    Passenger findByPassengerId(String id);

    /**
     * Get list of all passengers in the database
     * @return Passenger record corresponsing to the PassengerId
     */
    List<Passenger> findAll();

    /**
     * Delete passenger corresponding to the given PassengerId
     * @param id PassengerId by which to delete the Passenger
     * @return Newly deleted Passenger record
     */
    @Transactional
    int deletePassengerByPassengerId(String id);
}
