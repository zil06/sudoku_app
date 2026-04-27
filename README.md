# 数独問題集アプリ (Sudoku App)

知人のお母様の「複数問題をまとめて印刷したい」という悩みを解消するために開発した、数独問題の管理・印刷用Webアプリケーションです。

## 公開URL
https://javasilvertest.an.r.appspot.com/

## 開発記録
[docs/ ディレクトリ](./docs/)内の以下ファイル参照
　troubleshooting.md  # 遭遇したエラーと対処の記録
　dev-log.md          # 開発日誌
　testing-memo.md     # 動作テスト記録

---

## プロジェクト概要
対象者が利用中の数独サイトでは難しかった、「複数問題の一括印刷」に対応。
主にJava Silver試験で培ったプログラミング知識と、Spring BootによるWeb開発を組み合わせて構築しています。

## 使用技術

### Backend
- Java 17
- Spring Boot 4.0.5
- Spring Data JPA
- Spring Security (認証・認可)

### Frontend
- Thymeleaf (テンプレートエンジン)
- CSS @media print (印刷レイアウト制御)

### Database / Infra
- SQLite
- Google App Engine

## 主な機能

- **難易度別ランダム出題** — 初級・中級・上級から問題をランダムに抽出
- **印刷最適化レイアウト** — 1ページ2問の縦配置と、解答まとめページの自動生成

## アーキテクチャ

- **3層アーキテクチャ** — Controller / Service / Repository の分離による保守性の確保
- **セキュリティ** — パスワードのBCryptハッシュ化

---

## 画面遷移

```
/ (トップ)
 └─ 難易度選択 → /problems?difficulty=EASY|MEDIUM|HARD
                  └─ 印刷プレビュー → /print
                                       └─ window.print()
```

---

## テスト用ログインアカウント

| ユーザー名 | パスワード |
|-----------|----------|
| mother    | password123|
| admin     | password123|

---

## 構成ファイル一覧

```
sudoku/
├── pom.xml
├── src/main/
│   ├── java/com/sudoku/
│   │   ├── SudokuApplication.java     # 起動クラス・BCryptBean定義
│   │   ├── controller/
│   │   │   ├── SudokuController.java  # 問題表示・印刷・マイページ
│   │   │   └── AuthController.java    # ログイン・ログアウト
│   │   ├── model/
│   │   │   ├── SudokuProblem.java
│   │   │   ├── User.java
│   │   │   └── PrintHistory.java
│   │   ├── repository/
│   │   │   ├── SudokuProblemRepository.java
│   │   │   ├── UserRepository.java
│   │   │   └── PrintHistoryRepository.java
│   │   └── service/
│   │       └── SudokuService.java
│   └── resources/
│       ├── application.properties
│       ├── data.sql
│       └── templates/
│           ├── index.html
│           ├── problems.html
│           ├── print.html
│           ├── login.html
│           └── mypage.html
│       └── static/css/
│           ├── style.css
│           └── print.css
└── docs/
    ├── troubleshooting.md  # 遭遇したエラーと対処の記録
    ├── dev-log.md          # 開発日誌
    └── testing-memo.md     # 動作テスト記録
```

---

## 今後の拡張検討項目

1. **問題の自動生成** — 数独の問題を自動生成するバッチ処理の実装
2. **ユーザー認証** — 印刷履歴保存のためのログイン機能
3. **Spring Security 導入** — 現在は簡易セッション認証のみ、印刷履歴以外の管理機能追加時に対応検討
4. **印刷済みチェック** — マイページで「印刷済み」チェックを表示
5. **印刷履歴管理機能** — 印刷済みの問題を自動で記録、新規問題の表示対象から除外する機能の実装検討
6. **アカウント管理機能** — 新規作成・パスワード変更機能の実装検討

---

## ✒️ Author

zil06 ([GitHub](https://github.com/zil06))
