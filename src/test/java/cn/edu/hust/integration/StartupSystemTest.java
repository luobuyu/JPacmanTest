package cn.edu.hust.integration;

import cn.edu.hust.Launcher;
import cn.edu.hust.game.AbstractGame;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An example test class that conducts integration tests.
 */
public class StartupSystemTest {

    private Launcher launcher;

    /**
     * Start a launcher, which can display the user interface.
     */
    @BeforeEach
    public void before() {
        launcher = new Launcher();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }

    /**
     * The simplest test that just starts the
     * game and checks it is indeed in progress.
     */
    @Test
    public void gameIsRunning() {
        launcher.launch();

        getGame().start();

        Assertions.assertThat(getGame().isInProgress()).isTrue();
    }


    private AbstractGame getGame() {
        return launcher.getAbstractGame();
    }
}
