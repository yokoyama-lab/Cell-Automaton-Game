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
}
