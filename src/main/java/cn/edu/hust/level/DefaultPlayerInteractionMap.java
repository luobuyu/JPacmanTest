package cn.edu.hust.level;

import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.board.AbstractUnit;

/**
 * @author hewang
 */
public class DefaultPlayerInteractionMap implements CollisionMap {

    private final CollisionMap collisions = defaultCollisions();

    @Override
    public void collide(AbstractUnit mover, AbstractUnit movedInto) {
        collisions.collide(mover, movedInto);
    }

    /**
     * Creates the default collisions Player-Ghost and Player-Pellet.
     *
     * @return The collision map containing collisions for Player-Ghost and
     *         Player-Pellet.
     */
    private static CollisionInteractionMap defaultCollisions() {
        CollisionInteractionMap collisionMap = new CollisionInteractionMap();

        collisionMap.onCollision(Player.class, AbstractGhost.class,
            (player, ghost) -> player.setAlive(false));

        collisionMap.onCollision(Player.class, Pellet.class,
            (player, pellet) -> {
                pellet.leaveSquare();
                player.addPoints(pellet.getValue());
            });
        return collisionMap;
    }
}
