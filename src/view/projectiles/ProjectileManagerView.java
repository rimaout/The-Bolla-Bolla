package view.projectiles;

import model.utilz.Constants;
import view.utilz.Load;
import model.projectiles.ProjectileManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.Projectiles.DEFAULT_H;
import static model.utilz.Constants.Projectiles.DEFAULT_W;

/**
 * The ProjectileManagerView class is responsible for managing {@link ProjectileView}s.
 * It handles loading sprites, updating, and drawing projectile views.
 */
public class ProjectileManagerView {
    private static ProjectileManagerView instance;
    private final ProjectileManagerModel projectileManagerModel;

    private BufferedImage[][] fireBallSprites;
    private BufferedImage[][] bubbleSprites;
    private BufferedImage[][] lightingSprites;

    private ArrayList<ProjectileView> projectilesViews = new ArrayList<>();

    /**
     * Private constructor to implement singleton pattern.
     * Initializes the projectile manager model and loads sprites.
     */
    private ProjectileManagerView() {
        this.projectileManagerModel = ProjectileManagerModel.getInstance();
        loadSprites();
    }

    /**
     * Returns the singleton instance of the ProjectileManagerView.
     *
     * @return the singleton instance
     */
    public static ProjectileManagerView getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerView();
        }
        return instance;
    }

    /**
     * Updates the animation state of all active projectile views.
     */
    public void update() {
        for (ProjectileView pv : projectilesViews)
            if (pv.isActive())
                pv.update();
    }

    /**
     * Draws all active projectile views.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        syncProjectilesViewsWithModel();

        for (ProjectileView pv : projectilesViews)
            if (pv.isActive())
                pv.draw(g);
    }

    /**
     * Synchronizes the projectile views with the projectile models.
     * Adds new projectile views if they are not already present.
     */
    private void syncProjectilesViewsWithModel() {

        for (var p : projectileManagerModel.getProjectileModels()) {

            if (projectilesViews.stream().noneMatch(pv -> pv.projectileModel.equals(p)))
                switch (p.getType()) {
                    case MAITA_FIREBALL -> projectilesViews.add(new MaitaFireProjectileView(p));
                    case PLAYER_BUBBLE -> projectilesViews.add(new PlayerBubbleProjectileView(p));
                    case LIGHTNING -> projectilesViews.add(new LightingProjectileView(p));
                }
        }
    }

    /**
     * Loads the sprites for different types of projectiles.
     */
    private void loadSprites() {
        fireBallSprites = new BufferedImage[2][4];
        BufferedImage temp = Load.GetSprite(Load.PROJECTILE_FIREBALL_SPRITE);
        for (int i = 0; i < fireBallSprites.length; i++)
            for (int j = 0; j < fireBallSprites[i].length; j++)
                fireBallSprites[i][j] = temp.getSubimage(j * DEFAULT_W, i * DEFAULT_H, DEFAULT_W, DEFAULT_H);

        bubbleSprites = new BufferedImage[2][4];
        temp = Load.GetSprite(Load.BUD_BUBBLE_PROJECTILE_SPRITE);
        for (int i = 0; i < bubbleSprites.length; i++)
            for (int j = 0; j < bubbleSprites[i].length; j++)
                bubbleSprites[i][j] = temp.getSubimage(j * DEFAULT_W, i * DEFAULT_H, DEFAULT_W, DEFAULT_H);

        lightingSprites = new BufferedImage[2][1];
        temp = Load.GetSprite(Load.LIGHTNING_BUBBLE_SPRITE);
        lightingSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        lightingSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);
    }

    /**
     * Resets the projectile views for a new level.
     */
    public void newLevelReset() {
        projectilesViews.clear();
    }

    /**
     * Resets the projectile views for a new play session.
     */
    public void newPlayReset() {
        projectilesViews.clear();
    }

    /**
     * Returns the sprites for the specified projectile type.
     *
     * @param type the type of the projectile
     * @return the sprites for the specified projectile type
     */
    public BufferedImage[][] getSprites(Constants.Projectiles.ProjectileType type) {
        return switch (type) {
            case MAITA_FIREBALL -> fireBallSprites;
            case PLAYER_BUBBLE -> bubbleSprites;
            case LIGHTNING -> lightingSprites;
        };
    }
}