# 数独問題集アプリ (Sudoku App)
知人のお母様の「複数問題をまとめて印刷したい」という悩みを解消するために開発した、数独問題の管理・印刷Webアプリケーションです。

## 公開URL
https://javasilvertest.an.r.appspot.com/sudoku

## プロジェクト概要
既存の数独サイトでは難しかった「複数問題の一括印刷」に特化しています。Java Silverで培ったオブジェクト指向の知識と、Spring Bootによる効率的なWeb開発を組み合わせて構築しています。

## 使用技術
### Backend
- Java 21 (LTS)
- Spring Boot 3.x
- Spring Data JPA
- Spring Security (認証・認可)
- JUnit 5 (単体テスト)

### Frontend
- Thymeleaf (テンプレートエンジン)
- Bootstrap 5 (UIフレームワーク)
- CSS @media print (印刷レイアウト制御)

### Database / Infra
- SQLite (開発・MVP運用)
- Google App Engine (GAE)
- GitHub Actions (CI/CD 拡張予定)

## 主な機能
- **難易度別ランダム出題**: 初級・中級・上級から問題をランダムに抽出
- **印刷最適化レイアウト**: 1ページ2問の固定配置と、解答まとめページの自動生成
- **ユーザー認証**: 印刷履歴保存のためのログイン機能
- **管理者機能（実装予定）**: 問題データの追加・編集・削除(CRUD)

## アーキテクチャ
- **3層アーキテクチャ**: Controller / Service / Repository の分離による保守性の確保
- **セキュリティ**: パスワードのBCryptハッシュ化、CSRF対策の実施

## ✒️ Author
zil06 ([GitHub](https://github.com/zil06))
