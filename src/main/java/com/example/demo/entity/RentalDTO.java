package com.example.demo.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RentalDTO {

    private String message = "message";
    private String messageGeneration = "";
    private String rentals = "rentals";
    private List<Rental> listRental;

    public Map<String, List<Rental>> getAllRental() {
        Map<String, List<Rental>> syntaxListRental = new HashMap<>();
        syntaxListRental.put(rentals, listRental);
        return syntaxListRental;
    }

    public Map<String, String> createUpdateRentalMessage() {
        Map<String, String> sendMessageCreateRental = new HashMap<>();
        sendMessageCreateRental.put(message, messageGeneration);
        return sendMessageCreateRental;
    }

}
