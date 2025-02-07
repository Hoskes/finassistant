package org.example.finassistant.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private int quantity;
    private LocalDateTime date_created;
    private LocalDateTime date_edited;
}
