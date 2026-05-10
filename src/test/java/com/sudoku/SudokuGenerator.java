package com.sudoku;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuGenerator {
//問題文を生成するときのコマンドは以下の通り
//mvn exec:java -Dexec.mainClass="com.sudoku.SudokuGenerator" -Dexec.classpathScope="test" > result.sql

    private static final int SIZE = 9;
    private int[][] board = new int[SIZE][SIZE];

    public static void main(String[] args) {
        SudokuGenerator gen = new SudokuGenerator();
        
        System.out.println("-- ── 初級問題（空きマス〜39個）──────────────────────");
        System.out.println("-- 0 = 空きマス、1〜9 = 数字");
                
        // 最初だけ「INSERT INTO...」を出力（あとはVALUESのカッコだけ並べる）
        System.out.println("INSERT INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES");

        for (int i = 1; i <= 500; i++) {
            // 1. 盤面をリセット
            gen.board = new int[SIZE][SIZE];
            
            // 2. 問題生成
            if (gen.fillBoard()) {
                // 初級(EASY)の要件：空きマス35〜39個くらいが目安
                // ランダムに 35〜39 の間で穴をあける
                int holes = 35 + (int)(Math.random() * 5); 
                
                // 3. SQLの VALUES 部分を出力
                gen.printAsValuesOnly("EASY", holes);
                
                // 4. 最後以外はカンマ、最後だけセミコロン
                if (i < 500) {
                    System.out.println(",");
                } else {
                    System.out.println(";");
                }
            }
        }

        System.out.println("\n-- ── 中級問題（空きマス40〜45個）──────────────────");
        System.out.println("INSERT INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES");

        for (int i = 1; i <= 500; i++) {
            gen.board = new int[SIZE][SIZE];
            if (gen.fillBoard()) {
                // 中級(MEDIUM)の要件：空きマス40〜45個くらいが目安
                // ランダムに 40〜45 の間で穴をあける
                int holes = 40 + (int)(Math.random() * 6);
                gen.printAsValuesOnly("MEDIUM", holes);
                if (i < 500) {
                    System.out.println(",");
                } else {
                    System.out.println(";");
                }
            }
        }

        System.out.println("\n-- ── 上級問題（空きマス46〜50個）──────────────────");
        System.out.println("INSERT INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES");

        for (int i = 1; i <= 500; i++) {
            gen.board = new int[SIZE][SIZE];
            if (gen.fillBoard()) {
                // 上級(HARD)の要件：空きマス46〜50個くらいが目安
                // ランダムに 46〜50 の間で穴をあける
                int holes = 46 + (int)(Math.random() * 5);
                gen.printAsValuesOnly("HARD", holes);
                if (i < 500) {
                    System.out.println(",");
                } else {
                    System.out.println(";");
                }
            }
        }
    }

    /**
     * VALUES の中身 ( 'DIFF', 'PROB', 'ANS' ) だけを取り出す
    */
    private void printAsValuesOnly(String difficulty, int holeCount) {
        String answerData = boardToString(this.board);
        int[][] problemBoard = copyBoard(this.board);
        digHoles(problemBoard, holeCount);
        String problemData = boardToString(problemBoard);

        System.out.printf("('%s', '%s', '%s')", difficulty, problemData, answerData);
    }

    // 数字をルールに従って埋める（再帰処理）
    private boolean fillBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    List<Integer> numbers = IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList());
                    Collections.shuffle(numbers); // ランダム性を出すためにシャッフル
                    for (int num : numbers) {
                        if (isSafe(row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard()) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // ルールチェック
    private boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            // 横、縦、3x3ブロックの重複チェック
            if (board[row][i] == num || board[i][col] == num || 
                board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * SQL形式で出力する
     * @param difficulty 難易度ラベル
     * @param holeCount 空ける穴の数
     */
    
    // 盤面をコピーする
    private int[][] copyBoard(int[][] source) {
        int[][] dest = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) System.arraycopy(source[i], 0, dest[i], 0, SIZE);
        return dest;
    }

    // ランダムに穴をあける
    private void digHoles(int[][] targetBoard, int holeCount) {
        List<Integer> positions = IntStream.range(0, 81).boxed().collect(Collectors.toList());
        Collections.shuffle(positions);

        for (int i = 0; i < holeCount; i++) {
            int pos = positions.get(i);
            targetBoard[pos / 9][pos % 9] = 0;
        }
    }

    // 9x9配列を81文字のStringに変換
    private String boardToString(int[][] targetBoard) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : targetBoard) {
            for (int num : row) {
                sb.append(num);
            }
        }
        return sb.toString();
    }
}
