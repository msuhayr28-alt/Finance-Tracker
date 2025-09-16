package com.Suhayr.Finance.Tracker.repository;

import com.Suhayr.Finance.Tracker.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long>{
    List<Transactions> findByUserId(Long userId);
    List<Transactions> findByUserIdAndCategoryId(Long userId, Long categoryId);

}
