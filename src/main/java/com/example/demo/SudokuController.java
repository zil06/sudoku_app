package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import java.util.stream.*;

@Controller
public class SudokuController {

    @GetMapping("/sudoku")
    public String showSudoku(Model model) {
        // 1〜9をシャッフル
        List<Integer> seed = IntStream.rangeClosed(1, 9).
        boxed().collect(Collectors.toList());
        Collections.shuffle(seed);

        // 9x9の盤面（2次元リスト）を生成
        List<List<Integer>> board = IntStream.
        range(0, 9).mapToObj(r -> 
            IntStream.range(0, 9).mapToObj(c -> {
                int index = (3 * (r % 3) + r / 3 + c) % 9;
                return seed.get(index);
            }).collect(Collectors.toList())
        ).collect(Collectors.toList());

        model.addAttribute("board", board);
        return "sudoku";
    }
}