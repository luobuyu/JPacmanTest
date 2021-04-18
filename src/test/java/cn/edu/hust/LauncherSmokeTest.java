package cn.edu.hust;

import static org.assertj.core.api.Assertions.assertThat;

import cn.edu.hust.board.Direction;
import cn.edu.hust.game.AbstractGame;
import cn.edu.hust.level.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LauncherSmokeTest {

    private Launcher launcher;

    /**
     * Launch the user interface.
     */
    @BeforeEach
    void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();
    }

    /**
     * Quit the user interface when we're done.
     */
    @AfterEach
    void tearDown() {
        launcher.dispose();
    }

    /**
     * Launch the game, and imitate what would happen in a typical game.
     * The test is only a smoke test, and not a focused small test.
     * Therefore it is OK that the method is a bit too long.
     *
     * @throws InterruptedException Since we're sleeping in this test.
     */
//    @SuppressWarnings({"magicnumber", "methodlength", "PMD.JUnitTestContainsTooManyAsserts"})
    @Test
    void smokeTest() throws InterruptedException {
        AbstractGame abstractGame = launcher.getAbstractGame();
        Player player = abstractGame.getPlayers().get(0);

        // start cleanly.
        assertThat(abstractGame.isInProgress()).isFalse();
        abstractGame.start();
        assertThat(abstractGame.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

        // get points
        abstractGame.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

        // now moving back does not change the score
        abstractGame.move(player, Direction.WEST);
        assertThat(player.getScore()).isEqualTo(10);

        // try to move as far as we can
        move(abstractGame, Direction.EAST, 7);
        assertThat(player.getScore()).isEqualTo(60);

        // move towards the monsters
        move(abstractGame, Direction.NORTH, 6);
        assertThat(player.getScore()).isEqualTo(120);

        // no more points to earn here.
        move(abstractGame, Direction.WEST, 2);
        assertThat(player.getScore()).isEqualTo(120);

        move(abstractGame, Direction.NORTH, 2);

        // Sleeping in tests is generally a bad idea.
        // Here we do it just to let the monsters move.
        Thread.sleep(500L);

        // we're close to monsters, this will get us killed.
        move(abstractGame, Direction.WEST, 10);
        move(abstractGame, Direction.EAST, 10);
        assertThat(player.isAlive()).isFalse();


    }

    @Test
    void smokeTest1() throws InterruptedException {
        AbstractGame abstractGame = launcher.getAbstractGame();
        Player player = abstractGame.getPlayers().get(0);

        // start cleanly.
        assertThat(abstractGame.isInProgress()).isFalse();
        abstractGame.start();
        assertThat(abstractGame.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

        // get points
        abstractGame.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

    }

    @Test
    void smokeTest2() throws InterruptedException {
        AbstractGame abstractGame = launcher.getAbstractGame();
        Player player = abstractGame.getPlayers().get(0);

        // start cleanly.
        assertThat(abstractGame.isInProgress()).isFalse();
        abstractGame.start();
        assertThat(abstractGame.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

        // get points
        abstractGame.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

        // now moving back does not change the score
        abstractGame.move(player, Direction.WEST);
        assertThat(player.getScore()).isEqualTo(10);


    }

    @Test
    void smokeTest3() throws InterruptedException {
        AbstractGame abstractGame = launcher.getAbstractGame();
        Player player = abstractGame.getPlayers().get(0);

        // start cleanly.
        assertThat(abstractGame.isInProgress()).isFalse();
        abstractGame.start();
        assertThat(abstractGame.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

        // get points
        abstractGame.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

        // now moving back does not change the score
        abstractGame.move(player, Direction.WEST);
        assertThat(player.getScore()).isEqualTo(10);

        // try to move as far as we can
        move(abstractGame, Direction.EAST, 7);
        assertThat(player.getScore()).isEqualTo(60);

    }

    @Test
    void smokeTest4() throws InterruptedException {
        AbstractGame abstractGame = launcher.getAbstractGame();
        Player player = abstractGame.getPlayers().get(0);

        // start cleanly.
        assertThat(abstractGame.isInProgress()).isFalse();
        abstractGame.start();
        assertThat(abstractGame.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

        // get points
        abstractGame.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

        // now moving back does not change the score
        abstractGame.move(player, Direction.WEST);
        assertThat(player.getScore()).isEqualTo(10);

        // try to move as far as we can
        move(abstractGame, Direction.EAST, 7);
        assertThat(player.getScore()).isEqualTo(60);

        // move towards the monsters
        move(abstractGame, Direction.NORTH, 6);
        assertThat(player.getScore()).isEqualTo(120);


    }

    @Test
    void smokeTest5() throws InterruptedException {
        AbstractGame abstractGame = launcher.getAbstractGame();
        Player player = abstractGame.getPlayers().get(0);

        // start cleanly.
        assertThat(abstractGame.isInProgress()).isFalse();
        abstractGame.start();
        assertThat(abstractGame.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

        // get points
        abstractGame.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

        // now moving back does not change the score
        abstractGame.move(player, Direction.WEST);
        assertThat(player.getScore()).isEqualTo(10);

        // try to move as far as we can
        move(abstractGame, Direction.EAST, 7);
        assertThat(player.getScore()).isEqualTo(60);

        // move towards the monsters
        move(abstractGame, Direction.NORTH, 6);
        assertThat(player.getScore()).isEqualTo(120);

        // no more points to earn here.
        move(abstractGame, Direction.WEST, 2);
        assertThat(player.getScore()).isEqualTo(120);
        move(abstractGame, Direction.NORTH, 2);

    }

    /**
     * Make number of moves in given direction.
     *
     * @param abstractGame The game we're playing
     * @param dir The direction to be taken
     * @param numSteps The number of steps to take
     */
    public static void move(AbstractGame abstractGame, Direction dir, int numSteps) {
        Player player = abstractGame.getPlayers().get(0);
        for (int i = 0; i < numSteps; i++) {
            abstractGame.move(player, dir);
        }
    }
}
