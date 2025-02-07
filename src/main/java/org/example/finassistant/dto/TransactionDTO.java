package org.example.finassistant.dto;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionDTO {
    long id;
    long author;
}
