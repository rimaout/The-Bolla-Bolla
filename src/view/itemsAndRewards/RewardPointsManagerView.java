package view.itemsAndRewards;

import model.itemesAndRewards.RewardPointsManagerModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.PointsManager.*;

public class RewardPointsManagerView {
    private static RewardPointsManagerView instance;

    private BufferedImage[][] smallPointsSprites;
    private BufferedImage[] bigPointsSprites;

    private final ArrayList<PointsView> pointsViewArray = new ArrayList<>();

    private RewardPointsManagerView() {
        loadPointsSprites();
    }

    public static RewardPointsManagerView getInstance() {
        if (instance == null) {
            instance = new RewardPointsManagerView();
        }
        return instance;
    }

    public void draw(Graphics2D g) {
        syncPointsViewsWithModel();

        for (PointsView p : pointsViewArray) {
            if (p.isActive())
                p.draw(g);
        }
    }

    private void syncPointsViewsWithModel() {
        for (var p : RewardPointsManagerModel.getInstance().getPointsModelModelArray())
            if (pointsViewArray.stream().noneMatch(pv -> pv.getPointsModel().equals(p)))
                pointsViewArray.add(new PointsView(p));
    }

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

    private void loadPointsSprites() {
        // load small points sprites
        BufferedImage imgSmall = LoadSave.GetSprite(LoadSave.BUD_SMALL_POINTS_SPRITE);

        smallPointsSprites = new BufferedImage[2][9];
        for (int j = 0; j < smallPointsSprites.length; j++)
            for (int i = 0; i < smallPointsSprites[j].length; i++)
                smallPointsSprites[j][i] = imgSmall.getSubimage(i * SMALL_DEFAULT_W, j * SMALL_DEFAULT_H, SMALL_DEFAULT_W, SMALL_DEFAULT_H);

        // load big points sprites
        BufferedImage imgBig = LoadSave.GetSprite(LoadSave.BUD_BIG_POINTS_SPRITE);

        bigPointsSprites = new BufferedImage[10];
        for (int i = 0; i < bigPointsSprites.length; i++)
            bigPointsSprites[i] = imgBig.getSubimage(i * BIG_DEFAULT_W, 0, BIG_DEFAULT_W, BIG_DEFAULT_H);
    }

    public void newPlayReset() {
        pointsViewArray.clear();
    }

    public void newLevelReset() {
        pointsViewArray.clear();
    }
}
