package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservatinoRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findAll();

    List<Reservation> findByReservationNumber(int reservationNumber);

}
