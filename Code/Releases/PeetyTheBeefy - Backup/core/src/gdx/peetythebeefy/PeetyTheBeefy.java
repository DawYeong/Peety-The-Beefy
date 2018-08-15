package gdx.peetythebeefy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import gdx.peetythebeefy.cookiecutters.Constants;

public class PeetyTheBeefy extends Game implements InputProcessor {

    SpriteBatch batch;
    ShapeRenderer SR;
    BitmapFont font;
    ScrMainMenu scrMainMenu;
    ScrStageSelect scrStageSelect;
    FreeTypeFontParameter parameter;
    ScrControls scrControls;
    ScrLoading scrLoading;
    ScrLvl1 scrLvl1;
    ScrLvl2 scrLvl2;
    ScrDeath scrDeath;
    OrthographicCamera camera;
    GlyphLayout layout;
    float fMouseX, fMouseY, fBeefyProgression = 0, fLevelUp = 2;
    int nBeefinessLevel = 1;
    boolean isReset;
    public static final AssetManager assetManager = new AssetManager();

    @Override
    public void create() {
        batch = new SpriteBatch();
        SR = new ShapeRenderer();
        font = new BitmapFont();
        parameter = new FreeTypeFontParameter();
        //universal camera used between screens
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);

        scrLoading = new ScrLoading(this);
        setScreen(scrLoading); //Starting screen

        //No code is done unless assets are loaded
        assetManager.finishLoading();

        //Different Screens
        scrMainMenu = new ScrMainMenu(this);
        scrStageSelect = new ScrStageSelect(this);
        scrLvl1 = new ScrLvl1(this);
        scrControls = new ScrControls(this);
        scrLvl2 = new ScrLvl2(this);
        scrDeath = new ScrDeath(this);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        super.render();
        playerLvl();
    }

    public void updateScreen(int nScreen) {
        if (nScreen == 0) {
            setScreen(scrMainMenu);
        } else if (nScreen == 1) {
            setScreen(scrStageSelect);
        } else if (nScreen == 2) {
            setScreen(scrControls);
        } else if (nScreen == 3) {
            setScreen(scrLvl1);
        } else if (nScreen == 4) {
            setScreen(scrLvl2);
        } else if(nScreen == 5) {
            setScreen(scrDeath);
        }

    }
    public void queueAssets() {
        assetManager.load("mainMenu.png", Texture.class);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }
    public void playerGUI(SpriteBatch fixedBatch, SpriteBatch batch, Vector2 v2PlayerPosition, Vector2 vMousePosition,
                          BitmapFont font, FreeTypeFontGenerator generator) {
        float fAngle;
        parameter.size = 30;
        font = generator.generateFont(parameter);
        layout = new GlyphLayout(font, "BLV: " + nBeefinessLevel);
        batch.begin();
        Constants.sprWatergun.draw(batch);
        batch.end();
        if (!Constants.isShowing ) {
            fAngle = MathUtils.radiansToDegrees * MathUtils.atan2(vMousePosition.y - Constants.sprWatergun.getY(), vMousePosition.x - Constants.sprWatergun.getX());
            if (fAngle < 0) {
                fAngle += 360;
            }
            Constants.sprWatergun.setPosition(v2PlayerPosition.x * Constants.PPM - 6, v2PlayerPosition.y *Constants.PPM - 6);
            if(Constants.isGameStart) {
                if (fAngle > 90 && fAngle < 270) {
                    Constants.sprWatergun.setFlip(true, true);

                } else {
                    Constants.sprWatergun.setFlip(true, false);
                }
                Constants.sprWatergun.setRotation(fAngle);
            }
        }
        fixedBatch.begin();
        fixedBatch.draw(Constants.txGUI,Constants.SCREENWIDTH - Constants.txGUI.getWidth(),0);
        if(Constants.nHealth >=1) {
            fixedBatch.draw(Constants.txHeart, (float)690.5, 108, Constants.txHeart.getWidth() + 5, Constants.txHeart.getHeight()+5);
            if(Constants.nHealth >= 2) {
                fixedBatch.draw(Constants.txHeart, (float)690.5,  (float) (108 + 38.5), Constants.txHeart.getWidth() +5 , Constants.txHeart.getHeight() +5);
                if(Constants.nHealth >= 3) {
                    fixedBatch.draw(Constants.txHeart, (float)690.5, 108 + 77, Constants.txHeart.getWidth()+5, Constants.txHeart.getHeight()+5);
                    if(Constants.nHealth >=4) {
                        fixedBatch.draw(Constants.txHeart, (float)690.5   , (float) (108 + 115.5), Constants.txHeart.getWidth()+5, Constants.txHeart.getHeight()+5);
                    }
                }
            }
        }
        if(Constants.nBulletCount >= 1) {
            fixedBatch.draw(Constants.txBullet,583,14,27,27);
            if(Constants.nBulletCount >= 2) {
                fixedBatch.draw(Constants.txBullet,583 - 45,14,27,27);
                if(Constants.nBulletCount >= 3) {
                    fixedBatch.draw(Constants.txBullet,583 - 90,14,27,27);
                    if(Constants.nBulletCount >= 4) {
                        fixedBatch.draw(Constants.txBullet,583 - 135,14,27,27);
                    }
                }
            }
        }
        font.draw(fixedBatch, "BLV: " + nBeefinessLevel, 0, layout.height);
        fixedBatch.end();
        if(Constants.nHealth == 0) {
            Constants.isPlayerDead = true;
        }
    }

    public void playerLvl() {
        if(fBeefyProgression >= fLevelUp) {
            nBeefinessLevel++;
            fLevelUp++;
            fBeefyProgression = 0;
        }
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        fMouseX = Gdx.input.getX();
        fMouseY = Constants.SCREENHEIGHT - Gdx.input.getY();
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}