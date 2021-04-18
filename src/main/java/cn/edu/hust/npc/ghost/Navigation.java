package cn.edu.hust.npc.ghost;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.hust.board.Board;
import cn.edu.hust.board.Direction;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.board.AbstractUnit;


/**
 * @author zhangxu
 */
public final class Navigation {

    private Navigation() {
    }

    /**
     * Calculates the shortest path. This is done by BFS. This search ensures
     * the traveller is allowed to occupy the squares on the way, or returns the
     * shortest path to the square regardless of terrain if no traveller is
     * specified.
     *
     * @param from
     *            The starting square.
     * @param to
     *            The destination.
     * @param traveller
     *            The traveller attempting to reach the destination. If
     *            traveller is set to <code>null</code>, this method will ignore
     *            terrain and find the shortest path whether it can actually be
     *            reached or not.
     * @return The shortest path to the destination or <code>null</code> if no
     *         such path could be found. When the destination is the current
     *         square, an empty list is returned.
     */
    public static List<Direction> shortestPath(AbstractSquare from, AbstractSquare to,
                                               AbstractUnit traveller) {
        if (from.equals(to)) {
            return new ArrayList<>();
        }

        List<Node> targets = new ArrayList<>();
        Set<AbstractSquare> visited = new HashSet<>();
        targets.add(new Node(null, from, null));
        while (!targets.isEmpty()) {
            Node node = targets.remove(0);
            AbstractSquare abstractSquare = node.getAbstractSquare();
            if (abstractSquare.equals(to)) {
                return node.getPath();
            }
            visited.add(abstractSquare);
            addNewTargets(traveller, targets, visited, node, abstractSquare);
        }
        return null;
    }

    private static void addNewTargets(AbstractUnit traveller, List<Node> targets,
                                      Set<AbstractSquare> visited, Node node, AbstractSquare abstractSquare) {
        for (Direction direction : Direction.values()) {
            AbstractSquare target = abstractSquare.getSquareAt(direction);
            if (traveller == null || target.isAccessibleTo(traveller)) {
                if (!visited.contains(target)) {
                    targets.add(new Node(direction, target, node));
                }
            }
        }
    }

    /**
     * Finds the nearest unit of the given type and returns its location. This
     * method will perform a breadth first search starting from the given
     * square.
     *
     * @param type
     *            The type of unit to search for.
     * @param currentLocation
     *            The starting location for the search.
     * @return The nearest unit of the given type, or <code>null</code> if no
     *         such unit could be found.
     */
    public static AbstractUnit findNearest(Class<? extends AbstractUnit> type,
                                           AbstractSquare currentLocation) {
        List<AbstractSquare> toDo = new ArrayList<>();
        Set<AbstractSquare> visited = new HashSet<>();

        toDo.add(currentLocation);

        while (!toDo.isEmpty()) {
            AbstractSquare abstractSquare = toDo.remove(0);
            AbstractUnit abstractUnit = findUnit(type, abstractSquare);
            if (abstractUnit != null) {
                assert abstractUnit.hasSquare();
                return abstractUnit;
            }
            visited.add(abstractSquare);
            for (Direction direction : Direction.values()) {
                AbstractSquare newTarget = abstractSquare.getSquareAt(direction);
                if (!visited.contains(newTarget) && !toDo.contains(newTarget)) {
                    toDo.add(newTarget);
                }
            }
        }
        return null;
    }

    /**
     *  Finds a subtype of Unit in a level.
     *  This method is very useful for finding the ghosts in the parsed map.
     *
     * @param clazz the type to search for.
     * @param board the board to find the unit in.
     * @param <T> the return type, same as the type in clazz.
     *
     * @return the first unit found of type clazz, or null.
     */
//    public static <T extends AbstractUnit> T findUnitInBoard(Class<T> clazz, Board board) {
//        for (int y = 0; y < board.getHeight(); y++) {
//            for (int x = 0; x < board.getWidth(); x++) {
//                final T ghost = Navigation.findUnit(clazz, board.squareAt(x, y));
//                if (ghost != null) {
//                    return ghost;
//                }
//            }
//        }
//
//        return null;
//    }

    /**
     * Determines whether a square has an occupant of a certain type.
     *
     * @param type
     *            The type to search for.
     * @param abstractSquare
     *            The square to search.
     * @param <T>
     *           the type of unit we searched for.
     *
     * @return A unit of type T, iff such a unit occupies this square, or
     *         <code>null</code> of none does.
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractUnit> T findUnit(Class<T> type, AbstractSquare abstractSquare) {
        for (AbstractUnit abstractUnit : abstractSquare.getOccupants()) {
            if (type.isInstance(abstractUnit)) {
                assert abstractUnit.hasSquare();
                return (T) abstractUnit;
            }
        }
        return null;
    }


    private static final class Node {

        /**
         * The direction for this node, which is <code>null</code> for the root
         * node.
         */
        private final Direction direction;

        /**
         * The parent node, which is <code>null</code> for the root node.
         */
        private final Node parent;

        /**
         * The square associated with this node.
         */
        private final AbstractSquare abstractSquare;

        /**
         * Creates a new node.
         *
         * @param direction
         *            The direction, which is <code>null</code> for the root
         *            node.
         * @param abstractSquare
         *            The square.
         * @param parent
         *            The parent node, which is <code>null</code> for the root
         *            node.
         */
        Node(Direction direction, AbstractSquare abstractSquare, Node parent) {
            this.direction = direction;
            this.abstractSquare = abstractSquare;
            this.parent = parent;
        }

        /**
         * @return The direction for this node, or <code>null</code> if this
         *         node is a root node.
         */
        private Direction getDirection() {
            return direction;
        }

        /**
         * @return The square for this node.
         */
        private AbstractSquare getAbstractSquare() {
            return abstractSquare;
        }

        /**
         * @return The parent node, or <code>null</code> if this node is a root
         *         node.
         */
        private Node getParent() {
            return parent;
        }

        /**
         * Returns the list of values from the root of the tree to this node.
         *
         * @return The list of values from the root of the tree to this node.
         */
        private List<Direction> getPath() {
            if (parent == null) {
                return new ArrayList<>();
            }
            List<Direction> path = parent.getPath();
            path.add(getDirection());
            return path;
        }
    }
}
