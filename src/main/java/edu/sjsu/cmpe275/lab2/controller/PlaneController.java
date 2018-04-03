package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.service.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(path = "/plane")
public class PlaneController {

    @Autowired
    PlaneService planeService;

    @GetMapping(path = "fetchAllPlanes")
    public ResponseEntity fetchAllPlanes(){
        List<Plane>  planeList= Collections.emptyList();
        try{
            planeList= planeService.findAll();
            System.out.println("Output");
            System.out.println(planeList.size());
            System.out.println(planeList.get(0));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(planeList, HttpStatus.OK);
    }

    @PostMapping(path = "createPlane")
    public ResponseEntity createPlane(){
        List<Plane>  planeList= Collections.emptyList();
        try{

            Plane p = new Plane();
            p.setManufacturer("airbus");
            p.setCapacity(310);
            p.setModel("310");
            p.setYear(2001);
            planeService.save(p);

            planeList= planeService.findAll();
            System.out.println("Output");
            System.out.println(planeList.size());
            System.out.println(planeList.get(0));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(planeList, HttpStatus.OK);
    }

}
