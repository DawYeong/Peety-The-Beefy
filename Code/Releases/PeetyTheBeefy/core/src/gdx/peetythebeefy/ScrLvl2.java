/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gdx.peetythebeefy.cookiecutters.Buttons;

/**
 *
 * @author tn200
 */
public class ScrLvl2 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Buttons BackButton;

    public ScrLvl2(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
    }

    @Override
    public void show() {
        BackButton = new Buttons("backButton", batch, -8, 0, 96, 32);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        backButtonFunctionality();
    }
    public void backButtonFunctionality() {
        BackButton.Update();
        if(game.fMouseX > BackButton.fX && game.fMouseX < BackButton.fX + BackButton.fW
                && game.fMouseY > BackButton.fY && game.fMouseY < BackButton.fY + BackButton.fH) {
            System.out.println("moves to the main menu");
            game.updateScreen(0);
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
