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
                p.draw(g, getSprites(p.type));
    }

    public static ProjectileManager getInstance() {
        return instance;
    }

    public void update() {

        for (Projectile p : projectiles) {
            if (!p.isActive())
                continue;

            p.update();
            updateEntityHit(p);
        }
    }

    private void updateEntityHit(Projectile p) {

        if (p.type != ProjectileType.PLAYER_BUBBLE) {
            p.checkEntityHit(player);
        }

        else {
            for (Enemy e : EnemyManager.getInstance().getEnemies()) {
                if (!e.isActive() || e.isImmune())
                    continue;
                p.checkEntityHit(e);
            }
        }
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }


    public void resetNewGame() {
        projectiles.clear();
        playerProjectileSpeedMultiplier = 1;
        playerProjectileDistanceMultiplier = 1;
    }

    public void resetNewLevel() {
        projectiles.clear();
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
    }

    private BufferedImage[][] getSprites(ProjectileType type) {
        return switch (type) {
            case MAITA_FIREBALL -> fireBallSprites;
            case PLAYER_BUBBLE -> bubbleSprites;
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
