package com.example.demo.controller;

import com.example.demo.entity.Rental;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.AuthService;
import com.example.demo.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/rentals")
public class RentalsController {

    @Autowired
    RentalService rentalService;

    @Autowired
    AuthService authService;

    @GetMapping
    public ResponseEntity<Map<String, List<Rental>>> getAllRentals() {
        Map<String, List<Rental>> listRental = new HashMap<>();

        List<Rental> rentals = rentalService.getAllRentals();



        listRental.put("rentals", rentals);
        return new ResponseEntity<>(listRental, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        if (rental != null) {
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createRental( @RequestParam("name") String name,
                                           @RequestParam("surface") int surface,
                                           @RequestParam("price") double price,
                                           @RequestParam("picture") MultipartFile picture,
                                           @RequestParam("description") String description) {

        Date actualDate = new Date();

        // Ici extraire l'utilisateur actuel à partir du token JWT
        UserEntity currentUser = authService.getCurrentUser();

        Rental rental = new Rental();

        rental.setCreated_at(actualDate);
        rental.setUpdated_at(actualDate);

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);

        if (!picture.isEmpty()) {
            String picturePath = rentalService.saveFile(picture);
            rental.setPicture(picturePath);
        }

        rental.setDescription(description);

        rental.setOwner_id(currentUser.getId());

        rentalService.createRental(rental);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental created!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Long id,
                                               @RequestParam("name") String name,
                                               @RequestParam("surface") int surface,
                                               @RequestParam("price") double price,
                                               @RequestParam("description") String description) {

        System.out.println("Update rentals");

        Rental rental = rentalService.getRentalById(id);

        Date actualDate = new Date();
        rental.setUpdated_at(actualDate);

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        boolean updated = rentalService.updateRental(id, rental);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental updated!");
        Map<String, String> responseFalse = new HashMap<>();
        responseFalse.put("message", "Rental not found!");

        if (updated) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseFalse, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRental(@PathVariable Long id) {
        boolean deleted = rentalService.deleteRental(id);
        if (deleted) {
            return new ResponseEntity<>("Rental deleted!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Rental not found", HttpStatus.NOT_FOUND);
        }
    }

}
