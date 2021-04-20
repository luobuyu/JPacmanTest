package cn.edu.hust.sprite;


import static org.assertj.core.api.Assertions.assertThat;

import cn.edu.hust.Launcher;
import cn.edu.hust.board.*;

import cn.edu.hust.npc.ghost.GhostFactory;

import java.io.IOException;
import java.sql.Time;
import java.util.Timer;

import cn.edu.hust.level.LevelFactory;
import cn.edu.hust.level.MapParser;
import cn.edu.hust.level.Player;
import cn.edu.hust.level.PlayerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.common.collect.Lists;
/**
 * Verifies the Movatation of sprites.
 *
 * @author He Wang 
 */
@SuppressWarnings("magicnumber")
public class SpriteTest {

    private Sprite sprite;
    private SpriteStore store;
    private MapParser parser;

    private Player player;
    private BasicAbstractSquare s1, s2;
    private PlayerFactory playerFactory;
    private BoardFactory factory;
    private Launcher launcher;

    @BeforeEach
    void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();
    }
    /**
     * The common fixture of this test class is
     * a 64 by 64 pixel white sprite.
     *
     * @throws java.io.IOException
     *      when the sprite could not be loaded.
     */
    @BeforeEach
    public void setUp() throws IOException {
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

        store = new SpriteStore();
        sprite = store.loadSprite("/sprite/64x64white.png");
    }

    /**
     * Test the reaction of key-right
     */
    @Test
    void testEast() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(2, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.EAST);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the reaction of key-left
     */
    @Test
    void testWest() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(3, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(2, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.WEST);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }
    /**
     * Test the reaction of key-up
     */
    @Test
    void testNorth() {
        Board b = parser
            .parseMap(Lists.newArrayList("#   #", "#   #", "#   #"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 0); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.NORTH);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }
    /**
     * Test the reaction of key-up
     */
    @Test
    void testSouth() {
        Board b = parser
            .parseMap(Lists.newArrayList("#   #", "#   #", "#   #"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 2); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.SOUTH);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }
    /**
     * Test the movatation in east boundary
     */
    @Test
    void testEastBoundary() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(3, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(3, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.EAST);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the movatation in west boundary
     */
    @Test
    void testWestBoundary() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.WEST);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the movatation in north boundary
     */
    @Test
    void testNorthBoundary() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.NORTH);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the movatation in south boundary
     */
    @Test
    void testSouthBoundary() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.SOUTH);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the move east to barrier
     */
    @Test
    void testEastMove() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(3, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(3, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.EAST);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the move west to barrier
     */
    @Test
    void testWestMove() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.WEST);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }
    /**
     * Test the movatation in north boundary
     */
    @Test
    void testNorthMove() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.NORTH);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    /**
     * Test the move south to barrier
     */
    @Test
    void testSouthMove() {
        Board b = parser
            .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
            .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        launcher.getAbstractGame().move(pacMan, Direction.SOUTH);
        assertThat(pacMan.getAbstractSquare()).isEqualTo(s2);
    }

    @Test
    void testKeySpeed() {
        Board b = parser
                .parseMap(Lists.newArrayList("###############", "#             #", "###############"))
                .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(1, 1); // third col , first row
        Player pacMan = playerFactory.createPacMan();
        pacMan.occupy(s1);
        long extra = 20;

        launcher.getAbstractGame().move(pacMan, Direction.SOUTH);
        long firstTime = System.currentTimeMillis();
        launcher.getAbstractGame().move(pacMan, Direction.SOUTH);
        long secondTime = System.currentTimeMillis();
        assertThat(secondTime - firstTime).isGreaterThan(extra);
    }

}