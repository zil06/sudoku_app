package com.sudoku.model;

import jakarta.persistence.*;

/**
 * 数独問題エンティティ
 * problem_data / answer_data は81文字のString（左上から右下へ、0=空きマス）
 */
@Entity
@Table(name = "sudoku_problems")
public class SudokuProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    /** 難易度: EASY / MEDIUM / HARD */
    @Column(nullable = false, length = 10)
    private String difficulty;

    /** 81文字の問題データ（0=空きマス） */
    @Column(nullable = false, length = 81)
    private String problemData;

    /** 81文字の解答データ */
    @Column(nullable = false, length = 81)
    private String answerData;

    // ── Getters / Setters ──────────────────────────────

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getProblemData() { return problemData; }
    public void setProblemData(String problemData) { this.problemData = problemData; }

    public String getAnswerData() { return answerData; }
    public void setAnswerData(String answerData) { this.answerData = answerData; }

    // ── ヘルパー：81文字String → 9×9 int配列 ──────────

    /**
     * "530070000..." → int[9][9] に変換
     * 0 = 空きマス
     */
    public int[][] toProblemGrid() {
        return toGrid(problemData);
    }

    public int[][] toAnswerGrid() {
        return toGrid(answerData);
    }

    private int[][] toGrid(String data) {
        int[][] grid = new int[9][9];
        for (int i = 0; i < 81; i++) {
            grid[i / 9][i % 9] = Character.getNumericValue(data.charAt(i));
        }
        return grid;
    }
}
