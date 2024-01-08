package com.example.demo;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class deleteDDB {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;


    @Test
    void deleteUserRepositoryBDD(){
        userRepository.deleteAll();
    }

    @Test
    void deleteRentalRepositoryBDD(){
        rentalRepository.deleteAll();
    }

    @Test
    void createRowInDDB(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("user");
        userEntity.setPassword("user");
        userRepository.save(userEntity);

    }
}
