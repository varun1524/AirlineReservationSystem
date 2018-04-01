package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Integer> {

    List<Plane> findAll();

    Plane findPlaneByPlaneId(int planeId);


}
