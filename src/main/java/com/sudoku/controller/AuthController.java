package com.sudoku.controller;

import com.sudoku.model.User;
import com.sudoku.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ── ログイン画面 ────────────────────────────────────

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userRepo.findByUsername(username).orElse(null);

        if (user == null || !encoder.matches(password, user.getPasswordHash())) {
            model.addAttribute("error", "ユーザー名またはパスワードが正しくありません。");
            return "login";
        }

        session.setAttribute("loginUser", user);
        return "redirect:/";
    }

    // ── ログアウト ──────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
