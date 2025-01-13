package org.example.finassistant.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.example.finassistant.model.Message;
import org.example.finassistant.model.User;
import org.example.finassistant.security.JwtUtil;
import org.example.finassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5501", allowCredentials = "true")
public class AuthController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private  UserService userService;

//    @Autowired
//    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//        this.userService = userService;
//    }

    @PostMapping("/login")
    public Message login(@RequestBody User user, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        // Создание куки с JWT-токеном

        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(false); // Защита от XSS
        cookie.setPath("/"); // Доступна для всех путей
        cookie.setMaxAge(36000); // Время жизни куки в секундах (например, 1 час)
        cookie.setSecure(false);
        response.addCookie(cookie); // Добавление куки в ответ
        response.setContentType("application/json");
        System.out.println("Токен: " + token);
        System.out.println("Кука добавлена: " + cookie.getName() + "=" + cookie.getValue());
        return Message.builder().message(token).build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }
}
