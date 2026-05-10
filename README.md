# 数独問題集アプリ (Sudoku App)

知人のお母様の「複数問題をまとめて印刷したい」という悩みを解消するために開発した、数独問題の管理・印刷用Webアプリケーションです。

## 公開URL
https://javasilvertest.an.r.appspot.com/

## 開発記録
 [docs/ ディレクトリ](./docs/) 参照
- [要件定義書](./docs/requirements-definition.md)
- [遭遇したエラーと対処の記録](./docs/troubleshooting.md)
- [開発日誌](./docs/dev-log.md)
- [動作テスト記録](./docs/testing-memo.md)

---

## プロジェクト概要

対象者が利用中の数独サイトでは難しかった、「複数問題の一括印刷」に対応。
AI等のツールを活用しつつ、Java Silver試験で培ったプログラミング知識と、Spring BootによるWeb開発を組み合わせて構築しています。

## 使用技術

### Backend
- Java 17
- Spring Boot 4.0.5
- Spring Data JPA

### Frontend
- Thymeleaf (テンプレートエンジン)
- CSS @media print (印刷レイアウト制御)

### Database / Infra
- SQLite
- Google App Engine (GAE)

## 主な機能

- **問題の自動生成** — 数独生成スクリプトによる問題の自動生成(各難易度500問・計1500問)
- **難易度別ランダム出題** — 初級・中級・上級から、問題をランダムに抽出
- **印刷最適化レイアウト** — 1ページ2問の縦配置と、ページ末尾の解答まとめ自動生成機能

## アーキテクチャ

- **3層アーキテクチャ** — Controller / Service / Repository の分離による保守性の確保

---

## 画面遷移

```
/ (トップ)
 └─ 難易度選択 → /problems?difficulty=EASY|MEDIUM|HARD
                  └─ 印刷プレビュー → /print
                                       └─ window.print()
```

---

## 構成ファイル一覧

```
sudoku/
├── app.yaml                         # GAEデプロイ設定
├── pom.xml                          # Maven依存関係
├── sudoku.db                        # SQLiteデータベース
├── README.md                        # プロジェクト説明書
├── src/
│   ├──main/
│   │   ├── java/com/sudoku/
│   │   │   ├── SudokuApplication.java     # 起動クラス
│   │   │   ├── controller/
│   │   │   │   └── SudokuController.java  # 問題表示・印刷
│   │   │   ├── model/
│   │   │   │   └── SudokuProblem.java     # 問題エンティティ
│   │   │   ├── repository/
│   │   │   │   └── SudokuProblemRepository.java  # 問題表示
│   │   │   └── service/
│   │   │       └── SudokuService.java     # ビジネスロジック(問題抽出)
│   │   └── resources/
│   │       ├── application.properties     # SQLite・JPA設定
│   │       ├── data.sql                   # ブラウザ表示用の数独問題データ
│   │       └── templates/
│   │           ├── index.html             # トップ（難易度選択）
│   │           ├── problems.html          # 問題表示
│   │           └── print.html             # 印刷プレビュー
│   │       └── static/css/
│   │           ├── style.css              # メインスタイル
│   │           └── print.css              # 印刷用スタイル
│   └── test/java/com/sudoku/
│       └── SudokuGenerator.java   # 数独問題自動生成スクリプト
└── docs/
    ├── requirements-definition  # 要件定義書
    ├── troubleshooting.md  # 遭遇したエラーと対処の記録
    ├── dev-log.md          # 開発日誌
    └── testing-memo.md     # 動作テスト記録

```
---

## 本プロジェクトの要件定義について

クライアントへのヒアリング結果に基づき、初期の要件定義（docs/requirements-definition.md）に含まれていた追加検討機能(ログイン機能・印刷管理機能等)を除外。
上記に伴い、要望に合わせて各要件を最適化。

---

## 今後の拡張検討項目

1. **ユーザー認証・履歴管理** — ログイン機能による「一度印刷した問題」の除外(重複問題の回避)機能の実装検討
※現時点では問題文の母数確保により代替対応
2. **問題の自動生成** — アクセスのたびにバックエンドで、新規数独問題を生成して表示する方式に移行検討
※現時点では事前に自動生成した問題を、データベースに収録して表示
※唯一解の保証アルゴリズム実装後に対応検討

---

## ✒️ Author

菅原 志朗 (Shiro Sugawara)
zil06 ([GitHub](https://github.com/zil06))