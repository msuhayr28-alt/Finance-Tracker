package com.Suhayr.Finance.Tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // Optional: list of transactions for bidirectional mapping
    @OneToMany(mappedBy = "category")
    private List<Transactions> transactions;

    @OneToMany(mappedBy = "category")
    private List<Budget> budgets;

}
