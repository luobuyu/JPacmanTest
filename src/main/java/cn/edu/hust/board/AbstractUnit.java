package cn.edu.hust.board;

import cn.edu.hust.sprite.Sprite;

/**
 * @author jiangxihao
 */
public abstract class AbstractUnit {

    /**
     * The square this unit is currently occupying.
     */
    private AbstractSquare abstractSquare;

    /**
     * The direction this unit is facing.
     */
    private Direction direction;

    /**
     * Creates a unit that is facing east.
     */
    protected AbstractUnit() {
        this.direction = Direction.EAST;
    }

    /**
     * Sets this unit to face the new direction.
     * @param newDirection The new direction this unit is facing.
     */
    public void setDirection(Direction newDirection) {
        this.direction = newDirection;
    }

    /**
     * Returns the current direction this unit is facing.
     * @return The current direction this unit is facing.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Returns the square this unit is currently occupying.
     * Precondition: <code>hasSquare()</code>.
     *
     * @return The square this unit is currently occupying.
     */
    public AbstractSquare getAbstractSquare() {
        assert invariant();
        assert abstractSquare != null;
        return abstractSquare;
    }

    /**
     * Returns whether this unit is currently on  a square.
     *
     * @return True iff the unit is occupying a square at the moment.
     */
    public boolean hasSquare() {
        return abstractSquare != null;
    }

    /**
     * Occupies the target square iff this unit is allowed to as decided by
     * {@link AbstractSquare#isAccessibleTo(AbstractUnit)}.
     *
     * @param target
     *            The square to occupy.
     */
    public void occupy(AbstractSquare target) {
        assert target != null;

        if (abstractSquare != null) {
            abstractSquare.remove(this);
        }
        abstractSquare = target;
        target.put(this);
        assert invariant();
    }

    /**
     * Leaves the currently occupying square, thus removing this unit from the board.
     */
    public void leaveSquare() {
        if (abstractSquare != null) {
            abstractSquare.remove(this);
            abstractSquare = null;
        }
        assert invariant();
    }

    /**
     * Tests whether the square this unit is occupying has this unit listed as
     * one of its occupiers.
     *
     * @return <code>true</code> if the square this unit is occupying has this
     *         unit listed as one of its occupiers, or if this unit is currently
     *         not occupying any square.
     */
    protected boolean invariant() {
        return abstractSquare == null || abstractSquare.getOccupants().contains(this);
    }

    /**
     * Returns the sprite of this unit.
     *
     * @return The sprite of this unit.
     */
    public abstract Sprite getSprite();

    /**
     * A utility method for implementing the ghost AI.
     *
     * @param amountToLookAhead the amount of squares to follow this units direction in.
     * @return The square amountToLookAhead spaces in front of this unit.
     */
    public AbstractSquare squaresAheadOf(int amountToLookAhead) {
        Direction targetDirection = this.getDirection();
        AbstractSquare destination = this.getAbstractSquare();
        for (int i = 0; i < amountToLookAhead; i++) {
            destination = destination.getSquareAt(targetDirection);
        }

        return destination;
    }
}
