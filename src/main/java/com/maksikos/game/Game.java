package com.maksikos.game;

import com.maksikos.game.entities.Player;
import com.maksikos.game.entities.PlayerMP;
import com.maksikos.game.gfx.Screen;
import com.maksikos.game.gfx.SpriteSheet;
import com.maksikos.game.handlers.InputHandler;
import com.maksikos.game.handlers.WindowHandler;
import com.maksikos.game.level.Level;
import com.maksikos.game.net.Client;
import com.maksikos.game.net.Server;
import com.maksikos.game.net.packets.PacketLogin;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Game extends Canvas implements Runnable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 360;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 3;
    public static final String NAME = "2d Multiplayer Game";
    public static final Dimension DIMENSIONS = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
    public static Game game;

    public JFrame frame;

    private Thread thread;

    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colours = new int[6 * 6 * 6];

    private Screen screen;
    public InputHandler input;
    public WindowHandler windowHandler;
    public Level level;
    public Player player;

    public Client socketClient;
    public Server socketServer;

    public boolean debug = true;
    public boolean isApplet = false;
    private BufferedImage backgroundImage = null;

//    public void init() {
//        game = this;
//        int index = 0;
//        for (int r = 0; r < 6; r++) {
//            for (int g = 0; g < 6; g++) {
//                for (int b = 0; b < 6; b++) {
//                    int rr = (r * 255 / 5);
//                    int gg = (g * 255 / 5);
//                    int bb = (b * 255 / 5);
//
//                    colours[index++] = rr << 16 | gg << 8 | bb;
//                }
//            }
//        }
//        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
//        input = new InputHandler(this);
//        level = new Level("/levels/water_test_level.png");
//        player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please enter a username"),
//                null, -1);
//        level.addEntity(player);
//        if (!isApplet) {
//            PacketLogin loginPacket = new PacketLogin(player.getUsername(), player.getX(), player.getY());
//            if (socketServer != null) {
//                socketServer.addConnection((PlayerMP) player, loginPacket);
//            }
//            loginPacket.writeData(socketClient);
//        }
//    }

    public void init() {
        game = this;
        int index = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);

                    colours[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new InputHandler(this);
        level = new Level("/levels/water_test_level.png");


        try {
            backgroundImage = ImageIO.read(Game.class.getResourceAsStream("/levels/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }




        String username;
        while (true) {
            username = JOptionPane.showInputDialog(this, "Please enter a username");
            if (username != null && !username.trim().isEmpty()) {
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Name can't be empty!!!!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        player = new PlayerMP(level, 100, 100, input, username, null, -1);
        level.addEntity(player);

        if (!isApplet) {
            PacketLogin loginPacket = new PacketLogin(player.getUsername(), player.getX(), player.getY());
            if (socketServer != null) {
                socketServer.addConnection((PlayerMP) player, loginPacket);
            }
            loginPacket.writeData(socketClient);
        }
    }


    public synchronized void start() {
        running = true;

        thread = new Thread(this, NAME + "_main");
        thread.start();
        if (!isApplet) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
                socketServer = new Server(this);
                socketServer.start();
            }

            socketClient = new Client(this, "localhost");
            socketClient.start();
        }
    }

    public synchronized void stop() {
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                debug(DebugLevel.INFO, ticks + " ticks, " + frames + " frames");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        level.tick();
    }


    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }




        int xOffset = player.getX() - (screen.getWidth() / 2);
        int yOffset = player.getY() - (screen.getHeight() / 2);

        level.renderTiles(screen, xOffset, yOffset);
        level.renderEntities(screen);

        for (int y = 0; y < screen.getHeight(); y++) {
            for (int x = 0; x < screen.getWidth(); x++) {
                int colourCode = screen.getPixels()[x + y * screen.getWidth()];
                if (colourCode < 255)
                    pixels[x + y * WIDTH] = colours[colourCode];
            }
        }

        Graphics g = bs.getDrawGraphics();
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static long fact(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * fact(n - 1);
        }
    }

    public void debug(DebugLevel level, String msg) {
        switch (level) {
            default:
            case INFO:
                if (debug) {
                    System.out.println("[" + NAME + "] " + msg);
                }
                break;
            case WARNING:
                System.out.println("[" + NAME + "] [WARNING] " + msg);
                break;
            case SEVERE:
                System.out.println("[" + NAME + "] [SEVERE]" + msg);
                this.stop();
                break;
        }
    }

    public static enum DebugLevel {
        INFO, WARNING, SEVERE;
    }
}