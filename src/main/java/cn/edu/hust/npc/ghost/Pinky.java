package cn.edu.hust.npc.ghost;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.edu.hust.board.Direction;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.board.AbstractUnit;
import cn.edu.hust.level.Player;
import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.Sprite;

/**
 * @author wangzhongren
 */
public class Pinky extends AbstractGhost {

    private static final int SQUARES_AHEAD = 4;

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 200;

    /**
     * Creates a new "Pinky", a.k.a. "Speedy".
     *
     * @param spriteMap
     *            The sprites for this ghost.
     */
    public Pinky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * When the ghosts are not patrolling their home corners, Pinky wants to go
     * to the place that is four grid spaces ahead of Pac-Man in the direction
     * that Pac-Man is facing. If Pac-Man is facing down, Pinky wants to go to
     * the location exactly four spaces below Pac-Man. Moving towards this place
     * uses the same logic that Blinky uses to find Pac-Man's exact location.
     * Pinky is affected by a targeting bug if Pac-Man is facing up - when he
     * moves or faces up, Pinky tries moving towards a point up, and left, four
     * spaces.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();

        AbstractUnit player = Navigation.findNearest(Player.class, getAbstractSquare());
        if (player == null) {
            return Optional.empty();
        }
        assert player.hasSquare();
        AbstractSquare destination = player.squaresAheadOf(SQUARES_AHEAD);

        List<Direction> path = Navigation.shortestPath(getAbstractSquare(), destination, this);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }
}
