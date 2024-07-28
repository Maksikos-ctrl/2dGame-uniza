package com.maksikos.game.level.tiles;


import com.maksikos.game.gfx.Screen;
import com.maksikos.game.level.Level;

public class BasicTile extends Tile {

    protected int tileId;
    protected int tileColour;

    public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
        super(id, false, false, levelColour);
        this.tileId = x + y * 32;
        this.tileColour = tileColour;
    }

    @Override
    public void tick() {
        // Basic tiles don't need to update each tick
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileId, tileColour, 0x00, 1);
    }
}