package cn.edu.hust.board;

import cn.edu.hust.sprite.Sprite;

class BasicAbstractSquare extends AbstractSquare {

    /**
     * Creates a new basic square.
     */
    BasicAbstractSquare() {
        super();
    }

    @Override
    public boolean isAccessibleTo(AbstractUnit abstractUnit) {
        return true;
    }

    @Override
    @SuppressWarnings("return.type.incompatible")
    public Sprite getSprite() {
        return null;
    }
}
