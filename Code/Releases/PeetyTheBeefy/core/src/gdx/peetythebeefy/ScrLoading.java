package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import gdx.peetythebeefy.cookiecutters.Constants;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

public class ScrLoading implements Screen {

    PeetyTheBeefy game;
    Sprite sprLoading;
    SpriteBatch batch;
    ShapeRenderer SR;
    float fOpacity = 0, fLoadingBarProgress = 0;

    public ScrLoading (PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.SR = game.SR;
        assetManager.load("Loading.png", Texture.class);
        assetManager.finishLoading();
        sprLoading = new Sprite(assetManager.get("Loading.png", Texture.class));
    }

    @Override
    public void show() {
        queueAssets();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprLoading.draw(batch);
        batch.end();
        loadingBar();
        if(assetManager.update() && !Constants.isLoadingIn){
            game.scrMainMenu = new ScrMainMenu(game);
            game.scrStageSelect = new ScrStageSelect(game);
            game.scrLvl1 = new ScrLvl1(game);
            game.scrControls = new ScrControls(game);
            game.scrLvl2 = new ScrLvl2(game);
            game.scrLvl3 = new ScrLvl3(game);
            game.scrDeath = new ScrDeath(game);
            game.mBackground = assetManager.get("sound/Morning.mp3", Music.class);
            Constants.isLoadingIn = true;
        }

        screenTransition();
        transitionBlock();
    }

    public void loadingBar() {
        SR.begin(ShapeType.Line);
        SR.setColor(0, 0, 0, 1);
        SR.rect(Constants.SCREENWIDTH/4, Constants.SCREENHEIGHT/2 - 20, Constants.SCREENWIDTH/2, 40);
        SR.end();

       fLoadingBarProgress = assetManager.getProgress() * Constants.SCREENWIDTH/2;

        SR.begin(ShapeType.Filled);
        SR.setColor(0, 0, 0, 1);
        SR.rect(Constants.SCREENWIDTH/4, Constants.SCREENHEIGHT/2 -20, fLoadingBarProgress, 40);
        SR.end();
    }

    public void transitionBlock() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SR.begin(ShapeType.Filled);
        SR.setColor(0, 0, 0, fOpacity);
        SR.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SR.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void screenTransition() {
        if(Constants.isLoadingIn && fOpacity < 1) {
            fOpacity += 0.02f;
        }
        if(fOpacity >= 1) {
            Constants.isMainMenuOut = true;
            ScrMainMenu.fAlpha = 1;
            game.setScreen(game.scrMainMenu);
            Constants.isLoadingIn = false;
        }

    }

    public void queueAssets() {
        assetManager.load("bulletTexture.png", Texture.class);
        assetManager.load("buttons.png", Texture.class);
        assetManager.load("buttons.txt", TextureAtlas.class);
        assetManager.load("Controls image.png", Texture.class);
        assetManager.load("GUI.png", Texture.class);
        assetManager.load("Heart-Full.png", Texture.class);
        assetManager.load("level1Background.png", Texture.class);
        assetManager.load("level2Background.png", Texture.class);
        assetManager.load("mainMenu.png", Texture.class);
        assetManager.load("mainMenu2.png", Texture.class);
        assetManager.load("MTMsprite.png", Texture.class);
        assetManager.load("PTBsprite.png", Texture.class);
        assetManager.load("sky.png", Texture.class);
        assetManager.load("skyDay.png", Texture.class);
        assetManager.load("TextBoxMatty.png", Texture.class);
        assetManager.load("TextBoxPeety.png", Texture.class);
        assetManager.load("Tiled map Assets.png", Texture.class);
        assetManager.load("Watergun.png", Texture.class);
        assetManager.load("sound/Morning.mp3", Music.class);
        assetManager.load("sound/Hit.mp3", Sound.class);
        assetManager.load("sound/PeetyTalk.mp3", Sound.class);
        assetManager.load("sound/MattyTalk.mp3", Sound.class);
        assetManager.load("sound/Pew.mp3",Sound.class);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
