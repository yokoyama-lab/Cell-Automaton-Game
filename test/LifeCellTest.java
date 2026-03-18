import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class LifeCellTest {

    private LifeCell dummy;

    @BeforeAll
    static void setHeadless() {
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void setUp() {
        dummy = new LifeCell();
        dummy.isLiving = 0;
    }

    // --- generationalChange ---

    @Test
    void generationalChange_setsIsLivingFromWillLiving() {
        LifeCell cell = new LifeCell();
        cell.isLiving = 0;
        cell.outsideChange();  // sets willLiving = 1..4
        cell.generationalChange();
        assertTrue(cell.isLiving >= 1 && cell.isLiving <= 4,
            "After generationalChange, isLiving should be 1-4 (was set by outsideChange)");
    }

    @Test
    void generationalChange_afterOutsideClear_isLivingBecomesZero() {
        LifeCell cell = new LifeCell();
        cell.isLiving = 2;
        cell.outsideClear();  // sets willLiving = 0
        cell.generationalChange();
        assertEquals(0, cell.isLiving);
    }

    // --- forceKillAll ---

    @Test
    void forceKillAll_setsAllCellsToZero() {
        ArrayList<LifeCell> cells = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LifeCell c = new LifeCell();
            c.isLiving = i % 4 + 1;
            cells.add(c);
        }
        LifeCell.forceKillAll(cells);
        for (LifeCell c : cells) {
            assertEquals(0, c.isLiving, "All cells should be dead after forceKillAll");
        }
    }

    // --- checkSurroundings: birth rule ---

    @ParameterizedTest(name = "{0} neighbors -> born={1}")
    @CsvSource({"3, true", "2, false", "4, false"})
    void checkSurroundings_deadCellBirthRules(int neighborCount, boolean expectsBorn) {
        LifeCell center = new LifeCell();
        center.isLiving = 0;

        for (int i = 0; i < neighborCount; i++) {
            LifeCell neighbor = new LifeCell();
            neighbor.isLiving = 1;
            center.addSurroundings(neighbor);
        }
        setupSurroundings2(center);

        center.checkSurroundings();
        center.generationalChange();

        if (expectsBorn) {
            assertTrue(center.isLiving >= 1 && center.isLiving <= 4,
                "Dead cell with " + neighborCount + " neighbors should be born");
        } else {
            assertEquals(0, center.isLiving,
                "Dead cell with " + neighborCount + " neighbors should stay dead");
        }
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
