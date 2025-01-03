package org.example.finassistant.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.finassistant.dto.AuthDTO;
import org.example.finassistant.dto.UserDTO;
import org.example.finassistant.mapper.UserMapper;
import org.example.finassistant.model.User;
import org.example.finassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/mock")
    public ResponseEntity<UserDTO> getMock(){
        User user = userService.getUser();
        UserDTO userDTO = UserMapper.INSTANCE.userToDTO(user);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @PostMapping(value = "/mock/auth")
    public ResponseEntity<UserDTO> mockAuthUser(@RequestBody AuthDTO dto){
        System.out.println("#"+dto.toString());
        User user = userService.authUser(dto.getEmail(),dto.getPassword());
        return new ResponseEntity<>(UserMapper.INSTANCE.userToDTO(user),HttpStatus.OK);
    }


}
