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
import gdx.peetythebeefy.cookiecutters.Buttons;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

/**
 *
 * @author tn200
 */
//This screen is switched to when the user clicks the controls button on the main menu screen
public class ScrControls implements Screen {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture txMenuNew, txMenuMain, txControls;
    float fMainX, fNewX;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();

    public ScrControls(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        txMenuMain = assetManager.get("mainMenu.png");
        txMenuNew = assetManager.get("mainMenu2.png");
        txControls = assetManager.get("Controls image.png");

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
            if (game.fMouseX > alButtons.get(i).fX && game.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && game.fMouseY > alButtons.get(i).fY && game.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                System.out.println("moves to main menu");
                game.updateScreen(0);
                game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                game.fMouseY = Constants.SCREENHEIGHT;
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
    }

    public void screenTransition() {
        /*
        Starts off by, when you click the controls button on main menu, changes screens instantly
        but the screen starts off with the background of the main menu screen.
        After that it adds to the x value of both the main menu background and the new background,
        creating a scrolling effect.
        */
        if (fMainX >= -768) {
            fMainX -= 16;
        }
        if (fNewX >= 16) {
            fNewX -= 16;
        }

        batch.begin();

        batch.draw(txMenuMain, fMainX, 0);
        batch.draw(txMenuNew, fNewX, 0);
        batch.draw(txControls, fNewX, 0);

        batch.end();

    }
}
