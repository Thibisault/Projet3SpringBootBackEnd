package com.example.demo.controller;

import com.example.demo.entity.Rental;
import com.example.demo.entity.RentalDTO;
import com.example.demo.entity.TokenDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.AuthService;
import com.example.demo.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "rental", description = "API pour la manipulation des rentals, créer, afficher, update")
public class RentalsController {

    @Value("${server.port}")
    private String port;

    @Value("${APP_DB_HOST}")
    private String host;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "Récupérer tout les rentals", description = "Permet de récupérer tout les rentals et des les envoyer en format json")
    @GetMapping
    public ResponseEntity<Map<String, List<Rental>>> getAllRentals() {

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setListRental(rentalService.getAllRentals());
        return new ResponseEntity<>(rentalDTO.getAllRental(), HttpStatus.OK);
    }

    @Operation(summary = "Récupérer un rental par son id", description = "Récupère un rental et avant de le renvoyer, modifie sa variable String picture, pour adapter sa route en fonction du port utilisé")
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        if (rental != null) {
            rental.setPicture(host+port +"/"+rental.getPicture());
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Créer un nouveau rental", description = "Récupère tout les paramètre de création puis créer un new rental en appelant une méthohde permettant d'extraire le nom de l'image puis de créer une route après avoir enrigistrer l'image récupérer dans un dossier à la racine 'uploads'")
    @PostMapping
    public ResponseEntity<?> createRental( @RequestParam("name") String name,
                                           @RequestParam("surface") int surface,
                                           @RequestParam("price") double price,
                                           @RequestParam("picture") MultipartFile picture,
                                           @RequestParam("description") String description) {

        rentalService.createRental(name,surface,price,picture,description);
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setMessageGeneration("Rental created!");
        return new ResponseEntity<>(rentalDTO.createUpdateRentalMessage(), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à  jour un rental", description = "Récupère un rental grâce à son id, puis affiche un formulaire de mise à jour pour ce rental")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Long id,
                                               @RequestParam("name") String name,
                                               @RequestParam("surface") int surface,
                                               @RequestParam("price") double price,
                                               @RequestParam("description") String description) {

        Rental rental = rentalService.getRentalById(id);
        boolean updated = rentalService.updateRental(id, rental, name, surface, price, description);

        RentalDTO acceptedUpdateRentalDTO = new RentalDTO();
        acceptedUpdateRentalDTO.setMessageGeneration("Rental updated!");
        RentalDTO notFoundUpdateRentalDTO = new RentalDTO();
        notFoundUpdateRentalDTO.setMessageGeneration("Rental not found!");

        if (updated) {
            return new ResponseEntity<>(acceptedUpdateRentalDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(notFoundUpdateRentalDTO, HttpStatus.NOT_FOUND);
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
