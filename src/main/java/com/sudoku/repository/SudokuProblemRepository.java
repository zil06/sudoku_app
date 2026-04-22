package com.sudoku.repository;

import com.sudoku.model.SudokuProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SudokuProblemRepository extends JpaRepository<SudokuProblem, Long> {

    /**
     * 難易度でフィルタし、ランダムに2件取得
     */
    @Query(value = "SELECT * FROM sudoku_problems WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT 2",
           nativeQuery = true)
    List<SudokuProblem> findTwoRandomByDifficulty(@Param("difficulty") String difficulty);

    List<SudokuProblem> findByDifficulty(String difficulty);
}
