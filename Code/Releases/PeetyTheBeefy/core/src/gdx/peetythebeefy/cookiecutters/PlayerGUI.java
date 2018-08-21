package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

public class PlayerGUI {

    SpriteBatch fixedBatch, batch;
    Sprite sprWatergun;
    Texture txHeart,txGUI, txBullet;
    FreeTypeFontParameter parameter;
    FreeTypeFontGenerator generator;
    BitmapFont font;
    GlyphLayout layout;
    float fAngle;
    public Vector2 v2PlayerPosition, vMousePosition;

    public PlayerGUI(SpriteBatch _fixedBatch, SpriteBatch _batch, Vector2 _v2PlayerPosition, Vector2 _vMousePosition,
                     BitmapFont _font, FreeTypeFontGenerator _generator, FreeTypeFontParameter _parameter) {
        this.fixedBatch = _fixedBatch;
        this.batch = _batch;
        this.font = _font;
        this.generator = _generator;
        this.parameter = _parameter;
        this.v2PlayerPosition = _v2PlayerPosition;
        this.vMousePosition = _vMousePosition;
        parameter.size = 30;
        sprWatergun = new Sprite(assetManager.get("Watergun.png", Texture.class));
        txBullet = assetManager.get("bulletTexture.png");
        txGUI = assetManager.get("GUI.png");
        txHeart = assetManager.get("Heart-Full.png");
        font = generator.generateFont(parameter);
        layout = new GlyphLayout(font, "BLV: " + Constants.nBeefinessLevel);

    }

    public void Update() {
        batch.begin();
        sprWatergun.draw(batch);
        batch.end();
        if (!Constants.isShowing ) {
            fAngle = MathUtils.radiansToDegrees * MathUtils.atan2(vMousePosition.y - sprWatergun.getY(), vMousePosition.x - sprWatergun.getX());
            if (fAngle < 0) {
                fAngle += 360;
            }
            sprWatergun.setPosition(v2PlayerPosition.x * Constants.PPM - 6, v2PlayerPosition.y *Constants.PPM - 6);
            if(Constants.isGameStart) {
                if (fAngle > 90 && fAngle < 270) {
                    sprWatergun.setFlip(true, true);

                } else {
                    sprWatergun.setFlip(true, false);
                }
                sprWatergun.setRotation(fAngle);
            }
        }
        fixedBatch.begin();
        fixedBatch.draw(txGUI,Constants.SCREENWIDTH - txGUI.getWidth(),0);
        if(Constants.nHealth >=1) {
            fixedBatch.draw(txHeart, (float)690.5, 108, txHeart.getWidth() + 5, txHeart.getHeight()+5);
            if(Constants.nHealth >= 2) {
                fixedBatch.draw(txHeart, (float)690.5,  (float) (108 + 38.5), txHeart.getWidth() +5 , txHeart.getHeight() +5);
                if(Constants.nHealth >= 3) {
                    fixedBatch.draw(txHeart, (float)690.5, 108 + 77, txHeart.getWidth()+5, txHeart.getHeight()+5);
                    if(Constants.nHealth >=4) {
                        fixedBatch.draw(txHeart, (float)690.5   , (float) (108 + 115.5), txHeart.getWidth()+5, txHeart.getHeight()+5);
                    }
                }
            }
        }
        if(Constants.nBulletCount >= 1) {
            fixedBatch.draw(txBullet,583,14,27,27);
            if(Constants.nBulletCount >= 2) {
                fixedBatch.draw(txBullet,583 - 45,14,27,27);
                if(Constants.nBulletCount >= 3) {
                    fixedBatch.draw(txBullet,583 - 90,14,27,27);
                    if(Constants.nBulletCount >= 4) {
                        fixedBatch.draw(txBullet,583 - 135,14,27,27);
                    }
                }
            }
        }
        font.draw(fixedBatch, "BLV: " + Constants.nBeefinessLevel, 0, layout.height);
        fixedBatch.end();
        if(Constants.nHealth == 0) {
            Constants.isPlayerDead = true;
        }
    }
}
