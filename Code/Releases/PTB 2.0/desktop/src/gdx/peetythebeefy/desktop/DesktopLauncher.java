package gdx.peetythebeefy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.peetythebeefy.PeetyTheBeefy;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PeetyTheBeefy(), config);
                config.width = 768;
                config.height = 768;
	}
}
