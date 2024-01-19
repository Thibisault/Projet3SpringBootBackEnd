package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int surface;
    private double price;

    private String picture;
    private String description;

    private long owner_id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;
}