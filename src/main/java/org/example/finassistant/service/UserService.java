package org.example.finassistant.service;

import org.example.finassistant.exception.DataNotFoundException;
import org.example.finassistant.model.User;
import org.example.finassistant.repository.UserRepository;
import org.example.finassistant.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
    public Optional<User> findByID (Long id){
        return Optional.of(userRepository.findById(id)).orElseThrow(() -> new DataNotFoundException("Invalid auth data"));
    }
    public long loginUser(String email, String password) {
        User u = userRepository.findByEmail(email);
        if (u.getPassword().equals(password)) {
            return u.getId();
        } else {
            throw new AccessDeniedException("Acsess denied");
        }
    }
    public List<User> updatePasswords(String password){
        return userRepository.findUserByPassword(password);
    }
}
