package cn.edu.hust.npc.ghost;

import cn.edu.hust.board.Direction;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.board.AbstractUnit;
import cn.edu.hust.level.Player;
import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.Sprite;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhangxu
 */
public class Inky extends AbstractGhost {

    private static final int SQUARES_AHEAD = 2;

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 250;

    /**
     * Creates a new "Inky".
     *
     * @param spriteMap The sprites for this ghost.
     */
    public Inky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Inky has the most complicated AI of all. Inky considers two things: Blinky's
     * location, and the location two grid spaces ahead of Pac-Man. Inky
     * draws a line from Blinky to the spot that is two squares in front of
     * Pac-Man and extends that line twice as far. Therefore, if Inky is
     * alongside Blinky when they are behind Pac-Man, Inky will usually
     * follow Blinky the whole time. But if Inky is in front of Pac-Man when
     * Blinky is far behind him, Inky tends to want to move away from Pac-Man
     * (in reality, to a point very far ahead of Pac-Man). Inky is affected
     * by a similar targeting bug that affects Speedy. When Pac-Man is moving or
     * facing up, the spot Inky uses to draw the line is two squares above
     * and left of Pac-Man.
     * </p>
     *
     * <p>
     * <b>Implementation:</b>
     * To actually implement this in jpacman we have the following approximation:
     * first determine the square of Blinky (A) and the square 2
     * squares away from Pac-Man (B). Then determine the shortest path from A to
     * B regardless of terrain and walk that same path from B. This is the
     * destination.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();
        AbstractUnit blinky = Navigation.findNearest(Blinky.class, getAbstractSquare());
        AbstractUnit player = Navigation.findNearest(Player.class, getAbstractSquare());

        if (blinky == null || player == null) {
            return Optional.empty();
        }

        assert player.hasSquare();
        AbstractSquare playerDestination = player.squaresAheadOf(SQUARES_AHEAD);

        List<Direction> firstHalf = Navigation.shortestPath(blinky.getAbstractSquare(),
            playerDestination, null);

        if (firstHalf == null) {
            return Optional.empty();
        }

        AbstractSquare destination = followPath(firstHalf, playerDestination);
        List<Direction> path = Navigation.shortestPath(getAbstractSquare(),
            destination, this);

        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }


    private AbstractSquare followPath(List<Direction> directions, AbstractSquare start) {
        AbstractSquare destination = start;

        for (Direction d : directions) {
            destination = destination.getSquareAt(d);
        }

        return destination;
    }
}
