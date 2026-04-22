# ▼テスト中▼　数独問題集アプリ — セットアップガイド

## 構成ファイル一覧

```
sudoku/
├── pom.xml                          # Maven依存関係
├── src/main/
│   ├── java/com/sudoku/
│   │   ├── SudokuApplication.java   # 起動クラス
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
│       ├── application.properties   # DB接続設定
│       ├── data.sql                 # サンプルデータ
│       └── templates/               # Thymeleafテンプレート
│           ├── index.html           # トップ（難易度選択）
│           ├── problems.html        # 問題表示（2問）
│           ├── print.html           # 印刷プレビュー
│           ├── login.html           # ログイン
│           └── mypage.html          # 印刷履歴
│       └── static/css/
│           ├── style.css            # メインスタイル
│           └── print.css            # 印刷用スタイル
```

---

## セットアップ手順

### 1. MySQLデータベースの準備

```sql
-- MySQLにログイン後、data.sqlを実行
mysql -u root -p < src/main/resources/data.sql
```

### 2. application.properties の編集

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sudoku_db?...
spring.datasource.username=root
spring.datasource.password=【あなたのMySQLパスワード】
```

### 3. アプリの起動

```bash
./mvnw spring-boot:run
```

ブラウザで http://localhost:8080 にアクセス。

---

## サンプルアカウント

| ユーザー名 | パスワード |
|-----------|----------|
| mama      | password123 |
| admin     | password123 |

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

## 今後の拡張ポイント

1. **問題の自動生成** — バックトラッキングアルゴリズムで数独を自動生成し、DBに追加登録するバッチ処理を実装
2. **用紙サイズ自動調整** — `@page { size: A4 }` を動的に切り替えるJSを追加
3. **印刷済みチェック** — マイページで「印刷済み」バッジを問題一覧に表示
4. **Spring Security 導入** — 現在は簡易セッション認証。本番はSpring Securityに移行推奨

---

## 難易度の定義（要件定義書より）

| 難易度 | 空きマス数 | difficulty値 |
|--------|-----------|-------------|
| 初級   | 〜39個    | EASY        |
| 中級   | 40〜45個  | MEDIUM      |
| 上級   | 46〜50個  | HARD        |
