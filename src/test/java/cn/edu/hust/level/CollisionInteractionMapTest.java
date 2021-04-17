package cn.edu.hust.level;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.edu.hust.board.AbstractUnit;
import cn.edu.hust.npc.AbstractGhost;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author ouyangwenzhe
 */
class CollisionInteractionMapTest {

    private final CollisionInteractionMap collisionInteractionMap = mock(CollisionInteractionMap.class);
    private final CollisionInteractionMap.CollisionHandler handler = mock(CollisionInteractionMap.CollisionHandler.class);

    @AfterEach
    void tearDown() {
        System.out.print("test completed");
    }


    @Test
    void onCollision() {
        collisionInteractionMap.onCollision(Player.class,AbstractGhost.class,handler);
        verify(collisionInteractionMap).onCollision(Player.class,AbstractGhost.class,handler);
    }

}