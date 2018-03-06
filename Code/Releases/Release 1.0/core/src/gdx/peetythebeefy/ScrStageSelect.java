/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
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
public class ScrStageSelect implements Screen {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture img;
    ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();

    public ScrStageSelect(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {
        createButtons();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawButtons();
    }

    public void createButtons() {
        alButtons.add(new Buttons("badlogic.jpg", batch, 350, 450, 200, 100));
        alButtons.add(new Buttons("badlogic.jpg", batch, 0, 0, 100, 50));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (PeetyTheBeefy.fMouseX > alButtons.get(i).fX && PeetyTheBeefy.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && PeetyTheBeefy.fMouseY > alButtons.get(i).fY && PeetyTheBeefy.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                if (i == 0) {
                    System.out.println("moves to lvl1");
                    ScrLvl1.isShowing = false;
                    game.updateScreen(2);
                } else if (i == 1) {
                    System.out.println("moves to main menu");
                    game.updateScreen(0);
                }
                PeetyTheBeefy.fMouseX = Gdx.graphics.getWidth(); // just moves mouse away from button
                PeetyTheBeefy.fMouseY = Gdx.graphics.getHeight();
            }
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
        img.dispose();
        batch.dispose();
    }
}