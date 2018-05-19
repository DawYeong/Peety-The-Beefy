package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;

public class Constants {
    public static final int SCREENWIDTH = Gdx.graphics.getWidth();
    public static final int SCREENHEIGHT = Gdx.graphics.getHeight();
    public static final int PPM = 32;

    //filters
    public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_BULLET = 8;
    public static final short BIT_ENEMY = 16;
}
