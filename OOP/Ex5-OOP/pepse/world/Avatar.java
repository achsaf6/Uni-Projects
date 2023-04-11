package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(50,50);
    private static final float MOVE_SPEED = 300;
    private static final int JUMP_SPEED = 300;
    private static final float ENERGY_CONSUMPTION_RATE = 0.5f;
    private static final float ENERGY_RECOVERY_RATE = 0.5f;
    private static final float INITIAL_ENERGY = 100;
    private final UserInputListener inputListener;
    private final AnimationRenderable[] animationRenderables;
    private float energy;

    private Avatar(Vector2 topLeftCorner, Renderable avatarRenderable, UserInputListener inputListener,
                   ImageReader imageReader) {
        super(topLeftCorner, AVATAR_DIMENSIONS, avatarRenderable);
        this.inputListener = inputListener;
        this.energy = INITIAL_ENERGY;
        this.animationRenderables = new AnimationRenderable[5];
        setAnimations(imageReader);
    }

    /**
     * This Converts the sprites to renderables and adds them to the list of possible animations
     * @param imageReader -- reads the images
     */
    private void setAnimations(ImageReader imageReader) {
//        Reference:
//        https://grafxkid.tumblr.com/pixels
        String[] idleAnimationFrames = {"assets/Kirby Idle 1.png",
                "assets/Kirby Idle 1.png",
                "assets/Kirby Idle 1.png",
                "assets/Kirby Idle 1.png",
                "assets/Kirby Idle 1.png",
                "assets/Kirby Idle 2.png",};
        this.animationRenderables[0] = new AnimationRenderable(idleAnimationFrames,
                imageReader,true,.2);

        String[] runAnimationFrames = {"assets/Kirby Run 1.png",
                "assets/Kirby Run 2.png",
                "assets/Kirby Run 3.png",
                "assets/Kirby Run 4.png",
                "assets/Kirby Run 5.png",
                "assets/Kirby Run 6.png"};
        this.animationRenderables[1] = new AnimationRenderable(runAnimationFrames,
                imageReader,true,.1);

        String[] fallAnimationFrames = {"assets/Kirby Fall 1.png",
                "assets/Kirby Fall 2.png"};
        this.animationRenderables[2] = new AnimationRenderable(fallAnimationFrames,
                imageReader, true,.1);

        String[] flyAnimationFrames = {"assets/Kirby Fly 1.png",
                "assets/Kirby Fly 2.png",
                "assets/Kirby Fly 3.png"};
        this.animationRenderables[3] = new AnimationRenderable(flyAnimationFrames,
                imageReader, true, .3);
    }

    /**
     * Creates and adds avatar game object to the game
     * @param gameObjects -- list of game objects to add the avatar to
     * @param layer -- layer to add the avatar to
     * @param topLeftCorner -- initial spawn point of avatat
     * @param inputListener -- checks which keys are pressed at a given time
     * @param imageReader -- reads given images
     * @return avatar game object
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader){
        Avatar avatar = new Avatar(topLeftCorner,null, inputListener, imageReader);
//        simulates effects of gravity
        avatar.transform().setAccelerationY(500);
//        prevents avatar from going through the ground
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.renderer().setRenderable(this.animationRenderables[0]);
        Vector2 moveDir = Vector2.ZERO;
//        sets animation to walk left
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            moveDir = moveDir.add(Vector2.LEFT);
            this.renderer().setIsFlippedHorizontally(true);
            this.renderer().setRenderable(this.animationRenderables[1]);
        }
//        sets animation to walk right
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            moveDir = moveDir.add(Vector2.RIGHT);
            this.renderer().setIsFlippedHorizontally(false);
            this.renderer().setRenderable(this.animationRenderables[1]);
        }
//        adds a single burst of flight if on ground (simulating a jump)
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && this.getVelocity().y() == 0){
            this.setVelocity(Vector2.UP.mult(JUMP_SPEED));
        }
//        adds bursts of flight so long as shift and space are pressed
        if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                && this.energy > 0){
            this.energy -= ENERGY_CONSUMPTION_RATE;
            this.setVelocity(Vector2.UP.mult(JUMP_SPEED));
        }
//        sets animation to idle
        if (moveDir.x() == 0){
            this.renderer().setRenderable(this.animationRenderables[0]);
        }
//        sets animation to falling
        if (this.getVelocity().y() > 0){
            this.renderer().setRenderable(this.animationRenderables[2]);
        }
//        sets animation to flying
        if (this.getVelocity().y() < 0){
            this.renderer().setRenderable(this.animationRenderables[3]);
        }
//        recovers energy so long as on the ground
        if (this.getVelocity().y() == 0 && this.energy < INITIAL_ENERGY){
            this.energy += ENERGY_RECOVERY_RATE;
        }
        this.setVelocity(new Vector2(moveDir.mult(MOVE_SPEED).x(),this.getVelocity().y()));
    }


}
