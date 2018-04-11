package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

    /**
     *Returns list of all flights in database
     * @return List of Flight in the database
     */
    List<Flight> findAll();

    /**
     * Find flight given the flightNumber
     * @param flightNumber flightNumber whose record is to be searched
     * @return Flight record corresponding to the given flightNumber
     */
    Flight findByFlightNumber(String flightNumber);

    /**
     * Deleting flight given flightNumber
     * @param flightNumber flightId by which to delete the Flight
     * @return Newly deleted flight record
     */
    @Transactional
    int deleteFlightByFlightNumber(String flightNumber);


    @Query("select f from Flight f inner join f.passengers p  " +
            " where (p.passengerId = :passengerId and " +
            " f.flightNumber<>:flightNo and  ((f.departureTime >= :departureTime  and f.departureTime <= :arrivalTime) " +
            " or (f.arrivalTime >= :departureTime  and f.arrivalTime <= :arrivalTime)))")
    List<Flight> findOverLappedFlights(@Param("passengerId") String passengerId, @Param("flightNo") String flightNo, @Param("departureTime") Date departureTime, @Param("arrivalTime") Date arrivalTime);
}
