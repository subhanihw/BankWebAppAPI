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
public class CreateAccountDTO {
    @NotBlank(message = "Account Type cannot be empty")
    private String accountType;
    @NotBlank(message = "Initial balance cannot be empty")
    private double balance;
    private double interestRate;
    @NotBlank(message = "Please assign some customer Id to account")
    private int customerId;
}
