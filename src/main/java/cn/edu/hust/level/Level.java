package cn.edu.hust.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.board.Board;
import cn.edu.hust.board.Direction;
import cn.edu.hust.board.AbstractSquare;
import cn.edu.hust.board.AbstractUnit;

/**
 * A level of Pac-Man. A level consists of the board with the players and the
 * AIs on it.
 * @author ouyangwenzhe
 */
@SuppressWarnings("ALL")
public class Level {

    /**
     * The board of this level.
     */
    private final Board board;

    /**
     * The lock that ensures moves are executed sequential.
     */
    private final Object moveLock = new Object();

    /**
     * The lock that ensures starting and stopping can't interfere with each
     * other.
     */
    private final Object startStopLock = new Object();

    /**
     * The NPCs of this level and, if they are running, their schedules.
     */
    private final Map<AbstractGhost, ScheduledExecutorService> npcs;

    /**
     * <code>true</code> iff this level is currently in progress, i.e. players
     * and NPCs can move.
     */
    private boolean inProgress;

    /**
     * The squares from which players can start this game.
     */
    private final List<AbstractSquare> startAbstractSquares;

    /**
     * The start current selected starting square.
     */
    private int startSquareIndex;

    /**
     * The players on this level.
     */
    private final List<Player> players;

    /**
     * The table of possible collisions between units.
     */
    private final CollisionMap collisions;

    /**
     * The objects observing this level.
     */
    private final Set<LevelObserver> observers;

    /**
     * Creates a new level for the board.
     *
     * @param board
     *            The board for the level.
     * @param abstractGhosts
     *            The ghosts on the board.
     * @param startPositions
     *            The squares on which players start on this board.
     * @param collisionMap
     *            The collection of collisions that should be handled.
     */
    public Level(Board board, List<AbstractGhost> abstractGhosts, List<AbstractSquare> startPositions,
                 CollisionMap collisionMap) {
        assert board != null;
        assert abstractGhosts != null;
        assert startPositions != null;

        this.board = board;
        this.inProgress = false;
        this.npcs = new HashMap<>();
        for (AbstractGhost abstractGhost : abstractGhosts) {
            npcs.put(abstractGhost, null);
        }
        this.startAbstractSquares = startPositions;
        this.startSquareIndex = 0;
        this.players = new ArrayList<>();
        this.collisions = collisionMap;
        this.observers = new HashSet<>();
    }

    /**
     * Adds an observer that will be notified when the level is won or lost.
     *
     * @param observer
     *            The observer that will be notified.
     */
    public void addObserver(LevelObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer if it was listed.
     *
     * @param observer
     *            The observer to be removed.
     */
    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player
     *            The player to register.
     */
    public void registerPlayer(Player player) {
        assert player != null;
        assert !startAbstractSquares.isEmpty();

        if (players.contains(player)) {
            return;
        }
        players.add(player);
        AbstractSquare abstractSquare = startAbstractSquares.get(startSquareIndex);
        player.occupy(abstractSquare);
        startSquareIndex++;
        startSquareIndex %= startAbstractSquares.size();
    }

    /**
     * Returns the board of this level.
     *
     * @return The board of this level.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Moves the unit into the given direction if possible and handles all
     * collisions.
     *
     * @param abstractUnit
     *            The unit to move.
     * @param direction
     *            The direction to move the unit in.
     */
    public void move(AbstractUnit abstractUnit, Direction direction) {
        assert abstractUnit != null;
        assert direction != null;
        assert abstractUnit.hasSquare();

        if (!isInProgress()) {
            return;
        }

        synchronized (moveLock) {
            abstractUnit.setDirection(direction);
            AbstractSquare location = abstractUnit.getAbstractSquare();
            AbstractSquare destination = location.getSquareAt(direction);

            if (destination.isAccessibleTo(abstractUnit)) {
                List<AbstractUnit> occupants = destination.getOccupants();
                abstractUnit.occupy(destination);
                for (AbstractUnit occupant : occupants) {
                    collisions.collide(abstractUnit, occupant);
                }
            }
            updateObservers();
        }
    }

    /**
     * Starts or resumes this level, allowing movement and (re)starting the
     * NPCs.
     */
    public void start() {
        synchronized (startStopLock) {
            if (isInProgress()) {
                return;
            }
            startNPCs();
            inProgress = true;
            updateObservers();
        }
    }

    /**
     * Stops or pauses this level, no longer allowing any movement on the board
     * and stopping all NPCs.
     */
    public void stop() {
        synchronized (startStopLock) {
            if (!isInProgress()) {
                return;
            }
            stopNPCs();
            inProgress = false;
        }
    }

    /**
     * Starts all NPC movement scheduling.
     */
    @SuppressWarnings("AlibabaThreadPoolCreation")
    private void startNPCs() {
        for (final AbstractGhost npc : npcs.keySet()) {
            //noinspection AlibabaThreadPoolCreation
            @SuppressWarnings("AlibabaThreadPoolCreation") ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//构造一个线程池
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                    1, 1, 10, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(1),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
            threadPool.execute(() -> {
                try {
                    service.schedule(new NpcMoveTask(service, npc),
                            npc.getInterval() / 2, TimeUnit.MILLISECONDS);
                    npcs.put(npc, service);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    threadPool.shutdown();// 关闭线程池
                }
            });
        }

    }

    /**
     * Stops all NPC movement scheduling and interrupts any movements being
     * executed.
     */
    private void stopNPCs() {
        for (Entry<AbstractGhost, ScheduledExecutorService> entry : npcs.entrySet()) {
            ScheduledExecutorService schedule = entry.getValue();
            assert schedule != null;
            schedule.shutdownNow();
        }
    }

    /**
     * Returns whether this level is in progress, i.e. whether moves can be made
     * on the board.
     *
     * @return <code>true</code> iff this level is in progress.
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Updates the observers about the state of this level.
     */
    private void updateObservers() {
        if (!isAnyPlayerAlive()) {
            for (LevelObserver observer : observers) {
                observer.levelLost();
            }
        }
        if (remainingPellets() == 0) {
            for (LevelObserver observer : observers) {
                observer.levelWon();
            }
        }
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     *         alive.
     */
    public boolean isAnyPlayerAlive() {
        for (Player player : players) {
            if (player.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts the pellets remaining on the board.
     *
     * @return The amount of pellets remaining on the board.
     */
    public int remainingPellets() {
        Board board = getBoard();
        int pellets = 0;
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                for (AbstractUnit abstractUnit : board.squareAt(x, y).getOccupants()) {
                    if (abstractUnit instanceof Pellet) {
                        pellets++;
                    }
                }
            }
        }
        assert pellets >= 0;
        return pellets;
    }

    /**
     * A task that moves an NPC and reschedules itself after it finished.

     */
    private final class NpcMoveTask implements Runnable {

        /**
         * The service executing the task.
         */
        private final ScheduledExecutorService service;

        /**
         * The NPC to move.
         */
        private final AbstractGhost npc;

        /**
         * Creates a new task.
         *
         * @param service
         *            The service that executes the task.
         * @param npc
         *            The NPC to move.
         */
        NpcMoveTask(ScheduledExecutorService service, AbstractGhost npc) {
            this.service = service;
            this.npc = npc;
        }

        @Override
        public void run() {
            Direction nextMove = npc.nextMove();
            if (nextMove != null) {
                move(npc, nextMove);
            }
            long interval = npc.getInterval();
            service.schedule(this, interval, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * An observer that will be notified when the level is won or lost.

     */
    public interface LevelObserver {

        /**
         * The level has been won. Typically the level should be stopped when
         * this event is received.
         */
        void levelWon();

        /**
         * The level has been lost. Typically the level should be stopped when
         * this event is received.
         */
        void levelLost();
    }
}
