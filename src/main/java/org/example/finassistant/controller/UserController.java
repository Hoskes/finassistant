package org.example.finassistant.controller;

import org.example.finassistant.model.User;
import org.example.finassistant.service.UserService;
import org.example.finassistant.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/login")
    public String login(User model) {
        return "login"; // Возвращает имя шаблона для страницы входа
    }
}
