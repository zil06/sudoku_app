# 数独問題集アプリ (Sudoku App)

知人のお母様の「複数問題をまとめて印刷したい」という悩みを解消するために開発した、数独問題の管理・印刷用Webアプリケーションです。

## 公開URL
https://javasilvertest.an.r.appspot.com/

---

## プロジェクト概要

既存の数独サイトでは難しかった「複数問題の一括印刷」に特化しています。Java Silverで培ったオブジェクト指向の知識と、Spring Bootによる効率的なWeb開発を組み合わせて構築しています。

## 使用技術

### Backend
- Java 21 (LTS)
- Spring Boot 3.x
- Spring Data JPA
- Spring Security (認証・認可)

### Frontend
- Thymeleaf (テンプレートエンジン)
- CSS @media print (印刷レイアウト制御)

### Database / Infra
- SQLite (開発・MVP運用)
- Google App Engine (GAE)

## 主な機能

- **難易度別ランダム出題** — 初級・中級・上級から問題をランダムに抽出
- **印刷最適化レイアウト** — 1ページ2問の縦配置と、解答まとめページの自動生成
- **ユーザー認証** — 印刷履歴保存のためのログイン機能

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

/login → /mypage（印刷履歴一覧）
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
    ├── troubleshooting.md  # 苦労したエラーと解決策
    ├── dev-log.md          # 開発日誌
    └── testing-memo.md     # 動作テスト記録
```

---

## 今後の拡張検討項目

1. **問題の自動生成** — バックトラッキングアルゴリズムで数独を自動生成するバッチ処理
2. **用紙サイズ自動調整** — `@page { size: A4 }` を動的に切り替えるJS
3. **印刷済みチェック** — マイページで「印刷済み」バッジを表示
4. **Spring Security 導入** — 現在は簡易セッション認証のみ。印刷履歴以外の管理機能追加時に対応検討
5. **アカウント管理機能** — 新規作成・パスワード変更機能の実装検討
6. **印刷履歴管理機能** — 印刷済みの問題を自動で記録・新規表示対象から除外する機能の実装検討
---

## ✒️ Author

zil06 ([GitHub](https://github.com/zil06))