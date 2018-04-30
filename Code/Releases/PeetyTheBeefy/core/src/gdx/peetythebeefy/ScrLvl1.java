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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.peetythebeefy.cookiecutters.*;

import java.util.ArrayList;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;


/**
 * @author tn200
 */
public class ScrLvl1 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    SpriteAnimation aniPeety, aniMeaty, aniMeaty2;
    World world;
    float fX, fY, fW, fH;
    Box2D b2Player;
    Box2D[] arb2Enemies = new Box2D[2];
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();
    TiledMap tMap;

    TextureRegion trTemp, trTempMeat, trTempMeat2;
    int nCount;
    static boolean isShowing = false;

    public ScrLvl1(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        world = new World(new Vector2(0f, -18f), false);
        world.setVelocityThreshold(0f);
        fX = Constants.SCREENWIDTH / 2;
        fY = Constants.SCREENHEIGHT / 2;
        fW = 32;
        fH = 32;

        aniPeety = new SpriteAnimation(9.2f, 0, 0, 0, 4, 6, "PTBsprite.png");
        aniMeaty = new SpriteAnimation(9.2f,0,0,0,4,1,"MTMsprite.png");
        aniMeaty2 = new SpriteAnimation(9.2f,0,0,0,4,1,"MTMsprite.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);
        b2dr = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        createButtons();
        tMap = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        otmr = new OrthogonalTiledMapRenderer(tMap);


        TiledPolyLines.parseTiledObjectLayer(world, tMap.getLayers().get("collision-layer").getObjects());
        if (nCount == 0) { //creates the boxes only once so it doesn't duplicate everytime the screen changes
            b2Player = new Box2D(world, "PLAYER", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 32, 32);
            arb2Enemies[0] = new Box2D(world, "ENEMIES1", fX + 100, fY + 50, fW, fH);
            arb2Enemies[1] = new Box2D(world, "ENEMIES2", fX - 100, fY + 50, fW, fH);
            nCount++;
        }
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        frameAnimation();
        trTemp = (TextureRegion) aniPeety.araniPeety[aniPeety.nPos].getKeyFrame(aniPeety.nFrame, true);
        trTempMeat = (TextureRegion) aniMeaty.araniPeety[aniMeaty.nPos].getKeyFrame(aniMeaty.nFrame,true);
        trTempMeat2 = (TextureRegion) aniMeaty2.araniPeety[aniMeaty2.nPos].getKeyFrame(aniMeaty2.nFrame,true);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
            game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
            game.fMouseY = Constants.SCREENHEIGHT;
            if (isShowing == false) { //its like a pop up menu, if you want to go back press p to bring up back button
                isShowing = true;
            } else {
                isShowing = false;
            }
        }
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        otmr.setView(camera);
        otmr.render();
        b2dr.render(world, camera.combined.scl(PPM));

        batch.begin();
        batch.draw(trTemp, b2Player.body.getPosition().x * PPM - fW / 2, b2Player.body.getPosition().y * PPM - fH / 2, fW, fH);
        batch.draw(trTempMeat,arb2Enemies[0].body.getPosition().x * PPM - fW / 2,arb2Enemies[0].body.getPosition().y * PPM - fH/ 2, fW, fH);
        batch.draw(trTempMeat2,arb2Enemies[1].body.getPosition().x * PPM - fW / 2,arb2Enemies[1].body.getPosition().y * PPM - fH / 2, fW, fH);
        batch.end();

        if (isShowing == true) {
            drawButtons();
        }

        b2Player.playerMove();
        for (int i = 0; i < 2; i++) {
            if (b2Player.body.getPosition().y < arb2Enemies[i].body.getPosition().y + 100 / PPM &&
                    b2Player.body.getPosition().y >= arb2Enemies[i].body.getPosition().y ||
                    b2Player.body.getPosition().y > arb2Enemies[i].body.getPosition().y - 100 / PPM &&
                            b2Player.body.getPosition().y < arb2Enemies[i].body.getPosition().y) {
                if (b2Player.body.getPosition().x < arb2Enemies[i].body.getPosition().x + 100 / PPM &&
                        b2Player.body.getPosition().x >= arb2Enemies[i].body.getPosition().x) {
                    arb2Enemies[i].isInRange = true;
                    arb2Enemies[i].nDir = 1;
                } else if (b2Player.body.getPosition().x > arb2Enemies[i].body.getPosition().x - 100 / PPM &&
                        b2Player.body.getPosition().x < arb2Enemies[i].body.getPosition().x) {
                    arb2Enemies[i].nDir = 2;
                    arb2Enemies[i].isInRange = true;
                }
            } else {
                arb2Enemies[i].isInRange = false;
            }
            arb2Enemies[i].enemyMove();
        }
        System.out.println(arb2Enemies[0].body.getLinearVelocity().x);
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
            if (game.fMouseX > alButtons.get(i).fX && game.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && game.fMouseY > alButtons.get(i).fY && game.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                System.out.println("move to main menu ");
                game.updateScreen(0);
                game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                game.fMouseY = Constants.SCREENHEIGHT;
            }
        }
    }

    public void frameAnimation() {
        if (b2Player.body.getLinearVelocity().x != 0 || b2Player.body.getLinearVelocity().y != 0) {
            aniPeety.nFrame++;
        }
        if (aniPeety.nFrame > 32) {
            aniPeety.nFrame = 0;
        }
        if(aniMeaty2.nFrame > 32) {
            aniMeaty2.nFrame = 0;
        }
        aniMeaty.nFrame++;
        aniMeaty2.nFrame++;

        if (Gdx.input.isKeyPressed(Input.Keys.A) && b2Player.body.getLinearVelocity().y >= 0) { //going left
            aniPeety.nDirection = 1;
            aniPeety.nPos = 1;
            aniPeety.fAniSpeed = 9.2f;
            aniPeety.araniPeety[aniPeety.nPos].setFrameDuration(aniPeety.fAniSpeed);
            //playerSprite(aniPeety.fAniSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && b2Player.body.getLinearVelocity().y >= 0) { //going right
            aniPeety.nDirection = 0;
            aniPeety.nPos = 0;
            aniPeety.fAniSpeed = 9.2f;
            aniPeety.araniPeety[aniPeety.nPos].setFrameDuration(aniPeety.fAniSpeed);
        }
        if (b2Player.body.getLinearVelocity().y < 0) { //falling animation + speed up
            aniPeety.nPos = 5;
            if (aniPeety.fAniSpeed >= 2.3f) {
                aniPeety.araniPeety[aniPeety.nPos].setFrameDuration(aniPeety.fAniSpeed -= 0.1f);
            }
        }
        if (b2Player.body.getLinearVelocity().x == 0 && b2Player.body.getLinearVelocity().y == 0) { //reset to last direction when stationary
            if (aniPeety.nDirection == 1) {
                aniPeety.nPos = 1;
            } else if (aniPeety.nDirection == 0) {
                aniPeety.nPos = 0;
            }
            aniPeety.fAniSpeed = 9.2f;
            aniPeety.araniPeety[aniPeety.nPos].setFrameDuration(aniPeety.fAniSpeed);
        }
    }

    @Override
    public void resize(int i, int i1) {
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
        batch.dispose();
        b2dr.dispose();
        world.dispose();
        aniPeety.cleanup();
        aniMeaty.cleanup();
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
