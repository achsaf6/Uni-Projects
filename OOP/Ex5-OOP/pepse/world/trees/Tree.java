package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Tree class, creates infinite randomly generated trees
 */
public class Tree {
    //Constants
    private static final Color BARK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String LEAF_TAG = "Leaf";
    private static final int TREE_CREATION_CHANCE = 16; // Higher number = lower chance, Must be >= 0
    private static final int MIN_TREE_HEIGHT = 8;
    private static final int MAX_TREE_HEIGHT = 14;
    private static final int SMALL_TREETOP = 2;
    private static final int LARGE_TREETOP = 3;

    // fields
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> groundHeightFunction;
    private final int barkLayer;
    private final int leavesLayer;
    private final HashMap<Integer, List<GameObject>> createdTrees = new HashMap<>();
    private int curTreesStart;
    private int curTreesEnd;
    private final int seed;

    /**
     * Creates an instance of tree
     * @param gameObjects The game objects collection of the game
     * @param groundHeightFunction A function that receives a float x and return a float y that is the height
     *                             of the terrain at point x
     * @param barkLayer The layer of the tree bark
     * @param leavesLayer The layer of the leaves
     * @param seed Seed for random generation of the trees
     */
    public Tree (GameObjectCollection gameObjects, Function<Float, Float> groundHeightFunction,
                 int barkLayer, int leavesLayer, int seed) {
        this.gameObjects = gameObjects;
        this.groundHeightFunction = groundHeightFunction;
        this.barkLayer = barkLayer;
        this.leavesLayer = leavesLayer;
        this.seed = seed;
    }

    /**
     * Creates trees in the area between the min and max x values
     * @param minX min x value
     * @param maxX max x value
     */
    public void createInRange (int minX, int maxX) {

        minX = (int)(Math.floor(minX/Block.SIZE) * Block.SIZE);
        maxX = (int)Math.ceil(maxX/Block.SIZE) * Block.SIZE;

        removeOldTrees(minX, maxX);

        curTreesStart = minX;
        curTreesEnd = maxX;

        for (int x = minX; x < maxX; x+=Block.SIZE) {
            if (createdTrees.containsKey(x)) {
                addOrRemoveCreatedTree(x, gameObjects::addGameObject);
            }

            else  {
                Random rand = new Random(Objects.hash(x, seed));
                if (rand.nextInt(TREE_CREATION_CHANCE) == 0) plantTree(x, rand);
            }
        }
    }

    private void plantTree (int x, Random rand) {
        createdTrees.put(x, new ArrayList<>());

        int surfaceY = (int) Math.floor(groundHeightFunction.apply((float)x) /  Block.SIZE);

        int bottomBlockY = surfaceY * Block.SIZE - Block.SIZE;
        int height = 0;
        while (height <= MIN_TREE_HEIGHT) height = rand.nextInt(MAX_TREE_HEIGHT);

        // Create tree bark
        createTreeBark(x, bottomBlockY, height);

        int treetopSize = rand.nextBoolean() ? SMALL_TREETOP : LARGE_TREETOP;

        int topBlockY = surfaceY * Block.SIZE - Block.SIZE * height;
        // Create treetop
        createLeaves(x, rand, topBlockY, treetopSize);

        // Add the tree to the game
        addOrRemoveCreatedTree(x, gameObjects::addGameObject);
    }

    private void createLeaves(int x, Random rand, float topBlockY, int treetopSize) {
        for (int i = -treetopSize; i <= treetopSize; i++) {
            for (int j = -treetopSize; j <= treetopSize; j++) {
                Vector2 topLeftCorner = new Vector2(x + i * Block.SIZE, topBlockY + j * Block.SIZE);
                Renderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));

                Leaf newLeaf = new Leaf(topLeftCorner, blockRenderable, rand);
                newLeaf.setTag(LEAF_TAG);

                createdTrees.get(x).add(newLeaf);
            }
        }
    }

    private void createTreeBark(int x, float bottomBlockY, int height) {
        for (int i = 0; i < height; i++) {
            Vector2 topLeftCorner = new Vector2(x, bottomBlockY - i * Block.SIZE);
            Renderable blockRenderable = new RectangleRenderable(ColorSupplier.approximateColor(BARK_COLOR));

            Block newBlock = new Block(topLeftCorner, blockRenderable);

            createdTrees.get(x).add(newBlock);
        }
    }

    private void removeOldTrees(int newTreesStart, int newTreesEnd) {
        if (createdTrees.isEmpty()) return;

        for (int x = curTreesStart; x < newTreesStart; x += Block.SIZE) {
            if (createdTrees.containsKey(x)) {
                addOrRemoveCreatedTree(x, gameObjects::removeGameObject);
                createdTrees.remove(x);
            }
        }

        for (int x = newTreesEnd; x < curTreesEnd; x += Block.SIZE) {
            if (createdTrees.containsKey(x)) {
                addOrRemoveCreatedTree(x, gameObjects::removeGameObject);
                createdTrees.remove(x);
            }
        }
    }

    private void addOrRemoveCreatedTree (int x, BiConsumer<GameObject, Integer> addOrRemove) {
        if (!createdTrees.containsKey(x)) return;

        for (var gameObject : createdTrees.get(x)) {
            int layer = gameObject.getTag().equals(LEAF_TAG) ? leavesLayer : barkLayer;
            addOrRemove.accept(gameObject, layer);
        }
    }
}
