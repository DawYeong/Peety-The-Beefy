package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import gdx.peetythebeefy.ScrLvl1;

//Variables that never change and we have to use between multiple screens
public class Constants {
    public static final int SCREENWIDTH = Gdx.graphics.getWidth();
    public static final int SCREENHEIGHT = Gdx.graphics.getHeight();
    public static final int PPM = 32;
    //PPM Stands for pixels per meter
    //used for Box2D because each tile is 1 meter in length but the texture is 32 pixels so
    //PPM is used as a conversion between the two (or else position is not to scale)

    //filters for different types of entities
    public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_BULLET = 8;
    public static final short BIT_ENEMY = 16;
    public static final short BIT_ENEMYBULLET = 32;
    public static int nHealth = 4, nBulletCount = 4, nCurrentScreen = 3;

    //Constant Textures that we need to draw across screens
    //This includes everything involved in drawing the GUI
    public static final Texture txHeart = new Texture("Heart-Full.png"),
            txBullet = new Texture("bulletTexture.png"), txGUI = new Texture("GUI.png"),
            txWaterGun = new Texture("Watergun.png");
    public static final Sprite sprWatergun = new Sprite(txWaterGun);
    public static boolean isPlayerDead = false, isShowing, isGameStart;
    public static boolean[] isLevelFinished = new boolean[12];
    public static boolean[] isLevelUnlocked = new boolean[12];
    public static boolean[] isFadeIn = new boolean [12];
    public static boolean[] isFadeOut = new boolean[12];



    //this needs to be static since the GUI is universal for all levels
    public static void playerGUI(SpriteBatch fixedBatch, SpriteBatch batch, Vector2 v2PlayerPosition, Vector2 vMousePosition) {
        float fAngle;
        batch.begin();
        sprWatergun.draw(batch);
        batch.end();
        if (!isShowing ) {
            fAngle = MathUtils.radiansToDegrees * MathUtils.atan2(vMousePosition.y - sprWatergun.getY(), vMousePosition.x - sprWatergun.getX());
            if (fAngle < 0) {
                fAngle += 360;
            }
            sprWatergun.setPosition(v2PlayerPosition.x * PPM - 6, v2PlayerPosition.y * PPM - 6);
            if(isGameStart) {
                if (fAngle > 90 && fAngle < 270) {
                    sprWatergun.setFlip(true, true);

                } else {
                    sprWatergun.setFlip(true, false);
                }
                sprWatergun.setRotation(fAngle);
            }
        }
        fixedBatch.begin();
        fixedBatch.draw(txGUI,SCREENWIDTH - txGUI.getWidth(),0);
        if(nHealth >=1) {
            fixedBatch.draw(txHeart, (float)690.5, 108, txHeart.getWidth() + 5, txHeart.getHeight()+5);
            if(nHealth >= 2) {
                fixedBatch.draw(txHeart, (float)690.5,  (float) (108 + 38.5), txHeart.getWidth() +5 , txHeart.getHeight() +5);
                if(nHealth >= 3) {
                    fixedBatch.draw(txHeart, (float)690.5, 108 + 77, txHeart.getWidth()+5, txHeart.getHeight()+5);
                    if(nHealth >=4) {
                        fixedBatch.draw(txHeart, (float)690.5   , (float) (108 + 115.5), txHeart.getWidth()+5, txHeart.getHeight()+5);
                    }
                }
            }
        }
        if(nBulletCount >= 1) {
            fixedBatch.draw(txBullet,583,14,27,27);
            if(nBulletCount >= 2) {
                fixedBatch.draw(txBullet,583 - 45,14,27,27);
                if(nBulletCount >= 3) {
                    fixedBatch.draw(txBullet,583 - 90,14,27,27);
                    if(nBulletCount >= 4) {
                        fixedBatch.draw(txBullet,583 - 135,14,27,27);
                    }
                }
            }
        }
        fixedBatch.end();
        if(nHealth == 0) {
            isPlayerDead = true;
        }
    }
    public static void assetsDispose() {
        txBullet.dispose();
        txGUI.dispose();
        txHeart.dispose();
        txWaterGun.dispose();
    }
}
