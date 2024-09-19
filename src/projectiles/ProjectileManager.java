package projectiles;

import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.Projectiles.*;

public class ProjectileManager {
    private static ProjectileManager instance;
    private final Player player;

    private BufferedImage[][] fireBallSprites;
    private BufferedImage[][] bubbleSprites;
    private BufferedImage[][] lightingSprites;

    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    // PowerUp Multipliers
    private float playerProjectileSpeedMultiplier = 1;
    private float playerProjectileDistanceMultiplier = 1;

    private ProjectileManager(Player player) {
        loadSprites();
        this.player = player;
    }

    public static ProjectileManager getInstance(Player player) {
        if (instance == null) {
            instance = new ProjectileManager(player);
        }
        return instance;
    }

    public void draw(Graphics g) {
        for (Projectile p : projectiles)
            if (p.isActive())
                p.draw(g);
    }

    public static ProjectileManager getInstance() {
        return instance;
    }

    public void update() {

        for (Projectile p : projectiles) {
            if (!p.isActive())
                continue;

            p.update();
            updateEntityCollisions(p);
        }
    }

    private void updateEntityCollisions(Projectile p) {
        p.checkPlayerHit(player);

        for (Enemy enemy : EnemyManager.getInstance().getEnemies())
            p.checkEnemyHit(enemy, player);
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void newLevelReset() {
        projectiles.clear();
    }

    public void newPlayReset() {
        projectiles.clear();
        playerProjectileSpeedMultiplier = 1;
        playerProjectileDistanceMultiplier = 1;
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

    public BufferedImage[][] getSprites(ProjectileType type) {
        return switch (type) {
            case MAITA_FIREBALL -> fireBallSprites;
            case PLAYER_BUBBLE -> bubbleSprites;
            case LIGHTNING -> lightingSprites;
        };
    }

    public float getPlayerProjectileSpeedMultiplier() {
        return playerProjectileSpeedMultiplier;
    }

    public void setPlayerProjectileSpeedMultiplier(float playerProjectileSpeedMultiplier) {
        this.playerProjectileSpeedMultiplier = playerProjectileSpeedMultiplier;
    }

    public float getPlayerProjectileDistanceMultiplier() {
        return playerProjectileDistanceMultiplier;
    }

    public void setPlayerProjectileDistanceMultiplier(float playerProjectileDistanceMultiplier) {
        this.playerProjectileDistanceMultiplier = playerProjectileDistanceMultiplier;
    }
}
