package com.sudoku.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sudoku.model.SudokuProblem;
import com.sudoku.model.User;
import com.sudoku.service.SudokuService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SudokuController {

    private final SudokuService sudokuService;

    public SudokuController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }

    // ── トップ画面 ──────────────────────────────────────

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ── 問題表示画面 ────────────────────────────────────

    /**
     * GET /problems?difficulty=EASY|MEDIUM|HARD
     * ランダム2問を取得してThymeleafに渡す
     */
    @GetMapping("/problems")
    public String showProblems(@RequestParam(defaultValue = "EASY") String difficulty,
                               Model model,
                               HttpSession session) {

        List<SudokuProblem> problems = sudokuService.getTwoProblems(difficulty);

        // 9×9グリッドに変換してビューへ
        model.addAttribute("problems", problems);
        model.addAttribute("difficulty", difficulty);

        // problem_id をセッションに保持（印刷プレビュー用）
        List<Long> ids = problems.stream()
                .map(SudokuProblem::getProblemId)
                .collect(Collectors.toList());
        session.setAttribute("currentProblemIds", ids);

        return "problems";
    }

    // ── 印刷プレビュー画面 ──────────────────────────────

    /**
     * GET /print
     * セッションに保持した problem_id から印刷用ページを生成
     */
    @GetMapping("/print")
    public String printPreview(Model model, HttpSession session) {

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) session.getAttribute("currentProblemIds");

        if (ids == null || ids.isEmpty()) {
            return "redirect:/";
        }

        List<SudokuProblem> problems = sudokuService.getByIds(ids);
        model.addAttribute("problems", problems);

        // ログイン済みなら履歴保存
        // ＞SQLiteでは実装できない機能(本番環境のsudoku.dbファイルは読み込み専用で記録不可)のため、一旦コメントアウト
        // User user = (User) session.getAttribute("loginUser");
        // if (user != null) {
        //     sudokuService.savePrintHistory(user, problems);
        // }

        return "print";
    }

    // ── マイページ ──────────────────────────────────────

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("history", sudokuService.getHistory(user.getUserId()));
        model.addAttribute("username", user.getUsername());
        return "mypage";
    }
}
