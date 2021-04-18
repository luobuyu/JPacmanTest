package cn.edu.hust.level;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.edu.hust.board.Board;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.npc.AbstractGhost;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// The four suppress warnings ignore the same rule, which results in 4 same string literals
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyStaticImports"})
class LevelTest {

    /**
     * The level under test.
     */
    private Level level;

    /**
     * An NPC on this level.
     */
    private final AbstractGhost abstractGhost = mock(AbstractGhost.class);

    /**
     * Starting position 1.
     */
    private final AbstractSquare abstractSquare1 = mock(AbstractSquare.class);

    /**
     * Starting position 2.
     */
    private final AbstractSquare abstractSquare2 = mock(AbstractSquare.class);

    /**
     * The board for this level.
     */
    private final Board board = mock(Board.class);

    /**
     * The collision map.
     */
    private final CollisionMap collisions = mock(CollisionMap.class);

    /**
     * Sets up the level with the default board, a single NPC and a starting
     * square.
     */
    @BeforeEach
    void setUp() {
        final long defaultInterval = 100L;
        level = new Level(board, Lists.newArrayList(abstractGhost), Lists.newArrayList(
                abstractSquare1, abstractSquare2), collisions);
        when(abstractGhost.getInterval()).thenReturn(defaultInterval);
    }

    /**
     * Validates the state of the level when it isn't started yet.
     */
    @Test
    void noStart() {
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Validates the state of the level when it is stopped without starting.
     */
    @Test
    void stop() {
        level.stop();
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Validates the state of the level when it is started.
     */
    @Test
    void start() {
        level.start();
        assertThat(level.isInProgress()).isTrue();
    }

    /**
     * Validates the state of the level when it is started then stopped.
     */
    @Test
    void startStop() {
        level.start();
        assertThat(level.isInProgress()).isTrue();
        level.stop();
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Validates the state of the level when it is stopped then stopped.
     */
    @Test
    void stopStop() {
        level.stop();
        assertThat(level.isInProgress()).isFalse();
        level.stop();
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Validates the state of the level when it is stopped then started.
     */
    @Test
    void stopStart() {
        level.stop();
        assertThat(level.isInProgress()).isFalse();
        level.start();
        assertThat(level.isInProgress()).isTrue();
    }

    /**
     * Validates the state of the level when it is started then started.
     */
    @Test
    void startStart() {
        level.start();
        assertThat(level.isInProgress()).isTrue();
        level.start();
        assertThat(level.isInProgress()).isTrue();
    }

    /**
     * Verifies registering a player puts the player on the correct starting
     * square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerPlayer() {
        Player p = mock(Player.class);
        level.registerPlayer(p);
        verify(p).occupy(abstractSquare1);
    }

    /**
     * Verifies registering a player twice does not do anything.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerPlayerTwice() {
        Player p = mock(Player.class);
        level.registerPlayer(p);
        level.registerPlayer(p);
        verify(p, times(1)).occupy(abstractSquare1);
    }

    /**
     * Verifies registering a second player puts that player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerSecondPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        level.registerPlayer(p1);
        level.registerPlayer(p2);
        verify(p2).occupy(abstractSquare2);
    }

    /**
     * Verifies registering a third player puts the player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerThirdPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);
        level.registerPlayer(p1);
        level.registerPlayer(p2);
        level.registerPlayer(p3);
        verify(p3).occupy(abstractSquare1);
    }
}
