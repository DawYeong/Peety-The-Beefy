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
import gdx.peetythebeefy.cookiecutters.Constants;

/**
 *
 * @author tn200
 */
public class ScrControls implements Screen {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture texMenuNew, texMenuMain;
    float fMainX, fNewX;
    ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();

    public ScrControls(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        texMenuMain = new Texture("mainMenu.png");
        texMenuNew = new Texture("mainMenu2.png");

    }

    @Override
    public void show() {
        fNewX = 768;
        fMainX = 0;
        createButtons();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        screenTransition();

        drawButtons();
    }

    public void createButtons() {
        alButtons.add(new Buttons("backButton", batch, -8, 0, 96, 32));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (PeetyTheBeefy.fMouseX > alButtons.get(i).fX && PeetyTheBeefy.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && PeetyTheBeefy.fMouseY > alButtons.get(i).fY && PeetyTheBeefy.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                System.out.println("moves to main menu");
                game.updateScreen(0);
                PeetyTheBeefy.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                PeetyTheBeefy.fMouseY = Constants.SCREENHEIGHT;
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
        texMenuMain.dispose();
        texMenuNew.dispose();
        batch.dispose();
    }

    public void screenTransition() {

        if (fMainX >= -768) {
            fMainX -= 16;
        }
        if (fNewX >= 16) {
            fNewX -= 16;
        }

        batch.begin();

        batch.draw(texMenuMain, fMainX, 0);
        batch.draw(texMenuNew, fNewX, 0);

        batch.end();

    }
}
