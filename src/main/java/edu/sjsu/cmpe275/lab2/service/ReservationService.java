package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.ReservatinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    @Autowired
    ReservatinoRepository reservatinoRepository;

    public Reservation save(Reservation reservation){
        return reservatinoRepository.save(reservation);
    }

    public List<Reservation> findAll(){
        return reservatinoRepository.findAll();
    }

    public List<Reservation> findReservationsByID(int reservationNumber){
        return reservatinoRepository.findByReservationNumber(reservationNumber);
    }


}
