package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import gdx.peetythebeefy.ScrLvl1;

public class Constants {
    public static final int SCREENWIDTH = Gdx.graphics.getWidth();
    public static final int SCREENHEIGHT = Gdx.graphics.getHeight();
    public static final int PPM = 32;

    //filters
    public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_BULLET = 8;
    public static final short BIT_ENEMY = 16;
    public static int nHealth = 4, nBulletCount = 4;

    //textures
    public static final Texture txHeart = new Texture("Heart-Full.png"),
            txBullet = new Texture("bulletTexture.png"), txGUI = new Texture("GUI.png"),
            txWaterGun = new Texture("Watergun.png");
    public static final Sprite sprWatergun = new Sprite(txWaterGun);


    //this needs to be static since the GUI is universal for all levels
    public static void playerGUI(SpriteBatch fixedBatch, SpriteBatch batch, Vector2 v2PlayerPosition, Vector2 vMousePosition) {
        float fAngle;
        batch.begin();
        sprWatergun.draw(batch);
        batch.end();
        if (!ScrLvl1.isShowing) {
            fAngle = MathUtils.radiansToDegrees * MathUtils.atan2(vMousePosition.y - sprWatergun.getY(), vMousePosition.x - sprWatergun.getX());
            if (fAngle < 0) {
                fAngle += 360;
            }
            sprWatergun.setPosition(v2PlayerPosition.x * PPM - 6, v2PlayerPosition.y * PPM - 6);
            if (fAngle > 90 && fAngle < 270) {
                sprWatergun.setFlip(true, true);

            } else {
                sprWatergun.setFlip(true, false);
            }
            sprWatergun.setRotation(fAngle);
        }
        fixedBatch.begin();
        fixedBatch.draw(txGUI,Constants.SCREENWIDTH - txGUI.getWidth(),0);
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
    }
    public static void assetsDispose() {
        txBullet.dispose();
        txGUI.dispose();
        txHeart.dispose();
        txWaterGun.dispose();
    }
}
