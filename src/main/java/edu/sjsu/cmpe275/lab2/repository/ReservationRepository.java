package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    /**
     * Retrieves list of all Reservation in database
     * @return List of Reservation
     */
    List<Reservation> findAll();

    /**
     * Retrieves reservation corresponding to the passed reservationNUmber
     * @param reservationNumber reservationId by which to delete the reservation
     * @return Deleted record of the reservaiton
     */
    Reservation findByReservationNumber(String reservationNumber);

    /**
     * Retrieves reservation corresponding to the flight and passenger
     * @param flight flightId of search parameter
     * @param passenger passengerId of search parameter
     * @return Reservation corresponding to both flight and passenger
     */
    Reservation findByFlightsAndPassenger(Flight flight, Passenger passenger);

    /**
     * Returns list of Reservation corresponding to the flight
     * @param flight flightId of flight in search parameter
     * @return List of Reservation
     */
    List<Reservation> findAllByFlights(Flight flight);

    /**
     * Return number of deleted reservations
     * @param reservationNUmber reservationNumber corresponding
     * @return integer denoting records of reservation deleted
     */
    int deleteReservationByReservationNumber(String reservationNUmber);

}
