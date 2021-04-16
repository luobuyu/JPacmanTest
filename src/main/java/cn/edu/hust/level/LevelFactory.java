package cn.edu.hust.level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.PacManSprites;
import cn.edu.hust.sprite.Sprite;
import cn.edu.hust.board.Board;
import cn.edu.hust.board.Direction;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.npc.ghost.GhostColor;
import cn.edu.hust.npc.ghost.GhostFactory;

/**
 * Factory that creates levels and units.
 * @author zhangxu

 */
public class LevelFactory {

    private static final int GHOSTS = 4;
    private static final int BLINKY = 0;
    private static final int INKY = 1;
    private static final int PINKY = 2;
    private static final int CLYDE = 3;

    /**
     * The default value of a pellet.
     */
    private static final int PELLET_VALUE = 10;

    /**
     * The sprite store that provides sprites for units.
     */
    private final PacManSprites sprites;

    /**
     * Used to cycle through the various ghost types.
     */
    private int ghostIndex;

    /**
     * The factory providing ghosts.
     */
    private final GhostFactory ghostFact;

    /**
     * Creates a new level factory.
     *
     * @param spriteStore
     *            The sprite store providing the sprites for units.
     * @param ghostFactory
     *            The factory providing ghosts.
     */
    public LevelFactory(PacManSprites spriteStore, GhostFactory ghostFactory) {
        this.sprites = spriteStore;
        this.ghostIndex = -1;
        this.ghostFact = ghostFactory;
    }

    /**
     * Creates a new level from the provided data.
     *
     * @param board
     *            The board with all ghosts and pellets occupying their squares.
     * @param abstractGhosts
     *            A list of all ghosts on the board.
     * @param startPositions
     *            A list of squares from which players may start the game.
     * @return A new level for the board.
     */
    public Level createLevel(Board board, List<AbstractGhost> abstractGhosts,
                             List<AbstractSquare> startPositions) {

        // We'll adopt the simple collision map for now.
        CollisionMap collisionMap = new PlayerCollisions();

        return new Level(board, abstractGhosts, startPositions, collisionMap);
    }

    /**
     * Creates a new ghost.
     *
     * @return The new ghost.
     */
    AbstractGhost createGhost() {
        ghostIndex++;
        ghostIndex %= GHOSTS;
        switch (ghostIndex) {
            case BLINKY:
                return ghostFact.createBlinky();
            case INKY:
                return ghostFact.createInky();
            case PINKY:
                return ghostFact.createPinky();
            case CLYDE:
                return ghostFact.createClyde();
            default:
                return new RandomAbstractGhost(sprites.getGhostSprite(GhostColor.RED));
        }
    }

    /**
     * Creates a new pellet.
     *
     * @return The new pellet.
     */
    public Pellet createPellet() {
        return new Pellet(PELLET_VALUE, sprites.getPelletSprite());
    }

    /**
     * Implementation of an NPC that wanders around randomly.

     */
    private static final class RandomAbstractGhost extends AbstractGhost {

        /**
         * The suggested delay between moves.
         */
        private static final long DELAY = 175L;

        /**
         * Creates a new random ghost.
         *
         * @param ghostSprite
         *            The sprite for the ghost.
         */
        RandomAbstractGhost(Map<Direction, Sprite> ghostSprite) {
            super(ghostSprite, (int) DELAY, 0);
        }

        @Override
        public Optional<Direction> nextAiMove() {
            return Optional.empty();
        }
    }
}
