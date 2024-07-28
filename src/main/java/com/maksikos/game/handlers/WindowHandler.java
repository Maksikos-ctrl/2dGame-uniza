package com.maksikos.game.handlers;

import com.maksikos.game.Game;
import com.maksikos.game.net.packets.PacketDisconnect;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener {

    private final Game game;

    public WindowHandler(Game game) {
        this.game = game;
        this.game.frame.addWindowListener(this);
    }

    @Override
    public void windowActivated(WindowEvent event) {}

    @Override
    public void windowClosed(WindowEvent event) {}

    @Override
    public void windowClosing(WindowEvent event) {
        new PacketDisconnect(this.game.player.getUsername())
                .writeData(this.game.socketClient);
    }

    @Override
    public void windowDeactivated(WindowEvent event) {}

    @Override
    public void windowDeiconified(WindowEvent event) {}

    @Override
    public void windowIconified(WindowEvent event) {}

    @Override
    public void windowOpened(WindowEvent event) {}
}
