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

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InkyTest {
    private static MapParser parser;
    private static Board b;
    private BoardFactory factory;
    private PlayerFactory playerFactory;
    private GhostFactory ghostFactory;
    private AbstractSquare s1;
    private AbstractSquare s2;
    private Launcher launcher;
    @BeforeAll
    static void beforeAll() {
        System.out.println("start test Inky!!!!");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finish test Inky!!!");
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

    @ParameterizedTest
    @MethodSource("providerChase")
    void nextAiMoveChase(AbstractSquare s1, AbstractSquare s2, Direction x) {

        AbstractGhost inky = ghostFactory.createInky();
        Player player = playerFactory.createPacMan();
        inky.occupy(s1);
        player.occupy(s2);
        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.valueOf(x.name())));
    }
    static Stream<Arguments> providerChase() throws IOException {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));
        b = parser.parseMap("/board.txt").getBoard();

        return Stream.of(
                // chase
                // up, gt eight   , chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(5, 1), Direction.SOUTH),
                // left    chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(5, 19), Direction.NORTH),
                // right   chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(15, 18), Direction.NORTH),
                // down   chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(1, 15), Direction.NORTH)
        );
    }
    @Test
    void nextAiMoveNull() throws IOException{
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));
        b = parser.parseMap("/board.txt").getBoard();
        AbstractGhost inky = ghostFactory.createInky();
        s1 = b.squareAt(5, 13);
        inky.occupy(s1);
        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }
}