package gdx.peetythebeefy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gdx.peetythebeefy.cookiecutters.Buttons;
import java.util.ArrayList;

public class PeetyTheBeefy extends Game implements InputProcessor{

    SpriteBatch batch;
    ScrMainMenu scrMainMenu;
    ScrStageSelect scrStageSelect;
    ScrControls scrControls;
    ScrLvl1 scrLvl1;
    static float fMouseX, fMouseY;
    public ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        scrMainMenu = new ScrMainMenu(this);
        scrStageSelect = new ScrStageSelect(this);
        scrLvl1 = new ScrLvl1(this);
        scrControls = new ScrControls(this);
        Gdx.input.setInputProcessor(this);
        setScreen(scrMainMenu);
//        Buttons();       
    }

    @Override
    public void render() {
        super.render();
//        drawButtons();
    }

    public void updateScreen(int nScreen) {
        if (nScreen == 0) {
            setScreen(scrMainMenu);
        } else if (nScreen == 1) {
            setScreen(scrStageSelect);
        } else if(nScreen == 2) {
            setScreen(scrLvl1);
        } else if(nScreen == 3) {
            setScreen(scrControls);
        }

    }
    
    

//    public void Buttons() {
//        alButtons.add(new Buttons("badlogic.jpg", batch, 0, 0, 100, 100));
//    }

//    public void drawButtons() {
//        for (int i = 0; i < alButtons.size(); i++) {
//            alButtons.get(i).Update();
//            if(fMouseX > alButtons.get(i).fX && fMouseX < alButtons.get(i). fX + alButtons.get(i).fW &&
//                    fMouseY > alButtons.get(i).fY && fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
//                if(getScreen() == scrMainMenu) {
//                    setScreen(scrLvl1);
//                } else if(getScreen() == scrLvl1) {
//                    setScreen(scrMainMenu);
//                }
//                fMouseX = Gdx.graphics.getWidth();
//                fMouseY = Gdx.graphics.getHeight();
//            }
//        }
//    }

    @Override
    public void dispose() {
        batch.dispose();
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
        fMouseX = Gdx.input.getX();
        fMouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        //System.out.println(fMouseX + " " + fMouseY);
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
