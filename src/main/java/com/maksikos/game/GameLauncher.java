package com.maksikos.game;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GameLauncher {

    private static final Game game = new Game();
    public static final boolean DEBUG = false;

    private static void setupGame() {
        game.setPreferredSize(Game.DIMENSIONS);
        game.setMaximumSize(Game.DIMENSIONS);
        game.setMinimumSize(Game.DIMENSIONS);
        game.frame = new JFrame(Game.NAME);
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLayout(new BorderLayout());
        game.frame.add(game, BorderLayout.CENTER);
        game.frame.pack();
        game.frame.setResizable(false);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        game.debug = DEBUG;
    }

    public static void main(String[] args) {
        setupGame();
        game.start();
    }
}
