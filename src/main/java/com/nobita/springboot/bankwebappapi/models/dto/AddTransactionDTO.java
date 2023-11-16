package com.nobita.springboot.bankwebappapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTransactionDTO {
    private String accountNum;
    private String type;
    private double amount;
}
