package org.example.finassistant.dto;

import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class UserDTO {
    @Id
    long id;
    String name;
    String email;
    String role;
}
