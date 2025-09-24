package com.Suhayr.Finance.Tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequest {
    private String categoryName;
    private Double amount;
     private String description;
       // e.g. "EXPENSE" or "INCOME"
    private LocalDateTime transactionDate;
}

