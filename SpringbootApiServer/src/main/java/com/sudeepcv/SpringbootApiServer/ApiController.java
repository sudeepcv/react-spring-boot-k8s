package com.sudeepcv.SpringbootApiServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class ApiController {
    @Value("${pod.name}")
    private String podName;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;



    @GetMapping("/podname")
    public ResponseEntity<String> getPodName() {
        try {
            if (podName == null || podName.isBlank()) {
                throw new RuntimeException("Pod name is not available");
            }
            return ResponseEntity.ok("The api server is running on Pod Name: " + podName);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving the pod name: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    @GetMapping("/users")
    public List<UserTable> getAllUsers() {
        return userRepository.findAll();
    }

}
