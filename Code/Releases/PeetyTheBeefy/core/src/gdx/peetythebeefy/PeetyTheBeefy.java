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
    float fMouseX, fMouseY;
    boolean isReset, isReady = true;
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
//        assetManager.finishLoading();

        //Different Screens
//        scrMainMenu = new ScrMainMenu(this);
//        scrStageSelect = new ScrStageSelect(this);
//        scrLvl1 = new ScrLvl1(this);
//        scrControls = new ScrControls(this);
//        scrLvl2 = new ScrLvl2(this);
//        scrDeath = new ScrDeath(this);

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

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        SR.dispose();
        font.dispose();
        scrLoading.dispose();
        scrLvl1.dispose();
        scrLvl2.dispose();
        scrControls.dispose();
        scrDeath.dispose();
        scrMainMenu.dispose();
        scrStageSelect.dispose();
        assetManager.dispose();
    }

    public void playerLvl() {
        if(Constants.fBeefyProgression >= Constants.fLevelUp) {
            Constants.nBeefinessLevel++;
            Constants.fLevelUp += 2;
            Constants.fPlayerDamage += 0.5f;
            Constants.fBeefyProgression = 0;
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
