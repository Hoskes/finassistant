package org.example.finassistant.controller;

import org.example.finassistant.exception.AcsessForbiddenException;
import org.example.finassistant.model.AuthRequest;
import org.example.finassistant.model.User;
import org.example.finassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private org.example.finassistant.security.JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @PostMapping("/auth/login")
    @CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
    public ResponseEntity<Long> login(@RequestBody AuthRequest authRequest) {
        Long id = userService.loginUser(authRequest.getUsername(),authRequest.getPassword());
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
    @PostMapping(value = "/auth/check")
    public ResponseEntity<String> checkData(@RequestBody Long id){
        User user = userService.findByID(id).orElseThrow(() -> new AcsessForbiddenException("InvalidUser"));
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }
}