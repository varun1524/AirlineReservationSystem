package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findAll();

    Reservation findByReservationNumber(String reservationNumber);

    Reservation findByFlightsAndPassenger(Flight flight, Passenger passenger);

    List<Reservation> findAllByFlights(Flight flight);

    int deleteReservationByReservationNumber(String reservationNUmber);

}
