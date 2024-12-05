package org.example.finassistant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Transaction {

    @Id
    private Long id;
    private long clientId;
    private String date;
    private String price;
    private String type;
    private String payableObjectLink;
    private String payableObjectTitle;
}
