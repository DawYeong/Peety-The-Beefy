/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gdx.peetythebeefy.cookiecutters.Buttons;
import java.util.ArrayList;

/**
 *
 * @author tn200
 */
public class ScrMainMenu implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture img;
    ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();

    public ScrMainMenu(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        img = new Texture("badlogic.jpg");
        Gdx.input.setInputProcessor(this);
        createButtons();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            game.updateScreen(1);
        }
        drawButtons();
//        batch.begin();
//        batch.draw(img,100,100);
//        batch.end();

    }
    
    public void createButtons() {
        alButtons.add(new Buttons("badlogic.jpg", batch, 350, 350, 100, 50));
    }
    
    public void drawButtons() {
        for(int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
        }
     }
    
    @Override
    public void resize(int i, int i1) {

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
