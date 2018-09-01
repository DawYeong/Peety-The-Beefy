package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gdx.peetythebeefy.cookiecutters.Constants;

//This screen is switched to when the player health reaches 0
public class ScrDeath implements Screen, InputProcessor{
    PeetyTheBeefy game;
    SpriteBatch fixedBatch;
    BitmapFont bfDeath = new BitmapFont();
    float fOpacity;
    GlyphLayout layout1, layout2;

    public ScrDeath(PeetyTheBeefy game) {
        this.game = game;
        fixedBatch = new SpriteBatch();
        layout1 = new GlyphLayout(bfDeath, "You Died (Press R)");
        layout2 = new GlyphLayout(bfDeath, "Remember to avoid the Matties from jumping on your Head!");
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        fOpacity = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(fOpacity < 1f) {
            fOpacity += 0.005f;
        }
        fixedBatch.begin();
        bfDeath.setColor(1, 1, 1, fOpacity);
        bfDeath.draw(fixedBatch, "You Died (Press R)", Constants.SCREENWIDTH/2 - layout1.width/2,Constants.SCREENHEIGHT/2 + 100);
        bfDeath.draw(fixedBatch, "Remember to avoid the Matties from jumping on your Head!", Constants.SCREENWIDTH/2 -layout2.width/2,
                Constants.SCREENHEIGHT/2 + 50);
        fixedBatch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            Constants.nHealth = 4;
            Constants.isPlayerDead = false;
            game.isReset = true;
            Constants.fPlayerDamage /= 2;
            Constants.nBeefinessLevel--;
            game.updateScreen(Constants.nCurrentScreen);
        }
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
        bfDeath.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
