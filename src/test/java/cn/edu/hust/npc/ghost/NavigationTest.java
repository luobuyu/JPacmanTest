package cn.edu.hust.npc.ghost;

import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.edu.hust.level.LevelFactory;
import cn.edu.hust.level.MapParser;
import cn.edu.hust.level.Pellet;
import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.PacManSprites;
import cn.edu.hust.board.Board;
import cn.edu.hust.board.BoardFactory;
import cn.edu.hust.board.Direction;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.board.AbstractUnit;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author zhangxu
 */
@SuppressWarnings({"magicnumber", "PMD.AvoidDuplicateLiterals"})
class NavigationTest {

    /**
     * Map parser used to construct boards.
     */
    private MapParser parser;

    /**
     * Set up the map parser.
     */
    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
            sprites)), new BoardFactory(sprites));
    }

    /** same
     * Verifies that the path to the same square is empty.
     */
    @Test
    void testShortestPathEmpty() {
        Board b = parser.parseMap(Lists.newArrayList(" ")).getBoard();
        AbstractSquare s1 = b.squareAt(0, 0);
        AbstractSquare s2 = b.squareAt(0, 0);
        List<Direction> path = Navigation
            .shortestPath(s1, s2, mock(AbstractUnit.class));
        assertThat(path).isEmpty();
    }

    /**
     * Verifies that if no path exists, the result is <code>null</code>.
     */
    @Test
    void testNoShortestPath() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "# # #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(3, 1); // third col , first row
        List<Direction> path = Navigation
            .shortestPath(s1, s2, mock(AbstractUnit.class));
        assertThat(path).isNull();
    }

    /**
     * Verifies that having no traveller ignores terrain.
     */
    @Test
    void testNoTraveller() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "# # #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1);
        AbstractSquare s2 = b.squareAt(3, 1);
        List<Direction> path = Navigation.shortestPath(s1, s2, null);
        assertThat(path).containsExactly(Direction.EAST, Direction.EAST);
    }

    /**
     * Tests if the algorithm can find a path in a straight line.
     */
    @Test
    void testSimplePath() {
        Board b = parser.parseMap(Lists.newArrayList("####", "#  #", "####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1);
        AbstractSquare s2 = b.squareAt(2, 1);
        List<Direction> path = Navigation
            .shortestPath(s1, s2, mock(AbstractUnit.class));
        assertThat(path).containsExactly(Direction.EAST);
    }

    /**
     * Verifies that the algorithm can find a path when it has to take corners.
     */
    @Test
    void testCornerPath() {
        Board b = parser.parseMap(
            Lists.newArrayList("####", "#  #", "## #", "####")).getBoard();
        AbstractSquare s1 = b.squareAt(1, 1);
        AbstractSquare s2 = b.squareAt(2, 2);
        List<Direction> path = Navigation
            .shortestPath(s1, s2, mock(AbstractUnit.class));
        assertThat(path).containsExactly(Direction.EAST, Direction.SOUTH);
    }

    /**
     * Verifies that the nearest object is detected.
     */
    @Test
    void testNearestUnit() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "# ..#", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1);
        AbstractSquare s2 = b.squareAt(2, 1);
        AbstractSquare result = Navigation.findNearest(Pellet.class, s1).getAbstractSquare();
        assertThat(result).isEqualTo(s2);
    }

    /**
     * Verifies that there is no such location if there is no nearest object.
     */
    @Test
    void testNoNearestUnit() {
        Board b = parser.parseMap(Lists.newArrayList(" ")).getBoard();
        AbstractSquare s1 = b.squareAt(0, 0);
        AbstractUnit abstractUnit = Navigation.findNearest(Pellet.class, s1);
        assertThat(abstractUnit).isNull();
    }

    /**
     * Verifies that there is ghost on the default board
     * next to cell [1, 1].
     *
     * @throws IOException if board reading fails.
     */
    @Test
    void testFullSizedLevel() throws IOException {
        try (InputStream i = getClass().getResourceAsStream("/board.txt")) {
            Board b = parser.parseMap(i).getBoard();
            AbstractSquare s1 = b.squareAt(1, 1);
            AbstractUnit abstractUnit = Navigation.findNearest(AbstractGhost.class, s1);
            assertThat(abstractUnit).isNotNull();
        }
    }
}
