import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class LifeCellTest {

    // 死亡（状態0）の placeholder セル。変更されないのでクラス内で1つ共有する。
    private static LifeCell dummy;

    @BeforeAll
    static void setUp() {
        System.setProperty("java.awt.headless", "true");
        dummy = new LifeCell(); // isLiving は構築時に 0
    }

    // --- generationalChange ---

    @Test
    void generationalChange_setsIsLivingFromWillLiving() {
        LifeCell cell = new LifeCell();
        cell.outsideChange();  // sets willLiving = 1..4
        cell.generationalChange();
        assertTrue(cell.isLiving >= 1 && cell.isLiving <= 4,
            "After generationalChange, isLiving should be 1-4 (was set by outsideChange)");
    }

    @Test
    void generationalChange_afterOutsideClear_isLivingBecomesZero() {
        LifeCell cell = living(2);
        cell.outsideClear();  // sets willLiving = 0
        cell.generationalChange();
        assertEquals(0, cell.isLiving);
    }

    @Test
    void generationalChange_resetsWillLivingToZeroEachTick() {
        // 一度 willLiving を立てて適用したら、次の generationalChange では 0 に戻る
        LifeCell cell = new LifeCell();
        cell.outsideChange();      // willLiving = 1..4
        cell.generationalChange(); // isLiving = 1..4, willLiving -> 0
        cell.generationalChange(); // 何も供給がなければ isLiving は 0 に戻るはず
        assertEquals(0, cell.isLiving,
            "willLiving は適用後に 0 へリセットされるため、供給が無ければ次世代で死亡する");
    }

    @Test
    void getisLiving_reflectsCurrentState() {
        assertEquals(3, living(3).getisLiving());
    }

    // --- outsideChange / outsideClear ---

    @RepeatedTest(10)
    void outsideChange_alwaysProducesStateBetween1And4() {
        LifeCell cell = new LifeCell();
        cell.outsideChange();
        cell.generationalChange();
        assertTrue(cell.isLiving >= 1 && cell.isLiving <= 4,
            "外周供給は常に状態 1..4 を与える（0 や 5 以上は出ない）");
    }

    // --- forceKillAll ---

    @Test
    void forceKillAll_setsAllCellsToZero() {
        ArrayList<LifeCell> cells = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cells.add(living(i % 4 + 1));
        }
        LifeCell.forceKillAll(cells);
        for (LifeCell c : cells) {
            assertEquals(0, c.isLiving, "All cells should be dead after forceKillAll");
        }
    }

    @Test
    void forceKillAll_onEmptyList_doesNotThrow() {
        assertDoesNotThrow(() -> LifeCell.forceKillAll(new ArrayList<>()));
    }

    // --- checkSurroundings: birth rule (count) ---

    @ParameterizedTest(name = "{0} neighbors -> born={1}")
    @CsvSource({"3, true", "2, false", "4, false", "0, false"})
    void checkSurroundings_deadCellBirthRules(int neighborCount, boolean expectsBorn) {
        LifeCell center = new LifeCell();
        for (int i = 0; i < neighborCount; i++) {
            center.addSurroundings(living(1));
        }
        setupSurroundings2(center);

        int score = tick(center);

        assertEquals(0, score, "誕生は消去ではないのでスコアは増えない");
        if (expectsBorn) {
            assertTrue(center.isLiving >= 1 && center.isLiving <= 4,
                "Dead cell with " + neighborCount + " neighbors should be born");
        } else {
            assertEquals(0, center.isLiving,
                "Dead cell with " + neighborCount + " neighbors should stay dead");
        }
    }

    // --- checkSurroundings: birth rule (color = (重み合計 % 4) + 1) ---

    @ParameterizedTest(name = "3 neighbors of states {0} -> color {1}")
    @CsvSource({
        "'1,1,1', 4",   // 重み合計 3 -> 3%4+1 = 4 (青)
        "'2,2,2', 3",   // 重み合計 6 -> 6%4+1 = 3 (緑)
        "'4,4,4', 1",   // 重み合計 12 -> 12%4+1 = 1 (黄)
        "'1,2,3', 3",   // 重み合計 6 -> 3 (緑)
        "'1,1,2', 1"    // 重み合計 4 -> 4%4+1 = 1 (黄)
    })
    void checkSurroundings_birthColorFromWeightedSum(String statesCsv, int expectedColor) {
        LifeCell center = new LifeCell();
        for (String s : statesCsv.split(",")) {
            center.addSurroundings(living(Integer.parseInt(s.trim())));
        }
        setupSurroundings2(center);

        tick(center);

        assertEquals(expectedColor, center.isLiving,
            "誕生する色は近傍の重み付き合計に依存する");
    }

    // --- checkSurroundings: death rule (surroundings2 の上下左右) ---

    @Test
    void checkSurroundings_twoOrthogonalSameColor_dies() {
        LifeCell center = newLivingWithDiamond(1);
        setS2(center, -1, 0, living(1)); // 上が同色
        setS2(center,  0, 1, living(1)); // 右が同色 -> 同色直交が2つ

        int score = tick(center);

        assertEquals(0, center.isLiving, "同色の直交隣接が2つ以上 -> 死亡");
        assertEquals(1, score, "セルが1つ消えたのでスコア +1");
    }

    @Test
    void checkSurroundings_isolatedLivingCell_survives() {
        LifeCell center = newLivingWithDiamond(1); // 周囲は全て死亡
        int score = tick(center);

        assertEquals(1, center.isLiving, "同色隣接が無ければ生存");
        assertEquals(0, score, "消えていないのでスコアは増えない");
    }

    @Test
    void checkSurroundings_oneSameColorWithExtension_dies() {
        // 同色直交が1つ（上）＋ その延長上にも同色 -> 死亡
        LifeCell center = newLivingWithDiamond(1);
        setS2(center, -1, 0, living(1)); // 上
        setS2(center, -2, 0, living(1)); // 上の延長（真上の2マス目）

        int score = tick(center);

        assertEquals(0, center.isLiving, "同色1つ＋延長線上に同色 -> 死亡");
        assertEquals(1, score);
    }

    @Test
    void checkSurroundings_oneSameColorNoExtension_survives() {
        // 同色直交が1つ（上）だが延長上に同色なし -> 生存
        LifeCell center = newLivingWithDiamond(1);
        setS2(center, -1, 0, living(1)); // 上だけ同色

        int score = tick(center);

        assertEquals(1, center.isLiving, "同色1つで延長が無ければ生存");
        assertEquals(0, score);
    }

    @Test
    void checkSurroundings_differentColorNeighbors_doNotKill() {
        // 直交隣接は埋まっているが全て別色 -> 生存
        LifeCell center = newLivingWithDiamond(1);
        setS2(center, -1, 0, living(2));
        setS2(center,  0, 1, living(3));
        setS2(center,  0, -1, living(4));
        setS2(center,  1, 0, living(2));

        int score = tick(center);

        assertEquals(1, center.isLiving, "別色は死亡判定に影響しない");
        assertEquals(0, score);
    }

    // ---------- helpers ----------

    // checkSurroundings（次世代の判定）→ generationalChange（適用）を1ティックとして実行し、スコアを返す
    private int tick(LifeCell cell) {
        int score = cell.checkSurroundings();
        cell.generationalChange();
        return score;
    }

    // 生存状態 state のセルを作る
    private LifeCell living(int state) {
        LifeCell c = new LifeCell();
        c.isLiving = state;
        return c;
    }

    // 中心セル（生存・全 surroundings2 をいったん死亡ダミーで埋める）を作る
    private LifeCell newLivingWithDiamond(int state) {
        LifeCell center = living(state);
        setupSurroundings2(center);
        return center;
    }

    // surroundings2[dy+2][dx+2] に cell を割り当てる（既存ダミーを上書き）
    private void setS2(LifeCell cell, int dy, int dx, LifeCell neighbor) {
        cell.addSurroundings2(neighbor, dy + 2, dx + 2, 1);
    }

    // Helper: populate surroundings2 with dead dummy cells so checkSurroundings won't NPE
    private void setupSurroundings2(LifeCell cell) {
        for (int dy = -2; dy <= 2; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                if (dx == 0 && dy == 0) continue;
                if (Math.abs(dx) + Math.abs(dy) <= 2) {
                    cell.addSurroundings2(dummy, dy + 2, dx + 2, 1);
                }
            }
        }
    }
}
