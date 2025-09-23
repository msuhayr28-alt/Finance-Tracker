package com.Suhayr.Finance.Tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetDTO {
    private Long id;            // now we show the id
    private String categoryName;
    private Double amount;
    private Integer month;
    private Integer year;


}

