package org.example.finassistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.finassistant.model.User;
import org.example.finassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(){
        User user = new User(1L,"Andrew","password","admin");
        return user;
    }
    public String authUser(){
        return "sd";
    }
}
