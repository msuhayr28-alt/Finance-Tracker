package com.Suhayr.Finance.Tracker.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequest {

    @NotBlank(message = "Category name cannot be empty")
    private String categoryName;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

     private String description;

    @NotNull(message = "Date is required")
    private LocalDateTime transactionDate;
}

