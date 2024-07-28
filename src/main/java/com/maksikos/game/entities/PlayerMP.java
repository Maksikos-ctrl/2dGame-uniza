package com.maksikos.game.entities;

import com.maksikos.game.handlers.InputHandler;
import com.maksikos.game.level.Level;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;




@Getter
@Setter
public class PlayerMP extends Player {
    private InetAddress ipAddress;
    private int port;

    public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port) {
        super(level, x, y, input, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port) {
        super(level, x, y, null, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }
}
