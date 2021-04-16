package cn.edu.hust.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AbstractSquareTest {

    /**
     * The square under test.
     */
    private AbstractSquare abstractSquare;

    /**
     * Resets the square under test.
     */
    @BeforeEach
    void setUp() {
        abstractSquare = new BasicAbstractSquare();
    }

    /**
     * Assert that the square holds the occupant once it has occupied it.
     */
    @Test
    void testOccupy() {
        AbstractUnit occupant = mock(AbstractUnit.class);
        abstractSquare.put(occupant);

        assertThat(abstractSquare.getOccupants()).contains(occupant);
    }

    /**
     * Assert that the square no longer holds the occupant after it has left the
     * square.
     */
    @Test
    void testLeave() {
        AbstractUnit occupant = mock(AbstractUnit.class);
        abstractSquare.put(occupant);
        abstractSquare.remove(occupant);

        assertThat(abstractSquare.getOccupants()).doesNotContain(occupant);
    }

    /**
     * Assert that the order in which units entered the square is preserved.
     */
    @Test
    void testOrder() {
        AbstractUnit o1 = mock(AbstractUnit.class);
        AbstractUnit o2 = mock(AbstractUnit.class);
        abstractSquare.put(o1);
        abstractSquare.put(o2);

        assertThat(abstractSquare.getOccupants()).containsSequence(o1, o2);
    }
}
