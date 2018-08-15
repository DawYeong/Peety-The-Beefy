package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

public class ScrLoading implements Screen {

    PeetyTheBeefy game;

    public ScrLoading (PeetyTheBeefy game) {
        this.game = game;
    }

    @Override
    public void show() {
        queueAssets();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(assetManager.update()) {
            game.updateScreen(0);
        }
    }

    public void queueAssets() {
        assetManager.load("bulletTexture.png", Texture.class);
        assetManager.load("buttons.png", Texture.class);
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
