"use strict";
/**
 * Cell-Automaton-Game — TypeScript + Canvas 版
 *
 * Java(Swing) 版の盤面・遷移規則・ゲーム性（難易度進行 / コンボ / ハイスコア /
 * 色選択 / リスタート）をブラウザに移植したもの。セル1個=JButton ではなく
 * 1枚の Canvas に直接描画するため軽量で、滑らかに動く。
 */
// ===== 定数（Java の Const.java 相当） =====
const COLS = 14;
const ROWS = 14;
const CELL = 36; // セル描画サイズ（正方形の一辺）
const GAP = 1; // セル間の隙間（黒背景でグリッドに見せる）
const STRIDE = CELL + GAP; // セルの配置間隔
const SLEEP_TIME_MS = 1500; // レベル1での更新間隔（基準値）
const MIN_SLEEP_TIME_MS = 300; // 更新間隔の下限
const SLEEP_STEP_MS = 100; // レベルごとに短縮する量
const SCORE_PER_LEVEL = 50; // この得点ごとにレベル +1
const MAX_COMBO_MULT = 5; // 同時消しコンボ倍率の上限
const GAMEOVER_LIMIT = 120; // 生存セルがこれを超えるとゲームオーバー
const BIRTH_CNT = 3; // 誕生に必要な近傍の生存数
const HIGHSCORE_KEY = "cellautomaton_highscore";
// 状態 0..4 に対応する色（0=死亡=白, 1=黄, 2=赤, 3=緑, 4=青）
const COLORS = ["#ffffff", "#ffd400", "#ff3b30", "#34c759", "#0a84ff"];
const COLOR_NAMES = ["", "黄", "赤", "緑", "青"];
// ===== ゲーム本体 =====
class Game {
    constructor() {
        this.score = 0;
        this.highScore = 0;
        this.level = 1;
        this.comboMult = 1;
        this.placeColor = 1; // 配置するセルの色 (1..4)
        this.running = false;
        this.gameover = false;
        this.living = 0;
        this.grid = Game.makeGrid();
        this.next = Game.makeGrid();
        this.highScore = this.loadHighScore();
    }
    static makeGrid() {
        const g = [];
        for (let y = 0; y < ROWS; y++)
            g.push(new Array(COLS).fill(0));
        return g;
    }
    loadHighScore() {
        try {
            const v = window.localStorage.getItem(HIGHSCORE_KEY);
            return v ? parseInt(v, 10) || 0 : 0;
        }
        catch (_a) {
            return 0;
        }
    }
    saveHighScore() {
        try {
            window.localStorage.setItem(HIGHSCORE_KEY, String(this.highScore));
        }
        catch (_a) {
            /* localStorage 不可でも致命的ではない */
        }
    }
    /** 現在のレベルに応じた更新間隔（下限あり） */
    currentSleepMs() {
        return Math.max(MIN_SLEEP_TIME_MS, SLEEP_TIME_MS - (this.level - 1) * SLEEP_STEP_MS);
    }
    static inBounds(x, y) {
        return x >= 0 && x < COLS && y >= 0 && y < ROWS;
    }
    /** 盤外は死亡(0)として扱う近傍参照 */
    at(x, y) {
        return Game.inBounds(x, y) ? this.grid[y][x] : 0;
    }
    static isCorner(x, y) {
        return (x === 0 || x === COLS - 1) && (y === 0 || y === ROWS - 1);
    }
    static isBorder(x, y) {
        return x === 0 || x === COLS - 1 || y === 0 || y === ROWS - 1;
    }
    /**
     * 1 世代進める。消えたセル数を返す。
     * 規則は Java 版 LifeCell.checkSurroundings / GameFrame の世代ループに準拠。
     */
    step() {
        let kills = 0;
        for (let y = 0; y < ROWS; y++) {
            for (let x = 0; x < COLS; x++) {
                const self = this.grid[y][x];
                if (self === 0) {
                    // --- 誕生：8近傍の生存数がちょうど BIRTH_CNT のとき ---
                    let count = 0;
                    let weighted = 0;
                    for (let dy = -1; dy <= 1; dy++) {
                        for (let dx = -1; dx <= 1; dx++) {
                            if (dx === 0 && dy === 0)
                                continue;
                            const v = this.at(x + dx, y + dy);
                            if (v >= 1 && v <= 4) {
                                count++;
                                weighted += v;
                            }
                        }
                    }
                    this.next[y][x] = count === BIRTH_CNT ? (weighted % 4) + 1 : 0;
                }
                else {
                    // --- 死亡判定：同色の上下左右隣接で決まる ---
                    const same = (dx, dy) => this.at(x + dx, y + dy) === self;
                    const up = same(0, -1);
                    const right = same(1, 0);
                    const left = same(-1, 0);
                    const down = same(0, 1);
                    const cnt = (up ? 1 : 0) + (right ? 1 : 0) + (left ? 1 : 0) + (down ? 1 : 0);
                    let dies = false;
                    if (cnt >= 2) {
                        // 同色が2方向以上 → 死亡
                        dies = true;
                    }
                    else if (cnt === 1) {
                        // 同色が1方向 + その延長/角に同色が続く → 死亡
                        if (up && (same(-1, -1) || same(0, -2) || same(1, -1)))
                            dies = true;
                        else if (left && (same(-1, -1) || same(-2, 0) || same(-1, 1)))
                            dies = true;
                        else if (right && (same(1, -1) || same(2, 0) || same(1, 1)))
                            dies = true;
                        else if (down && (same(-1, 1) || same(1, 1) || same(0, 2)))
                            dies = true;
                    }
                    if (dies) {
                        this.next[y][x] = 0;
                        kills++;
                    }
                    else {
                        this.next[y][x] = self;
                    }
                }
            }
        }
        // --- 外周の上書き（燃料供給）：角は消去、それ以外はランダム 1..4 ---
        for (let y = 0; y < ROWS; y++) {
            for (let x = 0; x < COLS; x++) {
                if (!Game.isBorder(x, y))
                    continue;
                this.next[y][x] = Game.isCorner(x, y) ? 0 : (Math.floor(Math.random() * 4) + 1);
            }
        }
        // --- 同時消しコンボでスコア加算 ---
        if (kills > 0) {
            this.comboMult = Math.min(kills, MAX_COMBO_MULT);
            this.score += kills * this.comboMult;
        }
        else {
            this.comboMult = 1;
        }
        this.level = 1 + Math.floor(this.score / SCORE_PER_LEVEL);
        // --- 世代交代＆生存数カウント ---
        const tmp = this.grid;
        this.grid = this.next;
        this.next = tmp;
        this.living = 0;
        for (let y = 0; y < ROWS; y++) {
            for (let x = 0; x < COLS; x++) {
                if (this.grid[y][x] > 0)
                    this.living++;
            }
        }
        if (this.living > GAMEOVER_LIMIT) {
            this.gameover = true;
            if (this.score > this.highScore) {
                this.highScore = this.score;
                this.saveHighScore();
            }
        }
    }
    /** クリックされたセル(x,y)を起点に 2×2 ブロックを操作 */
    spinCells(x, y) {
        if (this.gameover)
            return;
        // 端と最後の2列/2行は操作不可（Java 版と同じ可動域 x,y ∈ 1..11）
        if (!(x >= 1 && y >= 1 && x < COLS - 2 && y < ROWS - 2))
            return;
        const g = this.grid;
        const empty = g[y][x] === 0 && g[y + 1][x] === 0 && g[y][x + 1] === 0 && g[y + 1][x + 1] === 0;
        if (empty) {
            // 空の 2×2 → 選択色を1つ配置
            g[y][x] = this.placeColor;
        }
        else {
            // 生存セルあり → 2×2 を時計回りに回転
            const temp = g[y][x];
            g[y][x] = g[y + 1][x];
            g[y + 1][x] = g[y + 1][x + 1];
            g[y + 1][x + 1] = g[y][x + 1];
            g[y][x + 1] = temp;
        }
    }
    reset() {
        this.grid = Game.makeGrid();
        this.next = Game.makeGrid();
        this.score = 0;
        this.level = 1;
        this.comboMult = 1;
        this.living = 0;
        this.gameover = false;
    }
    cycleColor() {
        this.placeColor = (this.placeColor % 4) + 1;
    }
    cellAt(y, x) {
        return this.grid[y][x];
    }
}
// ===== 描画・入力・ループ（DOM 配線） =====
class App {
    constructor() {
        this.game = new Game();
        this.loop = () => {
            if (this.game.running && !this.game.gameover) {
                this.game.step();
                this.render();
            }
            const delay = this.game.running && !this.game.gameover ? this.game.currentSleepMs() : 100;
            window.setTimeout(this.loop, delay);
        };
        this.canvas = document.getElementById("board");
        this.canvas.width = COLS * STRIDE - GAP;
        this.canvas.height = ROWS * STRIDE - GAP;
        this.ctx = this.canvas.getContext("2d");
        this.startBtn = document.getElementById("startBtn");
        this.colorBtn = document.getElementById("colorBtn");
        const resetBtn = document.getElementById("resetBtn");
        this.statusTop = document.getElementById("statusTop");
        this.statusBottom = document.getElementById("statusBottom");
        this.startBtn.addEventListener("click", () => this.toggleRun());
        resetBtn.addEventListener("click", () => {
            this.game.reset();
            this.render();
        });
        this.colorBtn.addEventListener("click", () => {
            this.game.cycleColor();
            this.updateColorBtn();
        });
        this.canvas.addEventListener("click", (e) => this.onClick(e));
        // キーボード：Space=開始/停止, R=リセット
        window.addEventListener("keydown", (e) => {
            if (e.code === "Space") {
                e.preventDefault();
                this.toggleRun();
            }
            else if (e.key === "r" || e.key === "R") {
                this.game.reset();
                this.render();
            }
        });
        this.updateColorBtn();
        this.render();
        this.loop();
    }
    toggleRun() {
        this.game.running = !this.game.running;
        this.startBtn.textContent = this.game.running ? "ストップ" : "スタート";
    }
    updateColorBtn() {
        this.colorBtn.textContent = "色：" + COLOR_NAMES[this.game.placeColor];
    }
    onClick(e) {
        const rect = this.canvas.getBoundingClientRect();
        // CSS 拡大に対応するため実ピクセルへ換算
        const sx = this.canvas.width / rect.width;
        const sy = this.canvas.height / rect.height;
        const px = (e.clientX - rect.left) * sx;
        const py = (e.clientY - rect.top) * sy;
        const x = Math.floor(px / STRIDE);
        const y = Math.floor(py / STRIDE);
        // 範囲チェック（可動域）は spinCells 内で行う
        this.game.spinCells(x, y);
        this.render();
    }
    render() {
        const g = this.game;
        const ctx = this.ctx;
        // 背景（黒）→ セルを敷き詰め、隙間が黒で残りグリッドに見える
        ctx.fillStyle = "#000000";
        ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
        for (let y = 0; y < ROWS; y++) {
            for (let x = 0; x < COLS; x++) {
                ctx.fillStyle = COLORS[g.cellAt(y, x)];
                ctx.fillRect(x * STRIDE, y * STRIDE, CELL, CELL);
            }
        }
        // ゲームオーバー時のオーバーレイ
        if (g.gameover) {
            ctx.fillStyle = "rgba(0,0,0,0.65)";
            ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
            ctx.fillStyle = "#ffffff";
            ctx.textAlign = "center";
            ctx.font = "bold 40px sans-serif";
            ctx.fillText("GAME OVER", this.canvas.width / 2, this.canvas.height / 2 - 20);
            ctx.font = "18px sans-serif";
            ctx.fillText(`スコア ${g.score}  /  最高 ${g.highScore}`, this.canvas.width / 2, this.canvas.height / 2 + 16);
            ctx.fillText("リセット（R）で再挑戦", this.canvas.width / 2, this.canvas.height / 2 + 44);
        }
        // HUD テキスト更新
        this.statusTop.textContent = `スコア ${g.score}　最高 ${g.highScore}　Lv.${g.level}`;
        const combo = g.comboMult > 1 ? `　コンボ x${g.comboMult}` : "";
        this.statusBottom.textContent = g.gameover
            ? `GAME OVER　スコア ${g.score} / 最高 ${g.highScore}`
            : `セルの数 ${g.living}/${GAMEOVER_LIMIT}${combo}`;
    }
}
window.addEventListener("DOMContentLoaded", () => new App());
