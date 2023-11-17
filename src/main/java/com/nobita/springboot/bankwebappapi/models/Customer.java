package com.nobita.springboot.bankwebappapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    private String name;
    private String panNum;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts;
}