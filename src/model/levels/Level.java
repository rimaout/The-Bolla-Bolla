package model.levels;

import model.entities.EnemyModel;
import model.utilz.Constants.Direction;
import model.bubbles.specialBubbles.BubbleGenerator;

import java.util.ArrayList;
import java.awt.image.BufferedImage;    // used for level map data

/**
 * Represents a level data.
 *
 * <p>The class handles the creation and management of level data, including
 * the level map, wind directions, bubble generator, and enemies.
 */
public class Level {

    private BufferedImage levelMap;
    private int[][] levelTileData;
    private Direction[][] windDirectionData;
    private BubbleGenerator bubbleGenerator;
    private ArrayList<EnemyModel> Enemies;

    /**
     * Constructs a new Level with the specified level map image.
     *
     * @param levelMap the image representing the level map
     */
    public Level(BufferedImage levelMap) {
        this.levelMap = levelMap;
        createLevelData();
        createWindDirectionData();
        createBubbleGenerator();
        createEnemies();
    }

    /**
     * Creates the level data from the level map image.
     */
    private void createLevelData() {
        levelTileData = LevelImportMethods.GetLevelTilesData(levelMap);
    }

    /**
     * Creates the wind direction data from the level map image.
     */
    private void createWindDirectionData() {
        windDirectionData = LevelImportMethods.GetWindsDirectionsData(levelMap);
    }

    /**
     * Creates the bubble generator from the level map image.
     */
    private void createBubbleGenerator() {
        bubbleGenerator = LevelImportMethods.GetBubbleGenerator(levelMap);
    }

    /**
     * Creates the enemies from the level map image.
     */
    private void createEnemies() {
        Enemies = LevelImportMethods.GetEnemies(levelMap);
    }

    /**
     * Resets the state of the level for a new game session.
     */
    public void newGameReset() {
        createEnemies();
        createBubbleGenerator();
    }

    // -------- Getters Methods --------

    /**
     * Returns the sprite index at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the sprite index at the specified coordinates
     */
    public int getTileSpriteIndex(int x, int y) {
        return levelTileData[y][x];
    }

    /**
     * Returns the level data.
     *
     * @return the level data
     */
    protected int[][] getLevelTileData() {
        return levelTileData;
    }

    /**
     * Returns the wind direction data.
     *
     * @return the wind direction data
     */
    protected Direction[][] getWindDirectionData() {
        return windDirectionData;
    }

    /**
     * Returns the bubble generator.
     *
     * @return the bubble generator
     */
    public BubbleGenerator getBubbleGenerator() {
        return bubbleGenerator;
    }

    /**
     * Returns the list of enemies.
     *
     * @return the list of enemies
     */
    public ArrayList<EnemyModel> getEnemies() {
        return Enemies;
    }
}