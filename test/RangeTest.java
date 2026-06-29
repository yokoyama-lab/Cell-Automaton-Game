import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RangeTest {

    @Test
    void includes_valueWithinRange_returnsTrue() {
        Range<Integer> range = new Range<>(2, 3);
        assertTrue(range.includes(2));
        assertTrue(range.includes(3));
    }

    @Test
    void includes_valueBelowRange_returnsFalse() {
        Range<Integer> range = new Range<>(2, 3);
        assertFalse(range.includes(1));
    }

    @Test
    void includes_valueAboveRange_returnsFalse() {
        Range<Integer> range = new Range<>(2, 3);
        assertFalse(range.includes(4));
    }

    @Test
    void includes_singleValueRange_exact() {
        Range<Integer> range = new Range<>(5, 5);
        assertTrue(range.includes(5));
        assertFalse(range.includes(4));
        assertFalse(range.includes(6));
    }

    @Test
    void includes_stringRange() {
        Range<String> range = new Range<>("a", "c");
        assertTrue(range.includes("b"));
        assertFalse(range.includes("d"));
    }

    @Test
    void includes_lowerAndUpperBoundsAreInclusive() {
        Range<Integer> range = new Range<>(0, 10);
        assertTrue(range.includes(0), "下限は含む");
        assertTrue(range.includes(10), "上限は含む");
        assertFalse(range.includes(-1));
        assertFalse(range.includes(11));
    }

    @Test
    void includes_invertedBounds_neverIncludes() {
        // 下限 > 上限 の場合は常に空区間扱い
        Range<Integer> range = new Range<>(3, 2);
        assertFalse(range.includes(2));
        assertFalse(range.includes(3));
    }
}
