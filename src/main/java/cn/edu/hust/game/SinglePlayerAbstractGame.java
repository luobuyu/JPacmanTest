package cn.edu.hust.game;

import java.util.List;

import cn.edu.hust.level.Level;
import cn.edu.hust.level.Player;

import com.google.common.collect.ImmutableList;


/**
 * @author xurenjie
 */
public class SinglePlayerAbstractGame extends AbstractGame {

    /**
     * The player of this game.
     */
    private final Player player;

    /**
     * The level of this game.
     */
    private final Level level;

    /**
     * Create a new single player game for the provided level and player.
     *
     * @param player
     *            The player.
     * @param level
     *            The level.
     */
    protected SinglePlayerAbstractGame(Player player, Level level) {
        assert player != null;
        assert level != null;

        this.player = player;
        this.level = level;
        this.level.registerPlayer(player);
    }

    @Override
    public List<Player> getPlayers() {
        return ImmutableList.of(player);
    }

    @Override
    public Level getLevel() {
        return level;
    }
}
