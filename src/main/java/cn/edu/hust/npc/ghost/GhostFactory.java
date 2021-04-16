package cn.edu.hust.npc.ghost;

import cn.edu.hust.npc.AbstractGhost;
import cn.edu.hust.sprite.PacManSprites;

/**
 * @author ouyangwenzhe
 */
public class GhostFactory {

    /**
     * The sprite store containing the ghost sprites.
     */
    private final PacManSprites sprites;

    /**
     * Creates a new ghost factory.
     *
     * @param spriteStore The sprite provider.
     */
    public GhostFactory(PacManSprites spriteStore) {
        this.sprites = spriteStore;
    }

    /**
     * Creates a new Blinky / Shadow, the red Ghost.
     *
     * @see Blinky
     * @return A new Blinky.
     */
    public AbstractGhost createBlinky() {
        return new Blinky(sprites.getGhostSprite(GhostColor.RED));
    }

    /**
     * Creates a new Pinky / Speedy, the pink Ghost.
     *
     * @see Pinky
     * @return A new Pinky.
     */
    public AbstractGhost createPinky() {
        return new Pinky(sprites.getGhostSprite(GhostColor.PINK));
    }

    /**
     * Creates a new Inky / Bashful, the cyan Ghost.
     *
     * @see Inky
     * @return A new Inky.
     */
    public AbstractGhost createInky() {
        return new Inky(sprites.getGhostSprite(GhostColor.CYAN));
    }

    /**
     * Creates a new Clyde / Pokey, the orange Ghost.
     *
     * @see Clyde
     * @return A new Clyde.
     */
    public AbstractGhost createClyde() {
        return new Clyde(sprites.getGhostSprite(GhostColor.ORANGE));
    }
}
