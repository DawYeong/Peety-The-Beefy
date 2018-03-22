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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import gdx.peetythebeefy.cookiecutters.Buttons;
import gdx.peetythebeefy.cookiecutters.Enemies;
import java.util.ArrayList;
import gdx.peetythebeefy.cookiecutters.TiledPolyLines;

/**
 *
 * @author tn200
 */
public class ScrLvl1 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    World world;
    float fX, fY, fW, fH;
    Body playerBody;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    ArrayList<gdx.peetythebeefy.cookiecutters.Buttons> alButtons = new ArrayList<Buttons>();
    ArrayList<gdx.peetythebeefy.cookiecutters.Enemies> alEnemies = new ArrayList<Enemies>();
    TiledMap tMap;

    Animation araniPeety[];
    TextureRegion trPeety;
    Texture txSheet;
    int nPos, nFrame;
    static boolean isShowing = false;

    public ScrLvl1(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        world = new World(new Vector2(0f, -18f), false);
        fX = Gdx.graphics.getWidth() / 2;
        fY = Gdx.graphics.getHeight() / 2;
        fW = 32;
        fH = 32;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);
        b2dr = new Box2DDebugRenderer();
        createEnemies();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        createButtons();
        tMap = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        otmr = new OrthogonalTiledMapRenderer(tMap);

        nFrame = 0;
        nPos = 0;
        txSheet = new Texture("PTBsprite.png");
        araniPeety = new Animation[8];
        playerSprite(9.2f);
        drawEnemies();

        TiledPolyLines.parseTiledObjectLayer(world, tMap.getLayers().get("collision-layer").getObjects());
        playerBody = createBody(fX, fY, fW, fH, false);
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        frameAnimation();
        trPeety = (TextureRegion) araniPeety[nPos].getKeyFrame(nFrame, true);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
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
        
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        
        otmr.setView(camera);
        otmr.render();
        b2dr.render(world, camera.combined.scl(32));

        batch.begin();
        batch.draw(trPeety, playerBody.getPosition().x * 32 - fW / 2, playerBody.getPosition().y * 32 - fH / 2, fW, fH);
        batch.end();

        move();
        moveEnemies();
    }

    public void cameraUpdate() {
        camera.update();
    }

    public void createButtons() {
        alButtons.add(new Buttons("backButton", batch, -8, 0, 96, 32));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (PeetyTheBeefy.fMouseX > alButtons.get(i).fX && PeetyTheBeefy.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && PeetyTheBeefy.fMouseY > alButtons.get(i).fY && PeetyTheBeefy.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                System.out.println("move to main menu ");
                game.updateScreen(0);
                PeetyTheBeefy.fMouseX = Gdx.graphics.getWidth(); // just moves mouse away from button
                PeetyTheBeefy.fMouseY = Gdx.graphics.getHeight();
            }
        }
    }
    
    public void createEnemies() {
        alEnemies.add(new Enemies(fX + 100, fY + 50, fW, fH, world, batch));
        alEnemies.add(new Enemies(fX - 100, fY + 50, fW, fH, world, batch));
    }
    
    public void drawEnemies() {
        for(int i = 0; i < alEnemies.size(); i++) {
            alEnemies.get(i).Update();
        }
    }
    
    public void moveEnemies() {
        for(int i = 0; i < alEnemies.size(); i++) {
            alEnemies.get(i).move();
        }
    }

    public Body createBody(float x, float y, float width, float height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x / 32, y / 32);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) width / 2 / 32, (float) height / 2 / 32);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

    public void move() {
        float fhForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fhForce -= 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fhForce += 1;
        }
        if (playerBody.getLinearVelocity().y == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                playerBody.applyForceToCenter(0, 500, false);
            }
        }
        if (playerBody.getPosition().y < 0) {
//            player.getPosition().set(player.getPosition().x, 100);
            playerBody.setTransform(playerBody.getPosition().x, Gdx.graphics.getHeight() / 32, 0);
            System.out.println(playerBody.getPosition());
        }
        playerBody.setLinearVelocity(fhForce * 5, playerBody.getLinearVelocity().y);
    }

    public void playerSprite(float nAniSpeed) {
        Sprite sprPeety;
        int nW, nH, nSx, nSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
        nW = txSheet.getWidth() / 4;
        nH = txSheet.getHeight() / 2;
        for (int i = 0; i < 4; i++) {
            Sprite[] arSprPeety = new Sprite[4];
            for (int j = 0; j < 4; j++) {
                nSx = j * nW;
                nSy = i * nH;
                sprPeety = new Sprite(txSheet, nSx, nSy, nW, nH);
                arSprPeety[j] = new Sprite(sprPeety);
            }
            araniPeety[i] = new Animation(nAniSpeed, arSprPeety);

        }
    }

    public void frameAnimation() {

        if (!Gdx.input.isKeyPressed(Input.Keys.D)
                && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (nPos == 0) {
                nFrame = 10;
            } else if (nPos == 1) {
                nFrame = 0;
                // Resets 1st frame when player stopped
            }
        } else {
            nFrame++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            nPos = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            nPos = 0;
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
        b2dr.dispose();
        world.dispose();
        txSheet.dispose();
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
