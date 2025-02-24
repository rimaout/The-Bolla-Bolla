package com.rima.view.itemsAndRewards;

import com.rima.view.utilz.Load;
import com.rima.model.itemesAndRewards.RewardPointsManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.rima.model.utilz.Constants.PointsManager.*;

/**
 * The RewardPointsManagerView class manages the rendering of reward points in the game.
 * It handles loading the sprites for the points, synchronizing the view with the model, and drawing the points on the screen.
 */
public class RewardPointsManagerView {
    private static RewardPointsManagerView instance;

    private BufferedImage[][] smallPointsSprites;
    private BufferedImage[] bigPointsSprites;

    /**
     * Private constructor to prevent instantiation.
     * Loads the points sprites.
     */
    private final ArrayList<PointsView> pointsViewArray = new ArrayList<>();

    /**
     * Private constructor for singleton design patter implementation.
     * Loads the points sprites.
     */
    private RewardPointsManagerView() {
        loadPointsSprites();
    }

    /**
     * Returns the singleton instance of RewardPointsManagerView, creating it if necessary.
     *
     * @return the singleton instance of RewardPointsManagerView
     */
    public static RewardPointsManagerView getInstance() {
        if (instance == null) {
            instance = new RewardPointsManagerView();
        }
        return instance;

    }
    /**
     * Draws all active points on the screen.
     *
     * @param g the Graphics2D object to draw with
     */
    public void draw(Graphics2D g) {
        syncPointsViewsWithModel();

        for (PointsView p : pointsViewArray) {
            if (p.isActive())
                p.draw(g);
        }
    }


    /**
     * Synchronizes the points views with the model.
     * Adds new PointsView instances for any PointsModel instances that do not already have a corresponding view.
     */
    private void syncPointsViewsWithModel() {
        for (var p : RewardPointsManagerModel.getInstance().getPointsModelModelArray())
            if (pointsViewArray.stream().noneMatch(pv -> pv.getPointsModel().equals(p))) //STREAM USED
                pointsViewArray.add(new PointsView(p));
    }

    /**
     * Returns the sprite image for small points based on the value.
     *
     * @param value the value of the points
     * @return the BufferedImage for the small points
     */
    public BufferedImage getSmallPointsImage(int value) {
        return switch (value) {
            case 100 -> smallPointsSprites[0][0];
            case 200 -> smallPointsSprites[0][1];
            case 300 -> smallPointsSprites[0][2];
            case 400 -> smallPointsSprites[0][3];
            case 500 -> smallPointsSprites[0][4];
            case 600 -> smallPointsSprites[0][5];
            case 700 -> smallPointsSprites[0][6];
            case 800 -> smallPointsSprites[0][7];
            case 900 -> smallPointsSprites[0][8];
            case 1000 -> smallPointsSprites[1][0];
            case 2000 -> smallPointsSprites[1][1];
            case 3000 -> smallPointsSprites[1][2];
            case 4000 -> smallPointsSprites[1][3];
            case 5000 -> smallPointsSprites[1][4];
            case 6000 -> smallPointsSprites[1][5];
            case 7000 -> smallPointsSprites[1][6];
            case 8000 -> smallPointsSprites[1][7];
            case 9000 -> smallPointsSprites[1][8];
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Returns the sprite image for big points based on the value.
     *
     * @param value the value of the points
     * @return the BufferedImage for the big points
     */
    public BufferedImage getBigPointsImage(int value) {
        return switch (value) {
            case 1000 -> bigPointsSprites[0];
            case 2000 -> bigPointsSprites[1];
            case 3000 -> bigPointsSprites[2];
            case 4000 -> bigPointsSprites[3];
            case 6000 -> bigPointsSprites[4];
            case 8000 -> bigPointsSprites[5];
            case 10000 -> bigPointsSprites[6];
            case 16000 -> bigPointsSprites[7];
            case 32000 -> bigPointsSprites[8];
            case 64000 -> bigPointsSprites[9];
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Loads the sprites for small and big points from the sprite sheets.
     */
    private void loadPointsSprites() {
        // load small points sprites
        BufferedImage imgSmall = Load.GetSprite(Load.BUD_SMALL_POINTS_SPRITE);

        smallPointsSprites = new BufferedImage[2][9];
        for (int j = 0; j < smallPointsSprites.length; j++)
            for (int i = 0; i < smallPointsSprites[j].length; i++)
                smallPointsSprites[j][i] = imgSmall.getSubimage(i * SMALL_DEFAULT_W, j * SMALL_DEFAULT_H, SMALL_DEFAULT_W, SMALL_DEFAULT_H);

        // load big points sprites
        BufferedImage imgBig = Load.GetSprite(Load.BUD_BIG_POINTS_SPRITE);

        bigPointsSprites = new BufferedImage[10];
        for (int i = 0; i < bigPointsSprites.length; i++)
            bigPointsSprites[i] = imgBig.getSubimage(i * BIG_DEFAULT_W, 0, BIG_DEFAULT_W, BIG_DEFAULT_H);
    }

    /**
     * Resets the points view array for a new play session.
     */
    public void newPlayReset() {
        pointsViewArray.clear();
    }

    /**
     * Resets the points view array for a new level.
     */
    public void newLevelReset() {
        pointsViewArray.clear();
    }
}