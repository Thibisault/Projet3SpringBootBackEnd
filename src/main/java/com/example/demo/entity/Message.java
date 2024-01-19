package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id;

    private Long rental_id;

    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
}
