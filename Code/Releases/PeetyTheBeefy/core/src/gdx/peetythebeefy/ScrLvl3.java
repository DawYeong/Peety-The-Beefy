package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.peetythebeefy.cookiecutters.Buttons;
import gdx.peetythebeefy.cookiecutters.Constants;
import gdx.peetythebeefy.cookiecutters.ContactListener1;
import gdx.peetythebeefy.cookiecutters.EntityCreation;
import static gdx.peetythebeefy.cookiecutters.Constants.PPM;


public class ScrLvl3 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    Buttons backButton;
    ScrLvl1 scrLvl1;
    SpriteBatch batch;
    OrthographicCamera camera;
    ShapeRenderer SR;
    BitmapFont font;
    FreeTypeFontParameter parameter;
    World world;
    EntityCreation ecPlayer;
    float fX, fY, fW, fH;
    Box2DDebugRenderer b2dr;


    public ScrLvl3(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = game.camera;
        this.SR = game.SR;
        this.font = game.font;
        this.parameter = game.parameter;
        scrLvl1 = new ScrLvl1(game);

        b2dr = new Box2DDebugRenderer();

        fX = Constants.SCREENWIDTH / 2;
        fY = Constants.SCREENHEIGHT / 2;
        fW = 32;
        fH = 32;

        world = new World(new Vector2(0f, 0f), false);
        world.setContactListener(new ContactListener1());

        backButton = new Buttons("backButton", scrLvl1.fixedBatch, -8, 0, 96, 32);

        ecPlayer = new EntityCreation(world, "PLAYER", fX, fY, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", 1, Constants.BIT_PLAYER,
                (short) (Constants.BIT_WALL | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0, new Vector2(0, 0),
                scrLvl1.ecPlayer.fHealth);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        b2dr.render(world, camera.combined.scl(PPM));


        ecPlayer.Update();
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
            game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
            game.fMouseY = Constants.SCREENHEIGHT;
            Constants.isShowing = !Constants.isShowing; //its like a pop up menu, if you want to go back press p to bring up back button
        }
        if (Constants.isShowing) {
            drawButtons();
        }
    }

    private void drawButtons() {
        backButton.Update();
        if (game.fMouseX > backButton.fX && game.fMouseX < backButton.fX + backButton.fW
                && game.fMouseY > backButton.fY && game.fMouseY < backButton.fY + backButton.fH) {
            Constants.isShowing = false;
            Constants.isFadeIn[1] = false;
            ScrMainMenu.fAlpha = 0;
            ScrStageSelect.fAlpha = 0;
            game.updateScreen(0);
            game.mGame.stop();
            game.mBackground.setLooping(true);
            game.mBackground.setVolume(0.1f);
            game.mBackground.play();
        }
    }

    private void cameraUpdate() {
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
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
        b2dr.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.fMouseX = Gdx.input.getX();
        game.fMouseY = Constants.SCREENHEIGHT - Gdx.input.getY();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
