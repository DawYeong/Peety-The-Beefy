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
public class ScrStageSelect implements Screen {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture texMenuMain, texMenuNew;
    float fMainX, fNewX, fY;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();

    public ScrStageSelect(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        texMenuMain = new Texture("mainMenu.png");
        texMenuNew = new Texture("mainMenu2.png");
    }

    @Override
    public void show() {
        fMainX = 0;
        fNewX = 768;
        fY = Gdx.graphics.getHeight();
        createButtons();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(20 / 255f, 10 / 255f, 50 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        screenTransition();

        drawButtons();
    }

    public void createButtons() {
        alButtons.add(new Buttons("b1", batch, 64, fY, 64, 64));
        alButtons.add(new Buttons("b2", batch, 256, fY, 64, 64));
        alButtons.add(new Buttons("b3", batch, 448, fY, 64, 64));
        alButtons.add(new Buttons("b4", batch, 640, fY, 64, 64));
        alButtons.add(new Buttons("b5", batch, 64, fY, 64, 64));
        alButtons.add(new Buttons("b6", batch, 256, fY, 64, 64));
        alButtons.add(new Buttons("b7", batch, 448, fY, 64, 64));
        alButtons.add(new Buttons("b8", batch, 640, fY, 64, 64));
        alButtons.add(new Buttons("b9", batch, 64, fY, 64, 64));
        alButtons.add(new Buttons("b10", batch, 256, fY, 64, 64));
        alButtons.add(new Buttons("b11", batch, 448, fY, 64, 64));
        alButtons.add(new Buttons("b12", batch, 640, fY, 64, 64));
        alButtons.add(new Buttons("backButton", batch, -8, 0, 96, 32));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (game.fMouseX > alButtons.get(i).fX && game.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && game.fMouseY > alButtons.get(i).fY && game.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                if (i == 0) {
                    System.out.println("moves to lvl 1");
                    ScrLvl1.isShowing = false;

                    game.updateScreen(3);
                } else if(i == 1) {
                    System.out.println("moves to lvl 2");

                    game.updateScreen(4);
                }
                else if (i == 12) {
                    System.out.println("moves to main menu");

                    game.updateScreen(0);
                }
                //resets the y position of each level button
                alButtons.get(0).fY = Constants.SCREENHEIGHT;
                alButtons.get(1).fY = Constants.SCREENHEIGHT;
                alButtons.get(2).fY = Constants.SCREENHEIGHT;
                alButtons.get(3).fY = Constants.SCREENHEIGHT;
                alButtons.get(4).fY = Constants.SCREENHEIGHT;
                alButtons.get(5).fY = Constants.SCREENHEIGHT;
                alButtons.get(6).fY = Constants.SCREENHEIGHT;
                alButtons.get(7).fY = Constants.SCREENHEIGHT;
                alButtons.get(8).fY = Constants.SCREENHEIGHT;
                alButtons.get(9).fY = Constants.SCREENHEIGHT;
                alButtons.get(10).fY = Constants.SCREENHEIGHT;
                alButtons.get(11).fY = Constants.SCREENHEIGHT;
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
        texMenuNew.dispose();
        texMenuMain.dispose();
        batch.dispose();
    }

    public void screenTransition() {

        if (fMainX >= -768) {
            fMainX -= 16;
        }
        if (fNewX >= 16) {
            fNewX -= 16;
            alButtons.get(0).fY -= 4;
            alButtons.get(1).fY -= 4;
            alButtons.get(2).fY -= 4;
            alButtons.get(3).fY -= 4;
            alButtons.get(4).fY -= 8;
            alButtons.get(5).fY -= 8;
            alButtons.get(6).fY -= 8;
            alButtons.get(7).fY -= 8;
            alButtons.get(8).fY -= 12;
            alButtons.get(9).fY -= 12;
            alButtons.get(10).fY -= 12;
            alButtons.get(11).fY -= 12;
        }

        batch.begin();

        batch.draw(texMenuMain, fMainX, 0);
        batch.draw(texMenuNew, fNewX, 0);

        batch.end();

    }
}
