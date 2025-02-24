package com.rima.view.itemsAndRewards;

import com.rima.model.itemesAndRewards.PointsModel;

import java.awt.*;

import static com.rima.model.utilz.Constants.PointsManager.*;
import static com.rima.model.utilz.Constants.PointsManager.PointsType.SMALL;

/**
 * The PointsView class represents the view for the {@link PointsModel} class.
 * It handles drawing the points with a fading effect.
 */
public class PointsView {
    private final RewardPointsManagerView rewardPointsManager = RewardPointsManagerView.getInstance();
    private final PointsModel pointsModel;

    private float alpha = 1.0f; // Initial alpha value (used for transparency)

    /**
     * Constructs a PointsView with the specified PointsModel.
     *
     * @param pointsModel the model of the points item
     */
    public PointsView(PointsModel pointsModel) {
        this.pointsModel = pointsModel;
    }

    /**
     * Draws the points on the screen with a fading effect.
     *
     * @param g the Graphics2D object to draw with
     */
    public void draw(Graphics2D g) {
        fade();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // Set transparency

        if ( pointsModel.getType() == SMALL )
            g.drawImage(rewardPointsManager.getSmallPointsImage(pointsModel.getValue()), (int) pointsModel.getX(), (int) pointsModel.getY(), SMALL_W, SMALL_H, null);
        else
            g.drawImage(rewardPointsManager.getBigPointsImage(pointsModel.getValue()), (int) pointsModel.getX(), (int) pointsModel.getY(), BIG_W, BIG_H, null);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset transparency
    }

    /**
     * Reduces the alpha value to create a fading effect.
     */
    private void fade() {
        if (alpha - 0.009 > 0.0f)
            alpha -= 0.009f;
    }

    // ----------- Getters Methods ----------- //

    /**
     * Checks if the points item is active.
     *
     * @return true if the points item is active, false otherwise
     */
    protected boolean isActive() {
        return pointsModel.isActive();
    }

    /**
     * Returns the PointsModel associated with this view.
     *
     * @return the PointsModel
     */
    protected PointsModel getPointsModel() {
        return pointsModel;
    }
}
