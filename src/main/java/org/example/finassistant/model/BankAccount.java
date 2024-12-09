package org.example.finassistant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BankAccount {
    @Id
    private long id;
    private String title;
    private String type;
    private String personLink;
}
