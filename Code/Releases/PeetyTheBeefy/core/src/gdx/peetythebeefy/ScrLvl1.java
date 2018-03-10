/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import gdx.peetythebeefy.cookiecutters.Buttons;
import gdx.peetythebeefy.cookiecutters.Box2D;
import java.util.ArrayList;

/**
 *
 * @author tn200
 */
public class ScrLvl1 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    Texture img;
    World world;
    Vector2 playerPosition;
    float fW, fH;
    Body player;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    ArrayList<gdx.peetythebeefy.cookiecutters.Box2D> alPlayer = new ArrayList<Box2D>();
    ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();
    static boolean isShowing = false;

    public ScrLvl1(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        img = new Texture("badlogic.jpg");
        world = new World(new Vector2(0f, 0f), false);
        playerPosition = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        fW = 32;
        fH = 32;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);
        b2dr = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        createButtons();
        //player = createBody(100, 100, fW, fH, false);
        createPlayer();
        drawPlayer();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
//            game.updateScreen(0);
//        }
        //cameraUpdate();
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            PeetyTheBeefy.fMouseX = Gdx.graphics.getWidth(); // just moves mouse away from button
            PeetyTheBeefy.fMouseY = Gdx.graphics.getHeight();
            if (isShowing == false) { //its like a pop up menu, if you want to go back press p to bring up back button
                isShowing = true;
            } else {
                isShowing = false;
            }
        }
        if (isShowing == true) {
            drawButtons();
        }
        update();
        b2dr.render(world, camera.combined.scl(32));
        System.out.println(playerPosition.x + " " + playerPosition.y);
    }
//

    public void update() {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
    }
//

    public void cameraUpdate() {
        camera.position.set(0, 0, 0);
        camera.update();
    }

    public void createPlayer() {
        alPlayer.add(new Box2D(playerPosition.x, playerPosition.y, fW, fH, false, world, batch));
    }

    public void drawPlayer() {
        alPlayer.get(0).Update();
    }
    
    public void move() {
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerPosition.x -= 5;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerPosition.x += 5;
        } else if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerPosition.y += 5;
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerPosition.y -= 5;
        }
    }

//    public Body createBody(float x, float y, float width, float height, boolean isStatic) {
//        Body pBody;
//        BodyDef def = new BodyDef();
//        if (isStatic) {
//            def.type = BodyDef.BodyType.StaticBody;
//        } else {
//            def.type = BodyDef.BodyType.DynamicBody;
//        }
//        def.position.set(x / 32, y / 32);
//        def.fixedRotation = false;
//        pBody = world.createBody(def);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox((float) x / 2 / 32, (float) y / 2 / 32);
//
//        pBody.createFixture(shape, 1.0f);
//        shape.dispose();
//        return pBody;
//    }
    public void createButtons() {
        alButtons.add(new Buttons("badlogic.jpg", batch, 0, 0, 100, 50));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (PeetyTheBeefy.fMouseX > alButtons.get(i).fX && PeetyTheBeefy.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && PeetyTheBeefy.fMouseY > alButtons.get(i).fY && PeetyTheBeefy.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                System.out.println("moves to the stageselectscreen");
                game.updateScreen(1);
                PeetyTheBeefy.fMouseX = Gdx.graphics.getWidth(); // just moves mouse away from button
                PeetyTheBeefy.fMouseY = Gdx.graphics.getHeight();
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        img.dispose();
        b2dr.dispose();
        world.dispose();
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
