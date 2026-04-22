package com.sudoku.service;

import com.sudoku.model.PrintHistory;
import com.sudoku.model.SudokuProblem;
import com.sudoku.model.User;
import com.sudoku.repository.PrintHistoryRepository;
import com.sudoku.repository.SudokuProblemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SudokuService {

    private final SudokuProblemRepository problemRepo;
    private final PrintHistoryRepository historyRepo;

    public SudokuService(SudokuProblemRepository problemRepo,
                         PrintHistoryRepository historyRepo) {
        this.problemRepo = problemRepo;
        this.historyRepo = historyRepo;
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

    /**
     * 印刷履歴を保存する
     */
    @Transactional
    public void savePrintHistory(User user, List<SudokuProblem> problems) {
        for (SudokuProblem p : problems) {
            PrintHistory history = new PrintHistory();
            history.setUser(user);
            history.setProblem(p);
            historyRepo.save(history);
        }
    }

    /**
     * ユーザーの印刷履歴一覧（新しい順）
     */
    public List<PrintHistory> getHistory(Long userId) {
        return historyRepo.findByUserUserIdOrderByPrintedAtDesc(userId);
    }
}
