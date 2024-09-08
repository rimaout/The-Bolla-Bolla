package entities;

import utilz.LoadSave;
import static utilz.Constants.Projectiles.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ProjectileManager {
    private static ProjectileManager instance;
    private Player player;

    private BufferedImage[][] fireBallSprites;

    private ArrayList<Projectile> projectiles = new ArrayList<>();

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

    public static ProjectileManager getInstance() {
        return instance;
    }

    public void update() {
        for (Projectile p : projectiles)
            if (p.isActive())
                p.update(player);
    }

    public void draw(Graphics g) {
        for (Projectile p : projectiles)
            if (p.isActive())
                p.draw(g, getSprites(p));
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void resetAll() {
        projectiles.clear();
    }

    private void loadSprites() {
        fireBallSprites = new BufferedImage[2][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.PROJECTILE_FIREBALL_SPRITE);
        for (int i = 0; i < fireBallSprites.length; i++)
            for (int j = 0; j < fireBallSprites[i].length; j++)
                fireBallSprites[i][j] = temp.getSubimage(j * DEFAULT_W, i * DEFAULT_H, DEFAULT_W, DEFAULT_H);

    }

    private BufferedImage[][] getSprites(Projectile p) {
       if (p instanceof MaitaProjectile)
           return fireBallSprites;

       else throw new IllegalArgumentException("Unknown projectile type: " + p.getClass().getName());
    }
}
