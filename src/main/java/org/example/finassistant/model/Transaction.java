package org.example.finassistant.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date_created;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    private int quantity;
    @ManyToOne
    private Tax tax;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item payableItem;
    @Nullable
    private LocalDateTime date_edited;

}
