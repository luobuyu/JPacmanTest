package cn.edu.hust.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.PacmanConfigurationException;
import cn.edu.hust.board.Board;
import cn.edu.hust.board.BoardFactory;
import cn.edu.hust.board.AbstractSquare;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * @author zhangxu
 */
public class MapParser {

    /**
     * The factory that creates the levels.
     */
    private final LevelFactory levelCreator;

    /**
     * The factory that creates the squares and board.
     */
    private final BoardFactory boardCreator;

    /**
     * Creates a new map parser.
     *
     * @param levelFactory
     *            The factory providing the NPC objects and the level.
     * @param boardFactory
     *            The factory providing the Square objects and the board.
     */
    public MapParser(LevelFactory levelFactory, BoardFactory boardFactory) {
        this.levelCreator = levelFactory;
        this.boardCreator = boardFactory;
    }

    /**
     * Parses the text representation of the board into an actual level.
     *
     * <ul>
     * <li>Supported characters:
     * <li>' ' (space) an empty square.
     * <li>'#' (bracket) a wall.
     * <li>'.' (period) a square with a pellet.
     * <li>'P' (capital P) a starting square for players.
     * <li>'G' (capital G) a square with a ghost.
     * </ul>
     *
     * @param map
     *            The text representation of the board, with map[x][y]
     *            representing the square at position x,y.
     * @return The level as represented by this text.
     */
    public Level parseMap(char[][] map) {
        int width = map.length;
        int height = map[0].length;

        AbstractSquare[][] grid = new AbstractSquare[width][height];

        List<AbstractGhost> abstractGhosts = new ArrayList<>();
        List<AbstractSquare> startPositions = new ArrayList<>();

        makeGrid(map, width, height, grid, abstractGhosts, startPositions);

        Board board = boardCreator.createBoard(grid);
        return levelCreator.createLevel(board, abstractGhosts, startPositions);
    }

    private void makeGrid(char[][] map, int width, int height,
                          AbstractSquare[][] grid, List<AbstractGhost> abstractGhosts, List<AbstractSquare> startPositions) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                char c = map[x][y];
                addSquare(grid, abstractGhosts, startPositions, x, y, c);
            }
        }
    }

    /**
     * Adds a square to the grid based on a given character. These
     * character come from the map files and describe the type
     * of square.
     *
     * @param grid
     *            The grid of squares with board[x][y] being the
     *            square at column x, row y.
     * @param abstractGhosts
     *            List of all ghosts that were added to the map.
     * @param startPositions
     *            List of all start positions that were added
     *            to the map.
     * @param x
     *            x coordinate of the square.
     * @param y
     *            y coordinate of the square.
     * @param c
     *            Character describing the square type.
     */
    protected void addSquare(AbstractSquare[][] grid, List<AbstractGhost> abstractGhosts,
                             List<AbstractSquare> startPositions, int x, int y, char c) {
        switch (c) {
            case ' ':
                grid[x][y] = boardCreator.createGround();
                break;
            case '#':
                grid[x][y] = boardCreator.createWall();
                break;
            case '.':
                AbstractSquare pelletAbstractSquare = boardCreator.createGround();
                grid[x][y] = pelletAbstractSquare;
                levelCreator.createPellet().occupy(pelletAbstractSquare);
                break;
            case 'G':
                AbstractSquare ghostAbstractSquare = makeGhostSquare(abstractGhosts, levelCreator.createGhost());
                grid[x][y] = ghostAbstractSquare;
                break;
            case 'P':
                AbstractSquare playerAbstractSquare = boardCreator.createGround();
                grid[x][y] = playerAbstractSquare;
                startPositions.add(playerAbstractSquare);
                break;
            default:
                throw new PacmanConfigurationException("Invalid character at "
                    + x + "," + y + ": " + c);
        }
    }

    /**
     * creates a Square with the specified ghost on it
     * and appends the placed ghost into the ghost list.
     *
     * @param abstractGhosts all the ghosts in the level so far, the new ghost will be appended
     * @param abstractGhost the newly created ghost to be placed
     * @return a square with the ghost on it.
     */
    protected AbstractSquare makeGhostSquare(List<AbstractGhost> abstractGhosts, AbstractGhost abstractGhost) {
        AbstractSquare ghostAbstractSquare = boardCreator.createGround();
        abstractGhosts.add(abstractGhost);
        abstractGhost.occupy(ghostAbstractSquare);
        return ghostAbstractSquare;
    }

    /**
     * Parses the list of strings into a 2-dimensional character array and
     * passes it on to {@link #parseMap(char[][])}.
     *
     * @param text
     *            The plain text, with every entry in the list being a equally
     *            sized row of squares on the board and the first element being
     *            the top row.
     * @return The level as represented by the text.
     * @throws PacmanConfigurationException If text lines are not properly formatted.
     */
    public Level parseMap(List<String> text) {

        checkMapFormat(text);

        int height = text.size();
        int width = text.get(0).length();

        char[][] map = new char[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = text.get(y).charAt(x);
            }
        }
        return parseMap(map);
    }

    /**
     * Check the correctness of the map lines in the text.
     * @param text Map to be checked
     * @throws PacmanConfigurationException if map is not OK.
     */
    private void checkMapFormat(List<String> text) {
        if (text == null) {
            throw new PacmanConfigurationException(
                "Input text cannot be null.");
        }

        if (text.isEmpty()) {
            throw new PacmanConfigurationException(
                "Input text must consist of at least 1 row.");
        }

        int width = text.get(0).length();

        if (width == 0) {
            throw new PacmanConfigurationException(
                "Input text lines cannot be empty.");
        }

        for (String line : text) {
            if (line.length() != width) {
                throw new PacmanConfigurationException(
                    "Input text lines are not of equal width.");
            }
        }
    }

    /**
     * Parses the provided input stream as a character stream and passes it
     * result to {@link #parseMap(List)}.
     *
     * @param source
     *            The input stream that will be read.
     * @return The parsed level as represented by the text on the input stream.
     * @throws IOException
     *             when the source could not be read.
     */
    public Level parseMap(InputStream source) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            source, "UTF-8"))) {
            List<String> lines = new ArrayList<>();
            while (reader.ready()) {
                lines.add(reader.readLine());
            }
            return parseMap(lines);
        }
    }

    /**
     * Parses the provided input stream as a character stream and passes it
     * result to {@link #parseMap(List)}.
     *
     * @param mapName
     *            Name of a resource that will be read.
     * @return The parsed level as represented by the text on the input stream.
     * @throws IOException
     *             when the resource could not be read.
     */
    @SuppressFBWarnings(value = "OBL_UNSATISFIED_OBLIGATION",
                        justification = "try with resources always cleans up")
    public Level parseMap(String mapName) throws IOException {
        try (InputStream boardStream = MapParser.class.getResourceAsStream(mapName)) {
            if (boardStream == null) {
                throw new PacmanConfigurationException("Could not get resource for: " + mapName);
            }
            return parseMap(boardStream);
        }
    }

    /**
     * @return the BoardCreator
     */
    protected BoardFactory getBoardCreator() {
        return boardCreator;
    }
}
