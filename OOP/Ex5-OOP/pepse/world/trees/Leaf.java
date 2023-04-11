package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.Random;

/**
 * Leaf class, A block that has leaf animations
 */
public class Leaf extends Block {
    // Constants
    private static final float FADE_OUT_TIME = 5;
    private static final float FALL_SPEED = 20;
    private static final float FADE_IN_TIME = 0.5f;
    private static final int MAX_LIFETIME = 90;
    private static final Float SWAY_VELOCITY = 60f;
    private static final float SWAY_SPEED = 1.5f;
    private static final Float INITIAL_LEAF_ANGLE = 0f;
    private static final Float FINAL_LEAF_ANGLE = 8f;
    private static final float WIND_TRANSITION_TIME = 1;
    private static final float LEAF_SIZE_TRANSITION_FACTOR = 0.9f;
    private static final float LEAF_MASS = 20;

    // Fields
    private final Vector2 topLeftCorner;
    private final Random rand;
    private Transition<Float> swayTransition;
    private boolean startedFading;

    /**
     * Creates an instance of a leaf
     * @param topLeftCorner The coordinates of the top left corner of the block
     * @param renderable The renderable of the block
     * @param rand A random number generator
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable, Random rand) {
        super(topLeftCorner, renderable);
        this.topLeftCorner = topLeftCorner;
        this.rand = rand;
        new ScheduledTask(this,
                rand.nextFloat(), false, this::startTransitions);
        physics().setMass(LEAF_MASS);
        birthLeaf();
    }

    private void birthLeaf() {
        startedFading = false;
        if (renderer().getOpaqueness() == 0) renderer().fadeIn(FADE_IN_TIME);

        setVelocity(Vector2.ZERO);
        transform().setTopLeftCorner(topLeftCorner);

        int deathTime = rand.nextInt(MAX_LIFETIME);

        new ScheduledTask(this, deathTime, false, this::killLeaf);
    }

    private void killLeaf () {
        transform().setVelocityY(FALL_SPEED);

        swayTransition = new Transition<>(this,
                transform()::setVelocityX,
                -SWAY_VELOCITY, SWAY_VELOCITY, Transition.CUBIC_INTERPOLATOR_FLOAT, SWAY_SPEED,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private void startTransitions () {
        new Transition<>(this,
                renderer()::setRenderableAngle, INITIAL_LEAF_ANGLE, FINAL_LEAF_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT, WIND_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        Vector2 leafSize = new Vector2(Block.SIZE, Block.SIZE);

        new Transition<>(this,
                this::setDimensions, leafSize, leafSize.mult(LEAF_SIZE_TRANSITION_FACTOR),
                Transition.CUBIC_INTERPOLATOR_VECTOR, WIND_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * If the leaf collides with the ground stops the side to side sway
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(Terrain.GROUND_TAG) && !startedFading){
            removeComponent(swayTransition);
            update(0);
            transform().setVelocity(Vector2.ZERO);
            startedFading = true;
            renderer().fadeOut(FADE_OUT_TIME, this::birthLeaf);
        }
    }
}
