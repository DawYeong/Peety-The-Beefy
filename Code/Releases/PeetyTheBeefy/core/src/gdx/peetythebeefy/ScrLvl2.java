/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.peetythebeefy.cookiecutters.*;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;

/**
 *
 * @author tn200
 */
public class ScrLvl2 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Buttons BackButton;
    TiledMap tMapLvl2;
    TiledPolyLines tplLvl2;
    World world;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    Box2DDebugRenderer b2dr;
    ScrLvl1 scrLvl1;
    EntityCreation ecPlayer;
    float fX, fY, fW, fH;
    int nLevelWidth, nLevelHeight;
    Texture txBackground, txSky;
    Vector2 vMousePosition;

    public ScrLvl2(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = game.camera;
        scrLvl1 = new ScrLvl1(game);
        this.b2dr = scrLvl1.b2dr;
        world = new World(new Vector2(0f, -18f), false);
        world.setContactListener(new ContactListener1());
        world.setVelocityThreshold(3f);
        fX = Constants.SCREENWIDTH / 2;
        fY = Constants.SCREENHEIGHT / 2;
        fW = 32;
        fH = 32;
        tMapLvl2 = new TmxMapLoader().load("PeetytheBeefy2.tmx");
        tplLvl2 = new TiledPolyLines(world, tMapLvl2.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL,
                (short)(Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0);
        txBackground = new Texture("level2Background.png");
        txSky = new Texture("skyDay.png");
        otmr = new OrthogonalTiledMapRenderer(tMapLvl2);

        MapProperties props = tMapLvl2.getProperties();
        nLevelWidth = props.get("width", Integer.class) ;
        nLevelHeight = props.get("height", Integer.class);

        ecPlayer = new EntityCreation(world, "PLAYER", fX, fY, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", false, false,
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL | Constants.BIT_ENEMY), (short) 0, new Vector2(0,0),
                scrLvl1.ecPlayer.nHealth);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        BackButton = new Buttons("backButton", scrLvl1.fixedBatch, -8, 0, 96, 32);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        scrLvl1.fixedBatch.begin();
        scrLvl1.fixedBatch.draw(txSky,0,0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        scrLvl1.fixedBatch.end();

        batch.begin();
        batch.draw(txBackground,0,0,Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.end();

        ecPlayer.Update();
        otmr.setView(camera);
        otmr.render();

        b2dr.render(world, camera.combined.scl(PPM));
        //playerGUI();
        vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        Constants.playerGUI(scrLvl1.fixedBatch, batch, ecPlayer.body.getPosition(), vMousePosition);
        backButtonFunctionality();
    }
    public void backButtonFunctionality() {
        BackButton.Update();
        if(game.fMouseX > BackButton.fX && game.fMouseX < BackButton.fX + BackButton.fW
                && game.fMouseY > BackButton.fY && game.fMouseY < BackButton.fY + BackButton.fH) {
            System.out.println("moves to the main menu");
            game.updateScreen(0);
        }
    }
    public void cameraUpdate() {
        CameraStyles.lerpAverageBetweenTargets(camera, scrLvl1.v2Target, ecPlayer.body.getPosition().scl(PPM));
        float fStartX = camera.viewportWidth / 2;
        float fStartY = camera.viewportHeight / 2;
        camera.zoom = 0.8f;
        CameraStyles.boundary(camera, fStartX, fStartY, nLevelWidth * 32 - fStartX * 2, nLevelHeight * 32 - fStartY * 2);
        camera.update();
    }
    public void playerGUI() {

    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Constants.SCREENWIDTH , Constants.SCREENHEIGHT);
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
     batch.dispose();
     scrLvl1.dispose();
     tMapLvl2.dispose();
     otmr.dispose();
     b2dr.dispose();
     txBackground.dispose();
     txSky.dispose();
     Constants.assetsDispose();
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
