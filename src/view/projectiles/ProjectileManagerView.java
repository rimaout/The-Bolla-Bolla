package view.projectiles;

import model.utilz.Constants;
import model.utilz.LoadSave;
import model.projectiles.ProjectileManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.Projectiles.DEFAULT_H;
import static model.utilz.Constants.Projectiles.DEFAULT_W;

public class ProjectileManagerView {
    private static ProjectileManagerView instance;
    private final ProjectileManagerModel projectileManagerModel;

    private BufferedImage[][] fireBallSprites;
    private BufferedImage[][] bubbleSprites;
    private BufferedImage[][] lightingSprites;

    private ArrayList<ProjectileView> projectilesViews = new ArrayList<>();

    private ProjectileManagerView() {
        this.projectileManagerModel = ProjectileManagerModel.getInstance();
        loadSprites();
    }

    public static ProjectileManagerView getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerView();
        }
        return instance;
    }

    public void update() {
        for (ProjectileView pv : projectilesViews)
            if (pv.isActive())
                pv.update();
    }

    public void draw(Graphics g) {
        syncProjectilesViewsWithModel();

        for (ProjectileView pv : projectilesViews)
            if (pv.isActive())
                pv.draw(g);
    }

    private void syncProjectilesViewsWithModel() {

        for (var p : projectileManagerModel.getProjectileModels()) {

            // if a projectile is not in the view, add it
            if (projectilesViews.stream().noneMatch(pv -> pv.projectileModel.equals(p)))
                switch (p.getType()) {
                    case MAITA_FIREBALL -> projectilesViews.add(new MaitaFireProjectileView(p));
                    case PLAYER_BUBBLE -> projectilesViews.add(new PlayerBubbleProjectileView(p));
                    case LIGHTNING -> projectilesViews.add(new LightingProjectileView(p));
                }
        }
    }

    private void loadSprites() {
        fireBallSprites = new BufferedImage[2][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.PROJECTILE_FIREBALL_SPRITE);
        for (int i = 0; i < fireBallSprites.length; i++)
            for (int j = 0; j < fireBallSprites[i].length; j++)
                fireBallSprites[i][j] = temp.getSubimage(j * DEFAULT_W, i * DEFAULT_H, DEFAULT_W, DEFAULT_H);

        bubbleSprites = new BufferedImage[2][4];
        temp = LoadSave.GetSprite(LoadSave.BUD_BUBBLE_PROJECTILE_SPRITE);
        for (int i = 0; i < bubbleSprites.length; i++)
            for (int j = 0; j < bubbleSprites[i].length; j++)
                bubbleSprites[i][j] = temp.getSubimage(j * DEFAULT_W, i * DEFAULT_H, DEFAULT_W, DEFAULT_H);

        lightingSprites = new BufferedImage[2][1];
        temp = LoadSave.GetSprite(LoadSave.LIGHTNING_BUBBLE_SPRITE);
        lightingSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        lightingSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);
    }

    public void newLevelReset() {
        projectilesViews.clear();
    }

    public void newPlayReset() {
        projectilesViews.clear();
    }

    public BufferedImage[][] getSprites(Constants.Projectiles.ProjectileType type) {
        return switch (type) {
            case MAITA_FIREBALL -> fireBallSprites;
            case PLAYER_BUBBLE -> bubbleSprites;
            case LIGHTNING -> lightingSprites;
        };
    }
}
