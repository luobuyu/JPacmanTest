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
 * @author hewang
 */
public class Blinky extends AbstractGhost {

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
     * Creates a new "Blinky", a.k.a. "Shadow".
     *
     * @param spriteMap
     *            The sprites for this ghost.
     */
    // TODO Blinky should speed up when there are a few pellets left, but he
    /** has no way to find out how many there are.
     *
     */
    public Blinky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * When the ghosts are not patrolling in their home corners (Blinky:
     * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left),
     * Blinky will attempt to shorten the distance between Pac-Man and himself.
     * If he has to choose between shortening the horizontal or vertical
     * distance, he will choose to shorten whichever is greatest. For example,
     * if Pac-Man is four grid spaces to the left, and seven grid spaces above
     * Blinky, he'll try to move up towards Pac-Man before he moves to the left.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();

        // TODO Blinky should patrol his corner every once in a while
        // TODO Implement his actual behaviour instead of simply chasing.
        AbstractUnit nearest = Navigation.findNearest(Player.class, getAbstractSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        AbstractSquare target = nearest.getAbstractSquare();

        List<Direction> path = Navigation.shortestPath(getAbstractSquare(), target, this);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }
}
