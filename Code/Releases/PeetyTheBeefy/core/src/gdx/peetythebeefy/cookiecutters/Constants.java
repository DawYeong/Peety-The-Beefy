package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

//Variables that never change and we have to use between multiple screens
public class Constants {
    public static final int SCREENWIDTH = Gdx.graphics.getWidth();
    public static final int SCREENHEIGHT = Gdx.graphics.getHeight();
    public static final int PPM = 32;
    //PPM Stands for pixels per meter
    //used for Box2D because each tile is 1 meter in length but the texture is 32 pixels so
    //PPM is used as a conversion between the two (or else position is not to scale)

    //filters for different types of entities
    public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_BULLET = 8;
    public static final short BIT_ENEMY = 16;
    public static final short BIT_ENEMYBULLET = 32;
    public static int nHealth = 4, nBulletCount = 4, nCurrentScreen = 3, nBeefinessLevel = 1;
    public static float fBeefyProgression = 0, fLevelUp = 2, fPlayerDamage = 0.5f;


    public static boolean isPlayerDead = false, isShowing, isGameStart, isLoadingIn, isMainMenuOut;
    public static boolean[] isLevelFinished = new boolean[12];
    public static boolean[] isLevelUnlocked = new boolean[12];
    public static boolean[] isFadeIn = new boolean [13];
    public static boolean[] isFadeOut = new boolean[12];

}
