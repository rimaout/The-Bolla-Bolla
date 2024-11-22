package view.entities;

import model.entities.EnemyManagerModel;
import model.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.ENEMY_H;

public class EnemyManagerView {
    private static EnemyManagerView instance;
    private static EnemyManagerModel enemyManagerModel = EnemyManagerModel.getInstance();

    private BufferedImage[][] zenChanSprites;
    private BufferedImage[][] maitaSprites;
    private BufferedImage[][] skelMonstaSprites;

    private ArrayList<EnemyView> enemiesViews = new ArrayList<>();

    private EnemyManagerView() {
        loadSprites();
    }

    public static EnemyManagerView getInstance() {
        if (instance == null) {
            instance = new EnemyManagerView();
        }
        return instance;
    }

    public void update() {
        for (EnemyView e : enemiesViews)
            if (e.isActive())
                e.update();
    }

    public void draw(Graphics g) {
        syncEnemyViewsWithModel();

        for (EnemyView e : enemiesViews)
            if(e.isActive())
                g.drawImage(getEnemySprite(e.enemyModel.getEnemyType())[e.enemyModel.getEnemyState()][e.getAnimationIndex()], (int) (e.enemyModel.getHitbox().x - ENEMY_HITBOX_OFFSET_X) + e.enemyModel.flipX(), (int) (e.enemyModel.getHitbox().y - ENEMY_HITBOX_OFFSET_Y), ENEMY_W * e.enemyModel.flipW(), ENEMY_H, null);
    }

    private void syncEnemyViewsWithModel() {
        for (var em : enemyManagerModel.getEnemiesModels()) {

            // if a projectile is not in the view, add it
            if (enemiesViews.stream().noneMatch(ev -> ev.enemyModel.equals(em)))
                switch (em.getEnemyType()) {
                    case MAITA -> enemiesViews.add(new EnemyView(em));
                    case ZEN_CHAN -> enemiesViews.add(new EnemyView(em));
                }
        }
    }

    private void loadSprites() {
        zenChanSprites = new BufferedImage[9][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.ZEN_CHAN_ENEMY_SPRITE);

        for (int j = 0; j < zenChanSprites.length; j++)
            for (int i = 0; i < zenChanSprites[j].length; i++)
                zenChanSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);

        maitaSprites = new BufferedImage[9][4];
        temp = LoadSave.GetSprite(LoadSave.MAITA_ENEMY_SPRITE);
        for (int j = 0; j < maitaSprites.length; j++)
            for (int i = 0; i < maitaSprites[j].length; i++)
                maitaSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);

        skelMonstaSprites = new BufferedImage[3][2];
        temp = LoadSave.GetSprite(LoadSave.SKEL_MONSTA_ENEMY_SPRITE);
        for (int j = 0; j < skelMonstaSprites.length; j++)
            for (int i = 0; i < skelMonstaSprites[j].length; i++)
                skelMonstaSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    public void newLevelReset() {
        enemiesViews.clear();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public BufferedImage[][] getEnemySprite(EnemyType enemyType) {
        return switch (enemyType) {
            case ZEN_CHAN -> zenChanSprites;
            case MAITA -> maitaSprites;
            case SKEL_MONSTA -> skelMonstaSprites;
        };
    }
}