package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DTOuser {

    private long id;
    private String name;
    private String email;

    private Date created_at;
    private Date updated_at;

}
