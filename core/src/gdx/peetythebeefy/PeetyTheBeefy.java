package gdx.peetythebeefy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PeetyTheBeefy extends Game {
	SpriteBatch batch;
        ScrMainMenu scrMainMenu;
        ScrLvl1 scrLvl1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
                scrMainMenu = new ScrMainMenu(this);
                scrLvl1 = new ScrLvl1(this);
		setScreen(scrMainMenu);
	}

	@Override
	public void render () {
            super.render();
        }
        public void updateScreen(int nScreen) {
            if(nScreen == 0) {
                setScreen(scrMainMenu);
            } else if (nScreen == 1) {
                setScreen(scrLvl1);
            }
            
        }
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
