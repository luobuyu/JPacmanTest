package cn.edu.hust.e2e.framework.startup;

import static org.assertj.core.api.Assertions.assertThat;

import cn.edu.hust.Launcher;
import cn.edu.hust.game.AbstractGame;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class StartupSteps {

    private Launcher launcher;

    private AbstractGame getGame() {
        return launcher.getAbstractGame();
    }


    /**
     * Launch the game. This makes the game available via
     * the {@link #getGame()} method.
     */
    @Given("^the user has launched the JPacman GUI$")
    public void theUserHasLaunchedTheJPacmanGUI() {
        launcher = new Launcher();
        launcher.launch();
    }

    /**
     * Start playing the game.
     */
    @When("^the user presses the \"Start\" button$")
    public void theUserPressesStart() {
        getGame().start();
    }

    /**
     * Verify that the play is actually running.
     */
    @Then("^the game is running$")
    public void theGameShouldStart() {
        assertThat(getGame().isInProgress()).isTrue();
    }

    /**
     * Close the UI after all tests are finished.
     */
    @After("@framework")
    public void tearDownUI() {
        launcher.dispose();
    }
}
