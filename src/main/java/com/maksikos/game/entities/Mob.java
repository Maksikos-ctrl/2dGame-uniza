package com.maksikos.game.entities;

import com.maksikos.game.level.Level;
import com.maksikos.game.level.tiles.Tile;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a mobile entity in the game. This includes any
 * character or creature that can move within the game world.
 */
@Getter
@Setter
public abstract class Mob extends Entity {
    protected String name;  // The name of the mob
    protected int speed;    // Movement speed of the mob
    protected int numSteps = 0; // Number of steps taken
    protected boolean isMoving; // Flag indicating if the mob is moving
    protected int movingDir = 1; // Direction the mob is moving in (0=up, 1=down, 2=left, 3=right)
    protected int scale = 1;     // Scaling factor for rendering

    /**
     * Constructor to initialize the mob with its level, name, position, and speed.
     *
     * @param level The level in which the mob is present.
     * @param name The name of the mob.
     * @param x The initial x position.
     * @param y The initial y position.
     * @param speed The speed of the mob.
     */
    public Mob(Level level, String name, int x, int y, int speed) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    /**
     * Moves the mob by the specified amount in the x and y directions.
     *
     * @param xa The amount to move in the x direction.
     * @param ya The amount to move in the y direction.
     */
    public void move(int xa, int ya) {
        if (xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }
        numSteps++;
        if (!hasCollided(xa, ya)) {
            if (ya < 0) movingDir = 0; // Moving up
            if (ya > 0) movingDir = 1; // Moving down
            if (xa < 0) movingDir = 2; // Moving left
            if (xa > 0) movingDir = 3; // Moving right
            x += xa * speed;
            y += ya * speed;
        }
    }

    /**
     * Abstract method to check if the mob has collided with something in the game world.
     * This method must be implemented by subclasses.
     *
     * @param xa The x movement amount.
     * @param ya The y movement amount.
     * @return True if a collision is detected, false otherwise.
     */
    public abstract boolean hasCollided(int xa, int ya);

    /**
     * Checks if the tile at the new position is solid, indicating a collision.
     *
     * @param xa The x movement amount.
     * @param ya The y movement amount.
     * @param x The x offset from the mob's current position.
     * @param y The y offset from the mob's current position.
     * @return True if the tile is solid, false otherwise.
     */
    protected boolean isSolidTile(int xa, int ya, int x, int y) {
        if (level == null) return false;
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        return !lastTile.equals(newTile) && newTile.isSolid();
    }
}
