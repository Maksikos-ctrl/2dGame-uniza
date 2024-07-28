package com.maksikos.game.level.tiles;
import com.maksikos.game.gfx.Screen;
import com.maksikos.game.level.Level;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
        super(id, x, y, tileColour, levelColour);
        this.solid = true;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        super.render(screen, level, x, y);
    }

    @Override
    public void tick() {
        super.tick();
    }
}