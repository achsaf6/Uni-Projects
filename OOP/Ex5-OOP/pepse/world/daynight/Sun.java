package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final Vector2 SUN_DIMENSIONS = new Vector2(50,50);
    private static final float SUN_ORBIT = 300;

    /**
     * Creates and returns a gameobject that rotates around the center of the screen and looks like a sun
     * @param gameObjects -- list to add the sun to
     * @param layer =-- layer to add the sun to
     * @param windowDimensions -- window dimensions, used for placement
     * @param cycleLength -- time it takes to do a complete cycle in the sky
     * @return sun object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength){
        Renderable sunRenderable = new OvalRenderable(Color.yellow);
        GameObject sun = new GameObject(Vector2.ZERO, SUN_DIMENSIONS,
                sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        new Transition<>(sun, angle -> {
//            calculates distance from coordinate space based on float that represents angle from
//            Vector.UP
            double theta = angle*2*Math.PI;
            float x = (float) (SUN_ORBIT*Math.sin(theta))*4 + windowDimensions.mult(.5f).x();
            float y = (float) (-SUN_ORBIT*Math.cos(theta)) + windowDimensions.mult(.5f).y();
            sun.setCenter(new Vector2(x,y+200));
        }, 0f, 1f,Transition.LINEAR_INTERPOLATOR_FLOAT,cycleLength*2,
                Transition.TransitionType.TRANSITION_LOOP,null);
        sun.setTag("sun");
        return sun;
    }
}
