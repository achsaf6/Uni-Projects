package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A game object that looks like a block in the game, it is of constant size, and by default impassable
 * and immovable
 */
public class Block extends GameObject{
    public static final int SIZE = 30;

    /**
     * Creates an instance of a block
     * @param topLeftCorner The coordinates of the top left corner of the block
     * @param renderable The renderable of the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
