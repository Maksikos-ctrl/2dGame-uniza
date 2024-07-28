package com.maksikos.game.handlers;

import com.maksikos.game.Game;
import lombok.Getter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class InputHandler implements KeyListener {

    private final Map<Integer, Key> keyMappings = new HashMap<>();

    public InputHandler(Game game) {
        game.addKeyListener(this);
        keyMappings.put(KeyEvent.VK_W, up);
        keyMappings.put(KeyEvent.VK_UP, up);
        keyMappings.put(KeyEvent.VK_S, down);
        keyMappings.put(KeyEvent.VK_DOWN, down);
        keyMappings.put(KeyEvent.VK_A, left);
        keyMappings.put(KeyEvent.VK_LEFT, left);
        keyMappings.put(KeyEvent.VK_D, right);
        keyMappings.put(KeyEvent.VK_RIGHT, right);
    }

    @Getter
    public class Key {
        private int numTimesPressed = 0;
        private boolean pressed = false;

        public void toggle(boolean isPressed) {
            pressed = isPressed;
            if (isPressed) numTimesPressed++;
        }
    }

    public final Key up = new Key();
    public final Key down = new Key();
    public final Key left = new Key();
    public final Key right = new Key();

    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void toggleKey(int keyCode, boolean isPressed) {
        Key key = keyMappings.get(keyCode);
        if (key != null) {
            key.toggle(isPressed);
        }
    }
}
