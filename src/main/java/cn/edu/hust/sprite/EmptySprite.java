package cn.edu.hust.sprite;

import java.awt.Graphics;

/**
 * @author ouyangwenzhe
 */
public class EmptySprite implements Sprite {

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        // nothing to draw.
    }

    @Override
    public Sprite split(int x, int y, int width, int height) {
        return new EmptySprite();
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

}
