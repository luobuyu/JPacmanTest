package cn.edu.hust.board;


/**
 * A top-down view of a matrix of {@link AbstractSquare}s.
 * @author hewang
 */
public class Board {

    /**
     * The grid of squares with board[x][y] being the square at column x, row y.
     */
    private final AbstractSquare[][] board;

    /**
     * Creates a new board.
     *
     * @param grid
     *            The grid of squares with grid[x][y] being the square at column
     *            x, row y.
     */
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    Board(AbstractSquare[][] grid) {
        assert grid != null;
        this.board = grid;
        assert invariant() : "Initial grid cannot contain null squares";
    }

    /**
     * Whatever happens, the squares on the board can't be null.
     * @return false if any square on the board is null.
     */
    protected final boolean invariant() {
        for (AbstractSquare[] row : board) {
            for (AbstractSquare abstractSquare : row) {
                if (abstractSquare == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the number of columns.
     *
     * @return The width of this board.
     */
    public int getWidth() {
        return board.length;
    }

    /**
     * Returns the number of rows.
     *
     * @return The height of this board.
     */
    public int getHeight() {
        return board[0].length;
    }


    public AbstractSquare squareAt(int x, int y) {
        assert withinBorders(x, y);
        AbstractSquare result = board[x][y];
        assert result != null : "Follows from invariant.";
        return result;
    }

    public boolean withinBorders(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }
}
