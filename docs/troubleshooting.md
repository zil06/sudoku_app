# トラブルシューティング記録

開発・デプロイ中に発生したエラーとその解決策をまとめています。

---

## 1. DBをMySQLからSQLiteに変更した際の修正

### 背景
本来練習用の想定(SQLite/無料)を用いた開発を予定していたが、実践用の想定(MySQL/有料)でソースコードを準備してしまっていたため、SQLiteに切り替え。

### 変更が必要だったファイル

**① SudokuProblemRepository.java**

```java
// 変更前（MySQL）
@Query(value = "SELECT * FROM sudoku_problems WHERE difficulty = :difficulty ORDER BY RAND() LIMIT 2",
       nativeQuery = true)

// 変更後（SQLite）
@Query(value = "SELECT * FROM sudoku_problems WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT 2",
       nativeQuery = true)
```

MySQLの`RAND()`はSQLiteでは`RANDOM()`に変更が必要。

**② data.sqlのDDL**

| 変更前（MySQL） | 変更後（SQLite） |
|---|---|
| `BIGINT AUTO_INCREMENT` | `INTEGER PRIMARY KEY AUTOINCREMENT` |
| `VARCHAR(50)` / `CHAR(81)` | `TEXT` |
| `CREATE DATABASE ...` / `USE ...` | 削除（SQLiteはファイル単位） |
| `CHARACTER SET utf8mb4` | 削除（デフォルトUTF-8） |

**③ pom.xml・application.properties**

MySQL用の設定からSQLite用に変更（詳細はREADMEのapplication.properties参照）。

---

## 2. INSERT文がうまく流れなかった問題

### エラー内容

```
Parse error near line 1: near "INSERT": syntax error
```

### 原因

`data.sql`のINSERT文が複数行にまたがって書かれていたため、`grep "^INSERT"`で抽出すると`VALUES`以降の行が取れず不完全なSQL文になっていた。

### 解決策

INSERT文を1行ずつ書き直したSQLファイルを作成して実行。

```bash
cat > /tmp/insert_data.sql << 'EOF'
INSERT OR IGNORE INTO sudoku_problems (...) VALUES (...);
...
EOF
sqlite3 sudoku.db < /tmp/insert_data.sql
```

---

## 3. Thymeleafのテンプレートパースエラー

### エラー内容

```
Could not parse as expression: "
    (${col == 2} or ${col == 5}) ? ' cell--border-right' : '' +
    (${row == 2} or ${row == 5}) ? ' cell--border-bottom' : ''
"
```

### 原因

`th:classappend`に複数条件を`+`でつなぐ書き方はThymeleafでは使えない。

### 解決策

`th:class`と`th:classappend`を分離し、三項演算子をネストして4パターンを1つの式で表現する。

```html
<td th:class="${val == '0'} ? 'cell cell--empty' : 'cell cell--given'"
    th:classappend="${(col == 2 or col == 5) and (row == 2 or row == 5)} ? ' cell--border-right cell--border-bottom' :
                   (${col == 2 or col == 5} ? ' cell--border-right' :
                   (${row == 2 or row == 5} ? ' cell--border-bottom' : ''))">
</td>
```

---

## 4. 空きマスのみの行の高さが潰れる問題

### 現象

横一列すべて空きマスの場合、該当行の上下幅が詰まって表示される。

### 原因

`aspect-ratio: 1`は中身が空のセルでは正しく機能しない場合がある。

### 解決策

`aspect-ratio`を削除して`height`を固定値で指定する。

```css
.sudoku-grid td.cell {
  width: 11.1%;
  height: 2.8rem;
  min-height: 2.8rem;
}
```

---

## 5. ログイン機能が動作しない問題

### 現象

正しいユーザー名・パスワードを入力しても「ユーザー名またはパスワードが正しくありません。」と表示される。

### 調査過程

デバッグログを追加して照合の状態を確認。

```java
System.out.println("DB取得ハッシュ: " + user.getPasswordHash());
System.out.println("照合結果: " + encoder.matches(password, user.getPasswordHash()));
```

結果：SQLの実行・ユーザー取得は正常、照合結果が`false`。

ログに以下のWARNが出ていた。

```
Encoded password does not look like BCrypt
```

### 原因

`AuthController.java`でBCryptPasswordEncoderをSpring管理外で`new`していたため、動作が不安定になっていた。また外部で生成したハッシュ値との互換性の問題もあった。

### 解決策

**① BCryptPasswordEncoderをBeanとして登録**

```java
// SudokuApplication.java
@Bean
public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**② AuthControllerでインジェクション**

```java
public AuthController(UserRepository userRepo, BCryptPasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.encoder = encoder;
}
```

**③ ハッシュをアプリ自身に生成させてDBに登録**

起動時にハッシュを生成してログに出力し、そのハッシュをそのままDBに投入する。外部ツールで生成したハッシュの流用は避ける。

---

## 6. GAE環境でログイン時の印刷プレビューが500エラーになる問題
### 現象

ローカル環境（ポート8080）：未ログイン・ログイン共に正常動作
本番環境（GAE）：未ログイン状態では正常、ログイン状態で印刷プレビューに遷移すると500エラー

### エラーログ

[SQLITE_READONLY] Attempt to write a readonly database
(attempt to write a readonly database)

### 原因
GAE（Google App Engine）のファイルシステムは読み取り専用のため、sudoku.dbへの書き込みが拒否される。
ログイン状態で印刷プレビューに遷移するとprint_historyテーブルへのINSERT（履歴保存）が実行されるが、未ログイン状態ではINSERTをスキップするため正常に表示できていた。

### 解決策（暫定対応）
SudokuController.javaの印刷履歴保存処理をコメントアウトして、INSERTをスキップ。
java// GAEのファイルシステムは読み取り専用のため、履歴保存機能を一時無効化して対処。

```java
// User user = (User) session.getAttribute("loginUser");
// if (user != null) {
// sudokuService.savePrintHistory(user, problems);
// }
```

印刷履歴保存機能を本番環境で動かすには、書き込み可能な有料DBへの移行が必要なため、修正は今後の課題として検討。
例：Google Cloud SQL