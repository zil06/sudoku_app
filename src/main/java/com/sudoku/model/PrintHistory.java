package com.sudoku.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "print_history")
public class PrintHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private SudokuProblem problem;

    @Column(nullable = false)
    private LocalDateTime printedAt;

    @PrePersist
    public void prePersist() {
        this.printedAt = LocalDateTime.now();
    }

    // ── Getters / Setters ──────────────────────────────

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public SudokuProblem getProblem() { return problem; }
    public void setProblem(SudokuProblem problem) { this.problem = problem; }

    public LocalDateTime getPrintedAt() { return printedAt; }
    public void setPrintedAt(LocalDateTime printedAt) { this.printedAt = printedAt; }
}
