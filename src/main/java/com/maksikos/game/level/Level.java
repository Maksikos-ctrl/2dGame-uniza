package com.maksikos.game.level;

import com.maksikos.game.entities.Entity;
import com.maksikos.game.entities.PlayerMP;
import com.maksikos.game.gfx.Screen;
import com.maksikos.game.level.tiles.Tile;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

@Getter
@Setter
public class Level {

    private byte[] tiles;
    private int width;
    private int height;
    private List<Entity> entities = new ArrayList<>();
    private String imagePath;
    private BufferedImage image;

    public Level(String imagePath) {
        if (imagePath != null) {
            this.imagePath = imagePath;
            this.loadLevelFromFile();
        } else {
            this.width = 64;
            this.height = 64;
            tiles = new byte[width * height];
            this.generateLevel();
        }
    }

    private void loadLevelFromFile() {
        try {
            this.image = ImageIO.read(Level.class.getResource(this.imagePath));
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTiles() {
        int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCheck:
                for (Tile t : Tile.tiles) {
                    if (t != null && t.getLevelColour() == tileColours[x + y * width]) {
                        this.tiles[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void saveLevelToFile() {
        try {
            ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterTile(int x, int y, Tile newTile) {
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColour());
    }

    public void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 7) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                } else {
                    tiles[x + y * width] = Tile.STONE.getId();
                }
            }
        }
    }

    public void tick() {
        for (Entity e : entities) {
            e.tick();
        }

        for (Tile t : Tile.tiles) {
            if (t == null) {
                break;
            }
            t.tick();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0) xOffset = 0;
        if (xOffset > ((width << 3) - screen.getWidth())) xOffset = ((width << 3) - screen.getWidth());
        if (yOffset < 0) yOffset = 0;
        if (yOffset > ((height << 3) - screen.getHeight())) yOffset = ((height << 3) - screen.getHeight());

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.getHeight() >> 3) + 1; y++) {
            for (int x = (xOffset >> 3); x < (xOffset + screen.getWidth() >> 3) + 1; x++) {
                getTile(x, y).render(screen, this, x << 3, y << 3);
            }
        }
    }

    public void renderEntities(Screen screen) {
        for (Entity e : entities) {
            e.render(screen);
        }
    }

    public Tile getTile(int x, int y) {
        if (0 > x || x >= width || 0 > y || y >= height)
            return Tile.VOID;
        return Tile.tiles[tiles[x + y * width]];
    }

    public synchronized void addEntity(Entity entity) {
        entities.add(entity);
    }

    public synchronized void removePlayerMP(String username) {
        entities.removeIf(e -> e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username));
    }

    private int getPlayerMPIndex(String username) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        int index = getPlayerMPIndex(username);
        if (index != -1) {
            PlayerMP player = (PlayerMP) entities.get(index);
            player.setX(x);
            player.setY(y);
            player.setMoving(isMoving);
            player.setNumSteps(numSteps);
            player.setMovingDir(movingDir);
        }
    }
}