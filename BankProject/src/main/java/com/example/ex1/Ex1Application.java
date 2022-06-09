package com.example.ex1;

import com.example.ex1.dao.ClientRepository;
import com.example.ex1.dao.CompteRepository;
import com.example.ex1.dao.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex1Application implements ApplicationRunner {

    public static void main(String[] args) {

        SpringApplication.run(Ex1Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


    }
}
