package com.sudoku.repository;

import com.sudoku.model.PrintHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrintHistoryRepository extends JpaRepository<PrintHistory, Long> {
    List<PrintHistory> findByUserUserIdOrderByPrintedAtDesc(Long userId);
}
