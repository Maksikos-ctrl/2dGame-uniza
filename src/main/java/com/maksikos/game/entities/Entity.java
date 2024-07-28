package com.maksikos.game.entities;

import com.maksikos.game.gfx.Screen;
import com.maksikos.game.level.Level;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a game entity.
 * An entity is any object that exists within the game world.
 * Each entity has a position (x, y) and is associated with a level.
 */
@Getter
@Setter
public abstract class Entity {
    protected int x, y;  // The x and y coordinates of the entity in the game world
    protected Level level;  // The level in which the entity exists

    /**
     * Constructor for Entity.
     * @param level The level in which the entity will exist
     */
    public Entity(Level level) {
        this.level = level;
    }

    /**
     * Method to update the entity's state each game tick.
     * This method should be overridden by subclasses to provide specific behavior.
     */
    public abstract void tick();

    /**
     * Method to render the entity on the screen.
     * This method should be overridden by subclasses to provide specific rendering.
     * @param screen The screen on which the entity will be rendered
     */
    public abstract void render(Screen screen);
}