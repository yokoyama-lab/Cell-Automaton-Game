import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 盤面寸法やルール定数の不変条件を固定するテスト。
 * 値を変更したときに前提（CELL_SIZE が辺の約数 等）が崩れないことを検出する。
 */
class ConstTest {

    @Test
    void cellSizeDividesFrameWidthAndHeight() {
        assertEquals(0, Const.FRAME_SIZE.width % Const.CELL_SIZE,
            "CELL_SIZE はフレーム幅の約数である必要がある（setCells が例外を投げる）");
        assertEquals(0, Const.FRAME_SIZE.height % Const.CELL_SIZE,
            "CELL_SIZE はフレーム高さの約数である必要がある");
    }

    @Test
    void birthCountIsThree() {
        // 誕生は生存近傍ちょうど 3 個（標準ライフゲーム由来）
        assertEquals(3, Const.BIRTH_CNT);
    }

    @Test
    void livingRangeIncludesTwoAndThree() {
        assertTrue(Const.LIVING_RANGE.includes(2));
        assertTrue(Const.LIVING_RANGE.includes(3));
        assertFalse(Const.LIVING_RANGE.includes(1));
        assertFalse(Const.LIVING_RANGE.includes(4));
    }

    @Test
    void sleepTimeIsPositive() {
        assertTrue(Const.SLEEP_TIME_MS > 0, "更新間隔は正の値");
    }
}
