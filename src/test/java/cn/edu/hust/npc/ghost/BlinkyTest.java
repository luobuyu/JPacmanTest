package cn.edu.hust.npc.ghost;

import cn.edu.hust.Launcher;
import cn.edu.hust.board.*;
import cn.edu.hust.level.LevelFactory;
import cn.edu.hust.level.MapParser;
import cn.edu.hust.level.Player;
import cn.edu.hust.level.PlayerFactory;
import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.PacManSprites;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
class BlinkyTest {

    private BoardFactory factory;

    /**
     * Squares on the board to test.
     */
    private AbstractSquare s1, s2;
    private GhostFactory ghostFactory;
    private MapParser parser;
    private Launcher launcher;
    private PlayerFactory playerFactory;
    @BeforeAll
    static void beforeAll() {
        System.out.println("start test blinky!!!!");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finish test blinky!!!");
    }

    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));

        factory = new BoardFactory(sprites);
        playerFactory = new PlayerFactory(sprites);
        ghostFactory = new GhostFactory(sprites);
        s1 = new BasicAbstractSquare();
        s2 = new BasicAbstractSquare();
        launcher = new Launcher();
        launcher.launch();
        launcher.getAbstractGame().start();
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * when can move，blinky will chase pacman
     */
    @Test
    void nextAiMoveTrue() {
        Board b = parser
                .parseMap(Lists.newArrayList("#####", "#   #", "#####"))
                .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(3, 1); // third col , first row
        AbstractGhost blinky = ghostFactory.createBlinky();
        Player player = playerFactory.createPacMan();
        player.occupy(s2);
        blinky.occupy(s1);

        assertThat(blinky.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
//        List<Direction> path = Navigation
//                .shortestPath(s1, s2, mock(AbstractUnit.class));
//        assertThat(path).isNull();
    }

    @Test
    void nextAiMoveNull() {
        Board b = parser
                .parseMap(Lists.newArrayList("#####", "# # #", "#####"))
                .getBoard();
        AbstractSquare s1 = b.squareAt(1, 1); // first row , first col
        AbstractSquare s2 = b.squareAt(3, 1); // third col , first row
        AbstractGhost blinky = ghostFactory.createBlinky();
        Player player = playerFactory.createPacMan();
        player.occupy(s2);
        blinky.occupy(s1);
        assertThat(blinky.nextAiMove()).isEqualTo(Optional.empty());
    }
}