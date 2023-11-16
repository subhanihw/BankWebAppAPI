package com.nobita.springboot.bankwebappapi.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddCustomerDTO {
    @NotBlank (message = "Name cannot be empty")
    private String name;
    private String panNum;
}
