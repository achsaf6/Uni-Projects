package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Adds sky color to the background
     * @param gameObjects -- list of gameobjects in the game
     * @param windowDimentions -- size of the window
     * @param skyLayer -- the layer that the sky is added to
     * @return gameobject of the background
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimentions,
                                    int skyLayer){
        GameObject sky = new GameObject(Vector2.ZERO, windowDimentions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky,skyLayer);
        sky.setTag("sky");
        return sky;
    }
}
