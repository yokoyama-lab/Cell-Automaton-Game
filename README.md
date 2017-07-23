# Cell-Automaton-Game

======================================================================\
セルオートマトンを用いたパズルゲーム
======================================================================　

### MainPrograme 
   * CellPattern.java　:(CAの例，今回は扱っていない)
   * Const.java        :(定数値を扱う)
   * GameFrame.java    :(フレーム、ボタン等の処理部分)
   * LifeCell.java     :(セルのクラス、遷移規則)
   * LifeGame.java     :(メインクラス)
   * Range.java        :(縦横比)
   * Mycallback.java   :(クリックした際回転する) 
   * makefile          :(上記のファイルを同時にコンパイルする) 

### 起動準備
  * Githubから本プログラムをダウンロード
  * ダウンロードしたディレクトリ内においてmakeコマンドでコンパイル
  * java LifeGame で実行

### 取扱説明
```javascript
 ライフゲーム的なパズル（仮）
 セルオートマトンを用いたパズルゲームである
 ・ ライフゲームのような規則で増殖する4色のセルを回転させてくっつけよう
 ・ 同じ色のセルを３つくっつけると消えるので、できるだけたくさんのセルを消そう
 ・ 内部のセルが一定以上に増えるとゲームオーバーになるので気をつけて

操作説明
 左クリック・・・右に回転
```

### 引用元
* [LifeGame](https://github.com/natmark/LifeGame)



以下の条件でご利用いただけます。\
 1)無料・無保証です。バグはないようにしたつもりですが、一切の責任は負えません。各自の責任においてご使用下さい。\
 2)再配布は自由に行って下さい。改造したものの配布は、製作者までご相談下さい。\
 3)商用利用の際は、事前に製作者までご相談下さい。
