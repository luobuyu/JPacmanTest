package cn.edu.hust.npc.ghost;

import cn.edu.hust.Launcher;
import cn.edu.hust.board.*;
import cn.edu.hust.level.LevelFactory;
import cn.edu.hust.level.MapParser;
import cn.edu.hust.level.Player;
import cn.edu.hust.level.PlayerFactory;
import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.PacManSprites;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ClydeTest {

    private static MapParser parser;
    private static Board b;
    private BoardFactory factory;
    private PlayerFactory playerFactory;
    private GhostFactory ghostFactory;
    private BasicAbstractSquare s1;
    private BasicAbstractSquare s2;
    private Launcher launcher;

    @BeforeAll
    static void beforeAll() {
        System.out.println("start test Clyde!!!!");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finish test Clyde!!!");
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
     * eight grids
     * @return
     * @throws IOException
     */
    static Stream<Arguments> providerRunaway() throws IOException {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));
        b = parser.parseMap("/board.txt").getBoard();

        return Stream.of(
                // up, lt eight   , run away
                Arguments.of(b.squareAt(8, 13), b.squareAt(5, 9), Direction.SOUTH),
                // left    run away
                Arguments.of(b.squareAt(2, 1), b.squareAt(1, 1), Direction.EAST),
                // right   run away
                Arguments.of(b.squareAt(16, 13), b.squareAt(21, 13), Direction.WEST),
                // down   run  away
                Arguments.of(b.squareAt(8, 17), b.squareAt(10, 19), Direction.WEST)

        );
    }

    @ParameterizedTest
    @MethodSource("providerRunaway")
    void nextAiMoveRunAway(AbstractSquare s1, AbstractSquare s2, Direction x) {

        AbstractGhost clyde = ghostFactory.createClyde();
        Player player = playerFactory.createPacMan();
        clyde.occupy(s1);
        player.occupy(s2);
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.valueOf(x.name())));
    }


    @ParameterizedTest
    @MethodSource("providerChase")
    void nextAiMoveChase(AbstractSquare s1, AbstractSquare s2, Direction x) {

        AbstractGhost clyde = ghostFactory.createClyde();
        Player player = playerFactory.createPacMan();
        clyde.occupy(s1);
        player.occupy(s2);
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.valueOf(x.name())));
    }
    static Stream<Arguments> providerChase() throws IOException {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
                sprites)), new BoardFactory(sprites));
        b = parser.parseMap("/board.txt").getBoard();

        return Stream.of(
                // chase
                // up, gt eight   , chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(5, 1), Direction.NORTH),
                // left    chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(5, 19), Direction.SOUTH),
                // right   chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(15, 18), Direction.SOUTH),
                // down   chase
                Arguments.of(b.squareAt(5, 13), b.squareAt(1, 15), Direction.SOUTH)

        );
    }
}