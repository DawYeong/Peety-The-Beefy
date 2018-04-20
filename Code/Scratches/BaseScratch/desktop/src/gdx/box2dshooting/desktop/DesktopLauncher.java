package gdx.box2dshooting.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.box2dshooting.Box2DShooting;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new Box2DShooting(), config);
        config.resizable = false;
        config.width = 768;
        config.height = 768;
    }
}
