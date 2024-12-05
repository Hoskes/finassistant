package org.example.finassistant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class InformationObject {
    @Id
    private Long id;
    private String title;
    private String data;
}
