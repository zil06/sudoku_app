# 動作テスト記録

---

## テスト環境

| 項目 | 内容 |
|------|------|
| 実行環境 | Google Cloud Shell |
| デプロイ先 | Google App Engine (GAE) |
| ブラウザ | Google Chrome |
| DB | SQLite |

---

## 数独問題の難易度定義

| 難易度 | 空きマス数 | difficulty値 |
|--------|-----------|-------------|
| 初級   | 〜39個    | EASY        |
| 中級   | 40〜45個  | MEDIUM      |
| 上級   | 46〜50個  | HARD        |

---

## テスト用・webアプリセットアップまでの手順

### 1. アプリの起動（初回）

```bash
./mvnw spring-boot:run
```

### 2. データ投入の確認
別ターミナルを立ち上げ、以下のコマンドでデータベースの中身を確認します。

```bash
# 各難易度500問ずつ、計1,500問入っているか確認
sqlite3 sudoku.db "SELECT difficulty, COUNT(*) FROM sudoku_problems GROUP BY difficulty;"
```

以下のように表示されれば成功です。

```Plaintext
EASY|500
HARD|500
MEDIUM|500
```

---

### 3. application.properties の確認

```properties
# SQLite接続設定
spring.datasource.url=jdbc:sqlite:sudoku.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.properties.hibernate.dialect=org.hibernate.community.dialect.SQLiteDialect

# 初期化設定
spring.sql.init.mode=always
spring.jpa.hibernate.ddl-auto=none

# その他
spring.thymeleaf.cache=false
```

---

### 4. ブラウザでの動作確認項目
[ ] http://localhost:8080 にアクセスし、トップ画面が表示されるか

[ ] 「初級」「中級」「上級」それぞれのボタンを押して、5問ずつランダムに表示されるか

[ ] 画面をリロードするたびに、表示される問題が入れ替わるか（1,500問からのランダム抽出確認）

[ ] 印刷プレビュー画面で、1ページに2問＋最後に解答がまとまって表示されるか

---
