package com.example.demo.service;

import com.example.demo.entity.Rental;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Value("${server.port}")
    private String port;

    @Value("${APP_DB_HOST}")
    private String host;

    @Autowired
    private AuthService authService;

    public List<Rental> getAllRentals() {

        List<Rental> rentalList = rentalRepository.findAll();
        for (Rental rental : rentalList){
            String picturePath = "";
            picturePath = rental.getPicture();
            rental.setPicture(host+port +"/"+picturePath);
        }
        return rentalList;
    }

    public Rental getRentalById(Long id) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        return optionalRental.orElse(null);
    }

    public Rental createRental(String name, int surface, double price, MultipartFile picture, String description) {

        Date actualDate = new Date();
        Rental rental = new Rental();
        // Ici extraire l'utilisateur actuel à partir du token JWT
        UserEntity currentUser = authService.getCurrentUser();

        rental.setCreated_at(actualDate);
        rental.setUpdated_at(actualDate);
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);

        if (!picture.isEmpty()) {
            String picturePath = this.saveFile(picture);
            rental.setPicture(picturePath);
        }
        rental.setDescription(description);
        rental.setOwner_id(currentUser.getId());
        return rentalRepository.save(rental);
    }

    public boolean updateRental(Long id, Rental rental, String name, int surface, double price, String description) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
            Date actualDate = new Date();

            rental.setUpdated_at(actualDate);
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);

            rentalRepository.save(rental);
            return true;
        }
        return false;
    }

    public boolean deleteRental(Long id) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
            rentalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public String saveFile(MultipartFile file) {
        String uploadDir = "uploads/";
        String originalFileName = file.getOriginalFilename();
        String filePath = uploadDir + originalFileName;

        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
