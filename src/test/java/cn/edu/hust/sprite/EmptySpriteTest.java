package cn.edu.hust.sprite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmptySpriteTest {

    private EmptySprite sprite;
    @BeforeEach
    void setUp() {
        sprite = new EmptySprite();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void split() {
        sprite.split(0, 0, 0, 0);
        assertThat(true).isEqualTo(true);
    }

    @Test
    void getWidth() {
        assertThat(sprite.getWidth()).isEqualTo(0);
    }

    @Test
    void getHeight() {
        assertThat(sprite.getHeight()).isEqualTo(0);
    }
}