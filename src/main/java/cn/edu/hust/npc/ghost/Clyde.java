package cn.edu.hust.npc.ghost;

import java.util.EnumMap;
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
 * lazy, stay away from pac man
 * @author hewang
 */
public class Clyde extends AbstractGhost {

    /**
     * The amount of cells Clyde wants to stay away from Pac Man.
     */
    private static final int SHYNESS = 8;

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
     * A map of opposite directions.
     */
    private static final Map<Direction, Direction> OPPOSITES = new EnumMap<>(Direction.class);

    static {
        OPPOSITES.put(Direction.NORTH, Direction.SOUTH);
        OPPOSITES.put(Direction.SOUTH, Direction.NORTH);
        OPPOSITES.put(Direction.WEST, Direction.EAST);
        OPPOSITES.put(Direction.EAST, Direction.WEST);
    }

    /**
     * Creates a new "Clyde", a.k.a. "Pokey".
     *
     * @param spriteMap The sprites for this ghost.
     */
    public Clyde(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Clyde has two basic AIs, one for when he's far from Pac-Man, and one for
     * when he is near to Pac-Man. 
     * When Clyde is far away from Pac-Man (beyond eight grid spaces),
     * Clyde behaves very much like Blinky, trying to move to Pac-Man's exact
     * location. However, when Clyde gets within eight grid spaces of Pac-Man,
     * he automatically changes his behavior and runs away
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();

        AbstractUnit nearest = Navigation.findNearest(Player.class, getAbstractSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        AbstractSquare target = nearest.getAbstractSquare();

        List<Direction> path = Navigation.shortestPath(getAbstractSquare(), target, this);
        if (path != null && !path.isEmpty()) {
            Direction direction = path.get(0);
            if (path.size() <= SHYNESS) {
                return Optional.ofNullable(OPPOSITES.get(direction));
            }
            return Optional.of(direction);
        }
        return Optional.empty();
    }
}
