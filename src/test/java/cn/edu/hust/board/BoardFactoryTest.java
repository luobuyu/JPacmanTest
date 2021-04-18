package cn.edu.hust.board;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

import cn.edu.hust.Launcher;
import cn.edu.hust.level.LevelFactory;
import cn.edu.hust.level.MapParser;
import cn.edu.hust.level.Player;
import cn.edu.hust.level.PlayerFactory;
import cn.edu.hust.npc.ghost.GhostFactory;
import cn.edu.hust.npc.ghost.Navigation;
import cn.edu.hust.sprite.PacManSprites;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests the linking of squares done by the board factory.

 */
class BoardFactoryTest {

    /**
     * The factory under test.
     */
    private BoardFactory factory;

    /**
     * Squares on the board to test.
     */
    private AbstractSquare s1, s2;
    private PlayerFactory playerFactory;
    private MapParser parser;
    private Launcher launcher;
    /**
     * Resets the factory under test.
     */
    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));

        factory = new BoardFactory(sprites);
        playerFactory = new PlayerFactory(sprites);
        s1 = new BasicAbstractSquare();
        s2 = new BasicAbstractSquare();
        launcher = new Launcher();
        launcher.launch();
        launcher.getAbstractGame().start();
    }

    /**
     * Verifies that a single cell is connected to itself on all sides.
     */
    @Test
    void worldIsRound() {
        factory.createBoard(new AbstractSquare[][]{{s1}});
        assertThat(Arrays.stream(Direction.values()).map(s1::getSquareAt)).containsOnly(s1);
    }

    /**
     * Verifies a chain of cells is connected to the east.
     */
    @Test
    void connectedEast() {
        factory.createBoard(new AbstractSquare[][]{{s1}, {s2}});
        System.out.println(s1.getSquareAt(Direction.EAST));
        assertThat(s1.getSquareAt(Direction.EAST)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.EAST)).isEqualTo(s1);
    }

    /**
     * Verifies a chain of cells is connected to the west.
     */
    @Test
    void connectedWest() {
        factory.createBoard(new AbstractSquare[][]{{s1}, {s2}});
        assertThat(s1.getSquareAt(Direction.WEST)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.WEST)).isEqualTo(s1);
    }

    /**
     * Verifies a chain of cells is connected to the north.
     */
    @Test
    void connectedNorth() {
        factory.createBoard(new AbstractSquare[][]{{s1, s2}});
        assertThat(s1.getSquareAt(Direction.NORTH)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.NORTH)).isEqualTo(s1);
    }

    /**
     * Verifies a chain of cells is connected to the south.
     */
    @Test
    void connectedSouth() {
        factory.createBoard(new AbstractSquare[][]{{s1, s2}});
        assertThat(s1.getSquareAt(Direction.SOUTH)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.SOUTH)).isEqualTo(s1);
    }
}
