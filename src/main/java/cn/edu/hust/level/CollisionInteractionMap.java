package cn.edu.hust.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hust.board.AbstractUnit;

/**
 * @author ouyangwenzhe
 */
@SuppressWarnings({"ALL", "AlibabaCollectionInitShouldAssignCapacity"})
public class CollisionInteractionMap implements CollisionMap {

    /**
     * The collection of collision handlers.
     */
    private final Map<Class<? extends AbstractUnit>,
        Map<Class<? extends AbstractUnit>, CollisionHandler<?, ?>>> handlers;

    /**
     * Creates a new, empty collision map.
     */
    public CollisionInteractionMap() {
        this.handlers = new HashMap<>();
    }

    /**
     * Adds a two-way collision interaction to this collection, i.e. the
     * collision handler will be used for both C1 versus C2 and C2 versus C1.
     *
     * @param <C1>
     *            The collider type.
     * @param <C2>
     *            The collidee (unit that was moved into) type.
     *
     * @param collider
     *            The collider type.
     * @param collidee
     *            The collidee type.
     * @param handler
     *            The handler that handles the collision.
     */
    public <C1 extends AbstractUnit, C2 extends AbstractUnit> void onCollision(
        Class<C1> collider, Class<C2> collidee, CollisionHandler<C1, C2> handler) {
        onCollision(collider, collidee, true, handler);
    }

    /**
     * Adds a collision interaction to this collection.
     *
     * @param <C1>
     *            The collider type.
     * @param <C2>
     *            The collidee (unit that was moved into) type.
     *
     * @param collider
     *            The collider type.
     * @param collidee
     *            The collidee type.
     * @param symetric
     *            <code>true</code> if this collision is used for both
     *            C1 against C2 and vice versa;
     *            <code>false</code> if only for C1 against C2.
     * @param handler
     *            The handler that handles the collision.
     */
    public <C1 extends AbstractUnit, C2 extends AbstractUnit> void onCollision(
        Class<C1> collider, Class<C2> collidee, boolean symetric,
        CollisionHandler<C1, C2> handler) {
        addHandler(collider, collidee, handler);
        if (symetric) {
            addHandler(collidee, collider, new InverseCollisionHandler<>(handler));
        }
    }

    /**
     * Adds the collision interaction..
     *
     * @param collider
     *            The collider type.
     * @param collidee
     *            The collidee type.
     * @param handler
     *            The handler that handles the collision.
     */
    @SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity")
    private void addHandler(Class<? extends AbstractUnit> collider,
                            Class<? extends AbstractUnit> collidee, CollisionHandler<?, ?> handler) {
        if (!handlers.containsKey(collider)) {
            //noinspection AlibabaCollectionInitShouldAssignCapacity
            handlers.put(collider, new HashMap<>(16));
        }

        Map<Class<? extends AbstractUnit>, CollisionHandler<?, ?>> map = handlers.get(collider);
        map.put(collidee, handler);
    }

    /**
     * Handles the collision between two colliding parties, if a suitable
     * collision handler is listed.
     *
     * @param <C1>
     *            The collider type.
     * @param <C2>
     *            The collidee (unit that was moved into) type.
     *
     * @param collider
     *            The collider.
     * @param collidee
     *            The collidee.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C1 extends AbstractUnit, C2 extends AbstractUnit> void collide(C1 collider,
                                                                           C2 collidee) {
        Class<? extends AbstractUnit> colliderKey = getMostSpecificClass(handlers, collider.getClass());
        if (colliderKey == null) {
            return;
        }

        Map<Class<? extends AbstractUnit>, CollisionHandler<?, ?>> map = handlers.get(colliderKey);
        Class<? extends AbstractUnit> collideeKey = getMostSpecificClass(map, collidee.getClass());
        if (collideeKey == null) {
            return;
        }

        CollisionHandler<C1, C2> collisionHandler = (CollisionHandler<C1, C2>) map.get(collideeKey);
        if (collisionHandler == null) {
            return;
        }

        collisionHandler.handleCollision(collider, collidee);
    }

    /**
     * Figures out the most specific class that is listed in the map. I.e. if A
     * extends B and B is listed while requesting A, then B will be returned.
     *
     * @param map
     *            The map with the key collection to find a matching class in.
     * @param key
     *            The class to search the most suitable key for.
     * @return The most specific class from the key collection.
     */
    private Class<? extends AbstractUnit> getMostSpecificClass(
            Map<Class<? extends AbstractUnit>, ?> map, Class<? extends AbstractUnit> key) {
        List<Class<? extends AbstractUnit>> collideeInheritance = getInheritance(key);
        for (Class<? extends AbstractUnit> pointer : collideeInheritance) {
            if (map.containsKey(pointer)) {
                return pointer;
            }
        }
        return null;
    }

    /**
     * Returns a list of all classes and interfaces the class inherits.
     *
     * @param clazz
     *            The class to create a list of super classes and interfaces
     *            for.
     * @return A list of all classes and interfaces the class inherits.
     */
    private List<Class<? extends AbstractUnit>> getInheritance(
        Class<? extends AbstractUnit> clazz) {
        List<Class<? extends AbstractUnit>> found = new ArrayList<>();
        found.add(clazz);

        int index = 0;
        while (found.size() > index) {
            Class<?> current = found.get(index);
            Class<?> superClass = current.getSuperclass();
            if (superClass != null && AbstractUnit.class.isAssignableFrom(superClass)) {
                found.add((Class<? extends AbstractUnit>) superClass);
            }
            for (Class<?> classInterface : current.getInterfaces()) {
                if (AbstractUnit.class.isAssignableFrom(classInterface)) {
                    found.add((Class<? extends AbstractUnit>) classInterface);
                }
            }
            index++;
        }

        return found;
    }

    /**
     * Handles the collision between two colliding parties
     *
     * @param <C1>
     *            The collider type.
     * @param <C2>
     *            The collidee type.
     */
    public interface CollisionHandler<C1 extends AbstractUnit, C2 extends AbstractUnit> {

        /**
         * Handles the collision between two colliding parties.
         *
         * @param collider
         *            The collider.
         * @param collidee
         *            The collidee.
         */
        void handleCollision(C1 collider, C2 collidee);
    }


    private static class InverseCollisionHandler<C1 extends AbstractUnit, C2 extends AbstractUnit>
        implements CollisionHandler<C1, C2> {

        /**
         * The handler of this collision.
         */
        private final CollisionHandler<C2, C1> handler;

        /**
         * Creates a new collision handler.
         *
         * @param handler
         *            The symmetric handler for this collision.
         */
        InverseCollisionHandler(CollisionHandler<C2, C1> handler) {
            this.handler = handler;
        }

        /**
         * Handles this collision by flipping the collider and collidee, making
         * it compatible with the initial collision.
         */
        @Override
        public void handleCollision(C1 collider, C2 collidee) {
            handler.handleCollision(collidee, collider);
        }
    }

}
