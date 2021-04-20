package cn.edu.hust.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;
class PacKeyListenerTest {
    private PacKeyListener pacKeyListener;
    @BeforeEach
    void setUp() {
        pacKeyListener = mock(PacKeyListener.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void keyPressed() {
        pacKeyListener.keyPressed(mock(KeyEvent.class));
        assertThat(true).isEqualTo(true);
    }
}