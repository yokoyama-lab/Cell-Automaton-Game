# Cell-Automaton-Game

セルオートマトン（ライフゲーム派生）を用いたパズルゲーム。Java Swing 製。

> 学部3年生の演習課題として 2016 年〜開発。原型は [natmark/LifeGame](https://github.com/natmark/LifeGame)。

## 動作要件

- **JDK 11 以上**（開発・検証は JDK 21 で実施）。`javac`（コンパイラ）が必要。JRE だけではビルドできない。
  - Ubuntu/Debian: `sudo apt install openjdk-21-jdk-headless`
- `make`（GNU make）
- テスト実行には `lib/junit-platform-console-standalone.jar`（JUnit 5）が必要。

## ビルド・実行・テスト

GitHub からクローン後、リポジトリのルートで以下を実行する。

```bash
make        # src/*.java を bin/ にコンパイル
make run    # ゲームを起動（java -cp bin LifeGame）
make test   # JUnit テストをヘッドレスで実行
make clean  # 生成された .class（bin/・test/classes/）を削除
```

### 実行方法

```bash
make          # 初回のみコンパイル
make run      # ウィンドウが開きゲーム開始
```

`make run` は内部で `java -cp bin LifeGame` を呼ぶ。GUI（Swing）を使うため、表示環境（X11 等）が必要。

### テスト方法

```bash
make test     # 全テストを実行（RangeTest / LifeCellTest / ConstTest）
```

`make test` は対象クラスを `test/classes/` にコンパイルし、`lib/junit-platform-console-standalone.jar`（JUnit 5）で `-Djava.awt.headless=true` を付けてヘッドレス実行する。成功すると次のように全件パスする。

```
[        N tests successful      ]
[        0 tests failed          ]
```

特定のテストクラスだけを走らせたい場合は、`make test` でコンパイル済みの `test/classes/` を使って次のように指定する。

```bash
java -Djava.awt.headless=true -jar lib/junit-platform-console-standalone.jar \
    --class-path test/classes --select-class RangeTest
```

## ソース構成（`src/`）

| ファイル | 役割 |
|----------|------|
| `LifeGame.java`   | メインクラス（エントリポイント） |
| `GameFrame.java`  | フレーム・ボタン・世代更新ループの処理 |
| `LifeCell.java`   | セルのクラスと遷移規則 |
| `Mycallback.java` | クリック時にセルを回転させるコールバック |
| `Const.java`      | 定数（盤面サイズ・更新間隔・ルール値） |
| `Range.java`      | 閉区間の判定ユーティリティ（縦横比） |
| `CellPattern.java`| CA の例（現バージョンでは未使用） |

テストは `test/`（`LifeCellTest` / `RangeTest` / `ConstTest`）。GUI 非依存のロジック（遷移規則・区間判定・定数の不変条件）を対象とする。実行手順は上記「テスト方法」を参照。

## 取扱説明

### セルの状態

| 状態 | 色 | 意味 |
|------|----|------|
| 0 | 白 | 死亡 |
| 1 | 黄 | 生存 |
| 2 | 赤 | 生存 |
| 3 | 緑 | 生存 |
| 4 | 青 | 生存 |

### 世代交代ルール（約 1.5 秒ごとに自動更新）

**死亡セルの誕生：**
- 8 近傍の生存セル合計がちょうど 3 個 → 誕生
- 誕生する状態は `(近傍の重み合計 % 4) + 1` で決まる

**生存セルの死亡（スコア +1）：**
- 上下左右に同色セルが 2 個以上隣接する → 死亡
- 同色セルが 1 個で、その延長線上にも同色が続く → 死亡
- それ以外 → 生存

**外周：**
- 外周セルは毎世代ランダムに状態 1〜4 がセットされる（燃料供給）

### 操作

| 操作 | 効果 |
|------|------|
| セルを左クリック（空の 2×2 ブロック） | 黄色セル（状態 1）を配置 |
| セルを左クリック（生存セルがある 2×2 ブロック） | 2×2 ブロックを時計回りに回転 |
| スタートボタン | 世代更新の開始／停止 |
| リセットボタン | 全セルを死亡状態に初期化 |

### ゲームオーバー

生存セル数が **120 個を超える** とゲームオーバー。できるだけ多くのセルを消してスコアを稼ごう。

## 引用元

- [LifeGame](https://github.com/natmark/LifeGame)

## ライセンス・利用条件

以下の条件でご利用いただけます。

1. 無料・無保証です。バグはないようにしたつもりですが、一切の責任は負えません。各自の責任においてご使用下さい。
2. 再配布は自由に行って下さい。改造したものの配布は、製作者までご相談下さい。
3. 商用利用の際は、事前に製作者までご相談下さい。
