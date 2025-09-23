package com.Suhayr.Finance.Tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetRequest {

    private String categoryName;
    private Double amount;
    private Integer month;
    private Integer year;

}
