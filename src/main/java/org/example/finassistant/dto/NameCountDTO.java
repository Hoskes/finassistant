package org.example.finassistant.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NameCountDTO {
    private String name;
    private Long count;
}
