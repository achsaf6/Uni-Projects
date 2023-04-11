package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final Vector2 HALO_DIMENSIONS = new Vector2(300,300);

    /**
     * Creates and returns a circular gameObject that resembles a sun Halo. It follows the sun object
     * @param gameObjects -- list to add the halo to
     * @param layer -- layer to add the halo to
     * @param sun -- object to follow
     * @param color -- color of the halo
     * @return halo gameObject
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color){

        Renderable haloRenderable = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(Vector2.ZERO, HALO_DIMENSIONS, haloRenderable);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sun halo");
        return sunHalo;
    }
}
