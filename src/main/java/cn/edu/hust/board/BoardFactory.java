package cn.edu.hust.board;

import cn.edu.hust.sprite.PacManSprites;
import cn.edu.hust.sprite.Sprite;

/**
 * A factory that creates {@link Board} objects from 2-dimensional arrays of
 * {@link AbstractSquare}s.
 *
 * @author hewang
 */
public class BoardFactory {

    /**
     * The sprite store providing the sprites for the background.
     */
    private final PacManSprites sprites;

    /**
     * Creates a new BoardFactory that will create a board with the provided
     * background sprites.
     *
     * @param spriteStore
     *            The sprite store providing the sprites for the background.
     */
    public BoardFactory(PacManSprites spriteStore) {
        this.sprites = spriteStore;
    }

    /**
     * Creates a new board from a grid of cells and connects it.
     *
     * @param grid
     *            The square grid of cells, in which grid[x][y] corresponds to
     *            the square at position x,y.
     * @return A new board, wrapping a grid of connected cells.
     */
    public Board createBoard(AbstractSquare[][] grid) {
        assert grid != null;

        Board board = new Board(grid);

        int width = board.getWidth();
        int height = board.getHeight();
        System.out.println(width);
        System.out.println(height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                AbstractSquare abstractSquare = grid[x][y];
                for (Direction dir : Direction.values()) {
                    int dirX = (width + x + dir.getDeltaX()) % width;
                    int dirY = (height + y + dir.getDeltaY()) % height;
                    AbstractSquare neighbour = grid[dirX][dirY];
                    abstractSquare.link(neighbour, dir);
                }
            }
        }

        return board;
    }

    /**
     * Creates a new square that can be occupied by any unit.
     *
     * @return A new square that can be occupied by any unit.
     */
    public AbstractSquare createGround() {
        return new Ground(sprites.getGroundSprite());
    }

    /**
     * Creates a new square that cannot be occupied by any unit.
     *
     * @return A new square that cannot be occupied by any unit.
     */
    public AbstractSquare createWall() {
        return new Wall(sprites.getWallSprite());
    }

    /**
     * A wall is a square that is inaccessible to anyone.

     */
    private static final class Wall extends AbstractSquare {

        /**
         * The background for this square.
         */
        private final Sprite background;

        /**
         * Creates a new wall square.
         *
         * @param sprite
         *            The background for the square.
         */
        Wall(Sprite sprite) {
            this.background = sprite;
        }

        @Override
        public boolean isAccessibleTo(AbstractUnit abstractUnit) {
            return false;
        }

        @Override
        public Sprite getSprite() {
            return background;
        }
    }


    private static final class Ground extends AbstractSquare {

        /**
         * The background for this square.
         */
        private final Sprite background;

        /**
         * Creates a new ground square.
         *
         * @param sprite
         *            The background for the square.
         */
        Ground(Sprite sprite) {
            this.background = sprite;
        }

        @Override
        public boolean isAccessibleTo(AbstractUnit abstractUnit) {
            return true;
        }

        @Override
        public Sprite getSprite() {
            return background;
        }
    }
}
