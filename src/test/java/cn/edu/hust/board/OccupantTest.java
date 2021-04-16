package cn.edu.hust.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OccupantTest {

    /**
     * The unit under test.
     */
    private AbstractUnit abstractUnit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        abstractUnit = new BasicAbstractUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        assertThat(abstractUnit.hasSquare()).isFalse();
    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
        AbstractSquare target = new BasicAbstractSquare();
        abstractUnit.occupy(target);
        assertThat(abstractUnit.getAbstractSquare()).isEqualTo(target);
        assertThat(target.getOccupants()).contains(abstractUnit);
    }

    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
    @Test
    void testReoccupy() {
        AbstractSquare target = new BasicAbstractSquare();
        abstractUnit.occupy(target);
        abstractUnit.occupy(target);
        assertThat(abstractUnit.getAbstractSquare()).isEqualTo(target);
        assertThat(target.getOccupants()).contains(abstractUnit);
    }
}
