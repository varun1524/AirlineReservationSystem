package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

    List<Flight> findAll();

    Flight findByFlightNumber(String flightNumber);

    @Transactional
    int deleteFlightByFlightNumber(String flightNumber);


    @Query("select f from Flight f inner join f.passengers p  " +
            " where (p.passengerId = :passengerId and " +
            " f.flightNumber<>:flightNo and  ((f.departureTime >= :departureTime  and f.departureTime <= :arrivalTime) " +
            " or (f.arrivalTime >= :departureTime  and f.arrivalTime <= :arrivalTime)))")
    List<Flight> findOverLappedFlights(@Param("passengerId") String passengerId, @Param("flightNo") String flightNo, @Param("departureTime") Date departureTime, @Param("arrivalTime") Date arrivalTime);
}
