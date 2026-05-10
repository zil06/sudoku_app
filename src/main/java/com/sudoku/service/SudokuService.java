package com.sudoku.service;

import com.sudoku.model.SudokuProblem;
import com.sudoku.repository.SudokuProblemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SudokuService {

    private final SudokuProblemRepository problemRepo;

    public SudokuService(SudokuProblemRepository problemRepo) {
        this.problemRepo = problemRepo;
    }

    /**
     * 難易度を受け取りランダム2問を返す
     * @param difficulty "EASY" | "MEDIUM" | "HARD"
     */
    public List<SudokuProblem> getTwoProblems(String difficulty) {
        List<SudokuProblem> problems = problemRepo.findTwoRandomByDifficulty(difficulty);
        if (problems.isEmpty()) {
            throw new IllegalStateException("難易度「" + difficulty + "」の問題が登録されていません。");
        }
        return problems;
    }

    /**
     * problem_id のリストから問題を取得（印刷プレビュー用）
     */
    public List<SudokuProblem> getByIds(List<Long> ids) {
        return problemRepo.findAllById(ids);
    }
}
