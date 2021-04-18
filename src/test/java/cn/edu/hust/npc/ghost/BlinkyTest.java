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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;

import javax.swing.text.html.Option;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
class BlinkyTest {

    private static MapParser parser;
    private BoardFactory factory;

    /**
     * Squares on the board to test.
     */
    private AbstractSquare s1;
    private AbstractSquare s2;
    private AbstractSquare expect;
    private GhostFactory ghostFactory;
    private Launcher launcher;
    private PlayerFactory playerFactory;
    private static Board b;





    @BeforeAll
    static void beforeAll() {
        System.out.println("start test blinky!!!!");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finish test blinky!!!");
    }

    @BeforeEach
    void setUp() throws IOException{
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
    @ParameterizedTest
    @MethodSource("provider")
    void nextAiMoveTrue(AbstractSquare s1, AbstractSquare s2, Direction x) {
        AbstractGhost blinky = ghostFactory.createBlinky();
        Player player = playerFactory.createPacMan();
        player.occupy(s2);
        blinky.occupy(s1);
        assertThat(blinky.nextAiMove()).isEqualTo(Optional.of(Direction.valueOf(x.name())));
    }

    static Stream<Arguments> provider() throws IOException{

        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));
        b = parser.parseMap("/board.txt").getBoard();

        return Stream.of(
                // up
                Arguments.of(b.squareAt(8, 13), b.squareAt(19, 1), Direction.NORTH),
                // left
                Arguments.of(b.squareAt(8, 13), b.squareAt(3, 3), Direction.NORTH),
                // right
                Arguments.of(b.squareAt(8, 13), b.squareAt(21, 15), Direction.NORTH),
                // down
                Arguments.of(b.squareAt(8, 13), b.squareAt(3, 19), Direction.WEST)
        );
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