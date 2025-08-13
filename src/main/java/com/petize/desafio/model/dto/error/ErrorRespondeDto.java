package com.petize.desafio.model.dto.error;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRespondeDto {
    private String timestamp;
    private int status;
    private String error;
    private String message;
}
