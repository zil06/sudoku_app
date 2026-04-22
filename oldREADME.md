# ▼テスト中▼ 数独問題集アプリ — セットアップガイド

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
│       ├── data.sql                 # サンプルデータ（SQLite用）
│       └── templates/               # Thymeleafテンプレート
│           ├── index.html           # トップ（難易度選択）
│           ├── problems.html        # 問題表示（2問・縦並び）
│           ├── print.html           # 印刷プレビュー
│           ├── login.html           # ログイン
│           └── mypage.html          # 印刷履歴
│       └── static/css/
│           ├── style.css            # メインスタイル
│           └── print.css            # 印刷用スタイル
```

---

## セットアップ手順

### 1. データベースの準備（SQLite）

テーブルはSpring Boot起動時にHibernateが自動生成します。
問題データのINSERT文は手動で流す必要があります。

```bash
# INSERT文をSQLiteのDBファイルに流す
cat > /tmp/insert_data.sql << 'EOF'
INSERT OR IGNORE INTO users (username, password_hash) VALUES ('mama', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT OR IGNORE INTO users (username, password_hash) VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('EASY', '530070000600195000098000060800060003400803001700020006060000280000419005000080079', '534678912672195348198342567859761423426853791713924856961537284287419635345286179');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('EASY', '200080300060070084030500209000105408000000000402706000301007040720040060004010003', '214986375963271584837524209679135428518492637452768910391657842725843961146319753');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('EASY', '000000907000420180000705026100904000050000040000507009920108000034059000507000000', '462831957375429186198765326126943578853672491749517839926188743634259817517384962');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('EASY', '600120384008459072000006005000264030070080006036047000400800700003900200209710800', '671529384538459672492306815851264937724983156936147528465831279713695248289712863');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('EASY', '730000000000600800004000000060010000008007306000340000001830000070005000090000050', '738942561512637849394581627263719485158427396479356218621893174847265913985174352');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('MEDIUM', '000000000000003085001804000000070600080000030006900000000401690000050000008006000', '987654321246173985531894267329571648815246739674938512752489163463715892198362475');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('MEDIUM', '000075400000000008080190000300001060000000034000068170204000603900000020530200000', '693875412145632798782194356358941267269357834471268179824719635916483521537526984');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('MEDIUM', '430000000000000531000000000610000000000006900204000080000041650005700000000008002', '432985716978612531651347928613894275785236941294571386827149653345768219169528472');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('MEDIUM', '000801000000000043500000000000070800000000100020030000600000075003400000000200600', '239841567718659243546372918491765832372918154825034796964283475183497621657526389');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('MEDIUM', '700500000020070008005008000600000070000602000030000006000800500400030090000007004', '748523961321479658965138427612945873879612345534786216296841537485237192173569784');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('HARD', '800000000003600000070090200060005030004008010000006000052000009000419005000000800', '812753649943682175675491238168945837734128916521367482452836791387219564296574813');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('HARD', '000000000000003800000050070000020060080000400600000000500400030009000200000890000', '354197682672583814918254673741328965285619437693742518527461389439875261861932457');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('HARD', '000004000000000700700038000000010040006000050500000001000800000040000000091007003', '385694172612473798794538261239715846476829350568342917153876429847261530921057683');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('HARD', '000300000006000910000000000700009600020040000000017000050006002000000005400200000', '175398246836524917942761358713849625628453781584217639357986124261134895498275863');
INSERT OR IGNORE INTO sudoku_problems (difficulty, problem_data, answer_data) VALUES ('HARD', '000006000059000008200008000045000000003000600000003040006000075010040000000100200', '734956182659712438218348967845179326973284651162593847486921375317465829592137264');
EOF
sqlite3 sudoku.db < /tmp/insert_data.sql
```

データが正しく投入されたか確認します。

```bash
sqlite3 sudoku.db "SELECT difficulty, COUNT(*) FROM sudoku_problems GROUP BY difficulty;"
```

以下のように表示されれば成功です。

```
EASY|5
HARD|5
MEDIUM|5
```

### 2. application.properties の確認

SQLite用に以下の設定になっていることを確認してください。

```properties
spring.datasource.url=jdbc:sqlite:sudoku.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.properties.hibernate.dialect=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.thymeleaf.cache=false
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

## 公開・修正履歴
| 項目 | 内容 |
|var0.1------|------|
|ホームページ|公開テスト用の仮ページをアップロード|
|var0.2------|------|
|ホームページ|本番環境テスト用のwebページに差し替え|
| ランダム取得クエリ | `ORDER BY RAND()` → `ORDER BY RANDOM()` に変更 |
| 問題表示レイアウト | 横2列 → 縦1列に変更 |
| 空行の高さ | `height` 固定指定で空きマスのみの行が潰れる問題を修正 |
| 3×3区切り線 | 外枠と同じ太さ・色の太線に変更 |
| Thymeleaf構文 | `th:class` と `th:classappend` を分離してパースエラーを解消 |

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

---

## 問題データの形式

`sudoku_problems`テーブルの`problem_data`・`answer_data`は81文字のStringで管理します。
左上のマスから右下に向かって1文字ずつ格納し、`0`は空きマスを表します。

```
例）530070000600195000... （81文字）
    ↓
5 3 0 | 0 7 0 | 0 0 0
6 0 0 | 1 9 5 | 0 0 0
...
```
