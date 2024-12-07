package view.itemsAndRewards;

import model.itemesAndRewards.PointsModel;

import java.awt.*;

import static model.Constants.PointsManager.*;
import static model.Constants.PointsManager.BIG_H;
import static model.Constants.PointsManager.PointsType.SMALL;

public class PointsView {
    private final RewardPointsManagerView rewardPointsManager = RewardPointsManagerView.getInstance();
    private final PointsModel pointsModel;

    private float alpha = 1.0f; // Initial alpha value (for transparency)


    public PointsView(PointsModel pointsModel) {
        this.pointsModel = pointsModel;
    }

    public void draw(Graphics2D g) {
        fade();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // Set transparency

        if ( pointsModel.getType() == SMALL )
            g.drawImage(rewardPointsManager.getSmallPointsImage(pointsModel.getValue()), (int) pointsModel.getX(), (int) pointsModel.getY(), SMALL_W, SMALL_H, null);
        else
            g.drawImage(rewardPointsManager.getBigPointsImage(pointsModel.getValue()), (int) pointsModel.getX(), (int) pointsModel.getY(), BIG_W, BIG_H, null);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset transparency
    }

    private void fade() {
        alpha -= 0.009f;
    }

    protected boolean isActive() {
        return pointsModel.isActive();
    }

    protected PointsModel getPointsModel() {
        return pointsModel;
    }
}
