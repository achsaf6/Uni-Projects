package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Terrain class, creates randomly generated infinite terrain
 */
public class Terrain {
    // Constants
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 16;
    public static final String GROUND_TAG = "Ground";
    private static final float GROUND_HEIGHT_RELATIVE_PIVOT_POINT = 0.25f;

    // Fields
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final int undergroundLayer;
    private final HashMap<Integer, GameObject[]> createdObjects = new HashMap<>();
    private int curTerrainStart;
    private int curTerrainEnd;
    private final NoiseGenerator noiseGenerator;

    /**
     * creates a new instance of terrain
     * @param gameObjects The collection of game objects in the game
     * @param groundLayer the layer the ground is on in the game
     * @param windowDimensions The dimensions of the window displaying the game
     * @param seed A seed for the random number generator
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.undergroundLayer = groundLayer + 1;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed);
    }

    /**
     * Gets the height the ground should be at place x
     * @param x The x coordinate
     * @return The height the ground should be at x
     */
    public float groundHeightAt (float x) {
        x = (float) Math.floor(x/Block.SIZE);

        float maxHeight = TERRAIN_DEPTH * Block.SIZE;

        float pivotPointHeight = maxHeight * GROUND_HEIGHT_RELATIVE_PIVOT_POINT;

        float pivotPoint = windowDimensions.y() - pivotPointHeight;

        float addToPivotY = (float) noiseGenerator.noise(x);

        return pivotPoint + addToPivotY * pivotPointHeight;
    }

    /**
     * Creates terrain that covers the area between the min and max x values
     * @param minX min x value
     * @param maxX max x value
     */
    public void createInRange (int minX, int maxX) {

        minX = (int)Math.floor(minX/Block.SIZE) * Block.SIZE;
        maxX = (int)Math.ceil(maxX/Block.SIZE) * Block.SIZE;

        removeOldTerrain (minX, maxX);

        curTerrainStart = minX;
        curTerrainEnd = maxX;

        for (int xCoord = minX; xCoord < maxX; xCoord += Block.SIZE) {

            if (!createdObjects.containsKey(xCoord)) {
                createNewTerrainColumn(xCoord);
            }
            addOrRemoveTerrainColumn(xCoord, gameObjects::addGameObject);
        }
    }

    private void createNewTerrainColumn(int xCoord) {
        createdObjects.put(xCoord, new GameObject[TERRAIN_DEPTH]);

        int topBlockY = (int) Math.floor(groundHeightAt(xCoord) / Block.SIZE) * Block.SIZE;

        for (int i = 0; i < TERRAIN_DEPTH; i++) {
            int yCoord = topBlockY + i * Block.SIZE;

            Vector2 blockTopCorner = new Vector2(xCoord, yCoord);
            Renderable blockRenderable =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

            Block newBlock = new Block(blockTopCorner, blockRenderable);
            newBlock.setTag(GROUND_TAG);

            createdObjects.get(xCoord)[i] = newBlock;
        }
    }

    private void removeOldTerrain(int newTerrainStart, int newTerrainEnd) {
        if (createdObjects.isEmpty()) return;

        for (int xCoord = curTerrainStart; xCoord < newTerrainStart; xCoord += Block.SIZE) {
            if (createdObjects.containsKey(xCoord)) {
                addOrRemoveTerrainColumn(xCoord, gameObjects::removeGameObject);
                createdObjects.remove(xCoord);
            }
        }

        for (int xCoord = newTerrainEnd; xCoord< curTerrainEnd; xCoord += Block.SIZE) {
            if (createdObjects.containsKey(xCoord)) {
                addOrRemoveTerrainColumn(xCoord, gameObjects::removeGameObject);
                createdObjects.remove(xCoord);
            }
        }
    }

    private void addOrRemoveTerrainColumn(int xCoord,
                                          BiConsumer<GameObject, Integer> addOrRemove) {
        var groundCol = createdObjects.get(xCoord);
        for (int i = 0; i < groundCol.length; i++) {
            int layer = i == 0 ? groundLayer : undergroundLayer;
            addOrRemove.accept(groundCol[i], layer);
        }
    }
}
