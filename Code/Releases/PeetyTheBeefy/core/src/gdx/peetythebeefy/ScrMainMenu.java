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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gdx.peetythebeefy.cookiecutters.Buttons;
import java.util.ArrayList;
import gdx.peetythebeefy.cookiecutters.Constants;

/**
 *
 * @author tn200
 */
public class ScrMainMenu implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture texMenu;
    OrthographicCamera camera;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();
    
    

    public ScrMainMenu(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = game.camera;
        texMenu = new Texture("mainMenu.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        createButtons();
        camera.zoom = 1f;
    }

    @Override
    public void render(float f) {
        
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(texMenu, 250, 200);
        batch.draw(texMenu, 0, 0);

        batch.end();
        drawButtons();


    }

    public void createButtons() {
        alButtons.add(new Buttons("playButton", batch, -8,Constants.SCREENHEIGHT/2, 192, 64));
        alButtons.add(new Buttons("controlsButton", batch, -8,Constants.SCREENHEIGHT/2 - 128, 192, 64));
        alButtons.add(new Buttons("stagesButton", batch, -8, Constants.SCREENHEIGHT/2 - 256, 192, 64));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (game.fMouseX > alButtons.get(i).fX && game.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && game.fMouseY > alButtons.get(i).fY && game.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                if (i == 0) {
                    System.out.println("moves to Lvl 1 screen");
                    Constants.isShowing = false;
                    Constants.isPlayerDead = false;
                    game.updateScreen(3);
                } else if (i == 1) {
                    System.out.println("moves to the controls");
                    game.updateScreen(2);
                } else if (i == 2) {
                    System.out.println("moves to the stage select");
                    game.updateScreen(1);
                }
                game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                game.fMouseY = Constants.SCREENHEIGHT;
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, Constants.SCREENWIDTH , Constants.SCREENHEIGHT);
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
        texMenu.dispose();
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
        game.fMouseX = Gdx.input.getX();
        game.fMouseY = Constants.SCREENHEIGHT - Gdx.input.getY();
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
