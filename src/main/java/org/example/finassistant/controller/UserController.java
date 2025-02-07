package org.example.finassistant.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.finassistant.exception.AcsessForbiddenException;
import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.Message;
import org.example.finassistant.model.User;
import org.example.finassistant.service.UserService;
import org.example.finassistant.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:5501") // HC исправить настроечным файлом или глобальной конфигой
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Проверяем, существует ли пользователь с таким же email
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        User savedUser = userService.save(user);
        log.info("### Пользователь "+savedUser.getName()+" зарегистрирован");
        return ResponseEntity.ok(savedUser.getId());
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody User model) {
        User u = userService.findByEmail(model.getEmail()).orElseThrow(()->new DataNotFoundException("Wrong user email"));
        System.out.println(u.getPassword());
        if(PasswordHasher.checkPassword(model.getPassword(),u.getPassword())){
            log.info("### Пользователь "+u.getName()+" зашел в систему");
            return new ResponseEntity<>(new Message(u.getId()+""),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new Message("Not OK"), HttpStatus.FORBIDDEN);
        }
    }

}
