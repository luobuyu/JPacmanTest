package cn.edu.hust.level;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.edu.hust.board.Direction;
import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.AnimatedSprite;
import cn.edu.hust.sprite.PacManSprites;
import cn.edu.hust.sprite.Sprite;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ouyangwenzhe
 */
class PlayerCollisionsTest {
    private final Pellet pellet = mock(Pellet.class);
    private final Player player = mock(Player.class);
    private final PlayerCollisions playerCollisions = mock(PlayerCollisions.class);
    private final AbstractGhost abstractGhost = mock(AbstractGhost.class);
    private final Sprite sprite = mock(Sprite.class);




    @BeforeEach

    void setUp() {
        playerCollisions.playerVersusGhost(player,abstractGhost);
        playerCollisions.playerVersusPellet(player,pellet);
    }

    @AfterEach
    void tearDown() {
        System.out.print("test completed");
    }

    @Test
    void testPlayerVersusGhost() {

        assertThat(player.isAlive()).isFalse();
    }

    @Test
    void testPlayerVersusPellet() {
        assertThat(player.getScore()).isEqualTo(0);
    }

}