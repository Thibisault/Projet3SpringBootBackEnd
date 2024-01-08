package com.example.demo.service;

import com.example.demo.entity.Rental;
import com.example.demo.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(Long id) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        return optionalRental.orElse(null);
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public boolean updateRental(Long id, Rental rental) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
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
