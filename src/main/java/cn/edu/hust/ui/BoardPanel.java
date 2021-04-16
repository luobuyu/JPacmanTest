package cn.edu.hust.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import cn.edu.hust.board.Board;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.board.AbstractUnit;
import cn.edu.hust.game.AbstractGame;

class BoardPanel extends JPanel {

    /**
     * Default serialisation ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The background colour of the board.
     */
    private static final Color BACKGROUND_COLOR = Color.BLACK;

    /**
     * The size (in pixels) of a square on the board. The initial size of this
     * panel will scale to fit a board with square of this size.
     */
    private static final int SQUARE_SIZE = 16;

    /**
     * The game to display.
     */
    private final AbstractGame abstractGame;

    /**
     * Creates a new board panel that will display the provided game.
     *
     * @param abstractGame
     *            The game to display.
     */
    BoardPanel(AbstractGame abstractGame) {
        super();
        assert abstractGame != null;
        this.abstractGame = abstractGame;

        Board board = abstractGame.getLevel().getBoard();

        int w = board.getWidth() * SQUARE_SIZE;
        int h = board.getHeight() * SQUARE_SIZE;

        Dimension size = new Dimension(w, h);
        setMinimumSize(size);
        setPreferredSize(size);
    }

    @Override
    public void paint(Graphics g) {
        assert g != null;
        render(abstractGame.getLevel().getBoard(), g, getSize());
    }

    /**
     * Renders the board on the given graphics context to the given dimensions.
     *
     * @param board
     *            The board to render.
     * @param graphics
     *            The graphics context to draw on.
     * @param window
     *            The dimensions to scale the rendered board to.
     */
    private void render(Board board, Graphics graphics, Dimension window) {
        int cellW = window.width / board.getWidth();
        int cellH = window.height / board.getHeight();

        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, window.width, window.height);

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                int cellX = x * cellW;
                int cellY = y * cellH;
                AbstractSquare abstractSquare = board.squareAt(x, y);
                render(abstractSquare, graphics, cellX, cellY, cellW, cellH);
            }
        }
    }

    /**
     * Renders a single square on the given graphics context on the specified
     * rectangle.
     *
     * @param abstractSquare
     *            The square to render.
     * @param graphics
     *            The graphics context to draw on.
     * @param x
     *            The x position to start drawing.
     * @param y
     *            The y position to start drawing.
     * @param width
     *            The width of this square (in pixels.)
     * @param height
     *            The height of this square (in pixels.)
     */
    private void render(AbstractSquare abstractSquare, Graphics graphics, int x, int y, int width, int height) {
        abstractSquare.getSprite().draw(graphics, x, y, width, height);
        for (AbstractUnit abstractUnit : abstractSquare.getOccupants()) {
            abstractUnit.getSprite().draw(graphics, x, y, width, height);
        }
    }
}
