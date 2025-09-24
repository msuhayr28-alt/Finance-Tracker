package com.Suhayr.Finance.Tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private String categoryName;
    private Double amount;
    private String type;
    private LocalDateTime date;
}

