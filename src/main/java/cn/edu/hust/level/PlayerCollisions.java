package cn.edu.hust.level;

import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.board.AbstractUnit;



/**
 * @author xurenjie
 */
public class PlayerCollisions implements CollisionMap {

    @Override
    public void collide(AbstractUnit mover, AbstractUnit collidedOn) {
        if (mover instanceof Player) {
            playerColliding((Player) mover, collidedOn);
        }
        else if (mover instanceof AbstractGhost) {
            ghostColliding((AbstractGhost) mover, collidedOn);
        }
        else if (mover instanceof Pellet) {
            pelletColliding((Pellet) mover, collidedOn);
        }
    }

    private void playerColliding(Player player, AbstractUnit collidedOn) {
        if (collidedOn instanceof AbstractGhost) {
            playerVersusGhost(player, (AbstractGhost) collidedOn);
        }
        if (collidedOn instanceof Pellet) {
            playerVersusPellet(player, (Pellet) collidedOn);
        }
    }

    private void ghostColliding(AbstractGhost abstractGhost, AbstractUnit collidedOn) {
        if (collidedOn instanceof Player) {
            playerVersusGhost((Player) collidedOn, abstractGhost);
        }
    }

    private void pelletColliding(Pellet pellet, AbstractUnit collidedOn) {
        if (collidedOn instanceof Player) {
            playerVersusPellet((Player) collidedOn, pellet);
        }
    }


    /**
     * Actual case of player bumping into ghost or vice versa.
     *
     * @param player The player involved in the collision.
     * @param abstractGhost The ghost involved in the collision.
     */
    public void playerVersusGhost(Player player, AbstractGhost abstractGhost) {
        player.setAlive(false);
    }

    /**
     * Actual case of player consuming a pellet.
     *
     * @param player The player involved in the collision.
     * @param pellet The pellet involved in the collision.
     */
    public void playerVersusPellet(Player player, Pellet pellet) {
        pellet.leaveSquare();
        player.addPoints(pellet.getValue());
    }

}
