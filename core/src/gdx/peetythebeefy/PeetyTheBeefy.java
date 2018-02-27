package gdx.peetythebeefy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gdx.peetythebeefy.cookiecutters.Buttons;
import java.util.ArrayList;

public class PeetyTheBeefy extends Game {

    SpriteBatch batch;
    ScrMainMenu scrMainMenu;
    ScrLvl1 scrLvl1;
    ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        scrMainMenu = new ScrMainMenu(this);
        scrLvl1 = new ScrLvl1(this);
        setScreen(scrMainMenu);
    }

    @Override
    public void render() {
        super.render();
        Buttons();
        drawButtons();
    }

    public void updateScreen(int nScreen) {
        if (nScreen == 0) {
            setScreen(scrMainMenu);
        } else if (nScreen == 1) {
            setScreen(scrLvl1);
        }

    }

    public void Buttons() {
        alButtons.add(new Buttons("badlogic.jpg", batch, 250, 450, 100, 100));
        alButtons.add(new Buttons("badlogic.jpg", batch, 250, 325, 100, 100));
        alButtons.add(new Buttons("badlogic.jpg", batch, 250, 200, 100, 100));
        alButtons.add(new Buttons("badlogic.jpg", batch, 250, 75, 100, 100));

    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
