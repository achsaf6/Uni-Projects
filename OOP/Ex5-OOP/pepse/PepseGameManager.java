package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;


/**
 * PepseGameManager class
 * the game can be run with the run method
 * the manager manages the pepse game
 */
public class PepseGameManager extends GameManager {
    // Layers
    public static final int GROUND_LAYER = -90;
    public static final int TREE_BARK_LAYER = -50;
    public static final int LEAVES_LAYER = Layer.DEFAULT + 2;
    public static final int AVATAR_LAYER = Layer.DEFAULT + 1;

    // Constants
    private static final float WINDOW_WIDTH = 1500;
    private static final float WINDOW_HEIGHT = 800;
    private static final int DAY_NIGHT_CYCLE_LENGTH = 30;
    private static final int SEED = 100;
    private static final String GAME_TITLE = "Pepse";
    private static final Color HALO_COLOR = new Color(255, 255, 0, 50);
    private static final Vector2 CAMERA_OFFSET = new Vector2(0, -WINDOW_HEIGHT / 9);
    private static final float SOFT_LANDING_THRESHOLD = 300;
    private static final float SOFT_LANDING_HEIGHT = 60;
    private static final float LANDING_RATE_OF_CHANGE = 50;


    // Fields
    private final Vector2 windowDimensions;
    private Avatar avatar;
    private Terrain terrain;
    private int leftWorldEdge;
    private int rightWorldEdge;
    private Tree tree;

    /**
     * The main method, creates an instance of the pepse game and runs it
     * @param args The command line args
     */
    public static void main(String[] args) {
        new PepseGameManager(GAME_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }

    /**
     * Creates an instance of PepseGameManager
     * @param windowTitle The title of the game, will appear in the top of the window
     * @param windowDimensions The dimensions of the game window
     */
    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
    }

    /**
     * Initializes the pepse game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 spawnPoint = new Vector2(windowDimensions.mult(.5f).x(),0);

//        creates and adds terrain
        leftWorldEdge = (int)(spawnPoint.x() - windowDimensions.x());
        rightWorldEdge = (int)(spawnPoint.x() + windowDimensions.x());

        terrain = new Terrain(gameObjects(), GROUND_LAYER, windowDimensions, SEED);
        terrain.createInRange(leftWorldEdge, rightWorldEdge);

//        creates and adds trees
        tree = new Tree(gameObjects(), terrain::groundHeightAt,TREE_BARK_LAYER, LEAVES_LAYER, SEED);
        tree.createInRange(leftWorldEdge, rightWorldEdge);

//        creates and adds sky
        Sky.create(gameObjects(),this.windowDimensions, Layer.BACKGROUND);

//        creates and adds day/night filter
        Night.create(gameObjects(), Layer.FOREGROUND, this.windowDimensions, DAY_NIGHT_CYCLE_LENGTH);

//        creates and adds sun
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND, this.windowDimensions,
                DAY_NIGHT_CYCLE_LENGTH);

//        creates and adds sun halo
        SunHalo.create(gameObjects(), Layer.BACKGROUND, sun, HALO_COLOR);

//        creates and adds avatar
        avatar = Avatar.create(gameObjects(),AVATAR_LAYER, spawnPoint, inputListener, imageReader);

        defineLayerCollisions();

        setCamera(new Camera(this.avatar, CAMERA_OFFSET, windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    private void defineLayerCollisions() {
        gameObjects().layers().shouldLayersCollide(GROUND_LAYER, LEAVES_LAYER, true);
        gameObjects().layers().shouldLayersCollide(LEAVES_LAYER, LEAVES_LAYER, false);
        gameObjects().layers().shouldLayersCollide(TREE_BARK_LAYER, AVATAR_LAYER, true);
        gameObjects().layers().shouldLayersCollide(GROUND_LAYER, AVATAR_LAYER, true);
    }

    /**
     * Overrides the default game manager update function
     * Generates new terrain and trees when necessary and applys a soft landing to the falling avatar
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float distFromLeftEdge = Math.abs(avatar.getCenter().x() - leftWorldEdge);
        float distFromRightEdge =  Math.abs(avatar.getCenter().x() - rightWorldEdge);

        if (distFromLeftEdge < WINDOW_WIDTH || distFromRightEdge < WINDOW_WIDTH) {

            leftWorldEdge = (int)(avatar.getCenter().x() - windowDimensions.x());
            rightWorldEdge = (int)(avatar.getCenter().x() + windowDimensions.x());

            terrain.createInRange(leftWorldEdge, rightWorldEdge);
            tree.createInRange(leftWorldEdge, rightWorldEdge);
        }

        softLanding();
    }

    private void softLanding() {
        float avatarDistanceFromGround = Math.abs((avatar.getTopLeftCorner().y() + avatar.getDimensions().y())
                - terrain.groundHeightAt(avatar.getCenter().x()));

        if (avatarDistanceFromGround < SOFT_LANDING_HEIGHT &&
                avatar.getVelocity().y() > SOFT_LANDING_THRESHOLD) {
            avatar.transform().setVelocityY(avatar.transform().getVelocity().y() - LANDING_RATE_OF_CHANGE);
        }
    }
}