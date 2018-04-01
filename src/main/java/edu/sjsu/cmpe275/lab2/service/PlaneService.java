package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.repository.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneService {
    @Autowired
    PlaneRepository planeRepository;

    public Plane save(Plane plane){
        return planeRepository.save(plane);
    }

    public Plane findPlane(int id){
        return planeRepository.findPlaneByPlaneId(id);
    }

    public List<Plane> findAll(){
        return planeRepository.findAll();
    }


}
