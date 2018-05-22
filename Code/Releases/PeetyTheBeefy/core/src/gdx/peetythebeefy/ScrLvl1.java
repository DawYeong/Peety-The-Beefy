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
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import gdx.peetythebeefy.cookiecutters.*;

import java.util.ArrayList;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;


/**
 * @author tn200
 */
public class ScrLvl1 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch, fixedBatch;
    World world;
    float fX, fY, fW, fH;
    Box2D b2Player;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();
    ArrayList<Box2D> alBullet = new ArrayList<Box2D>();
    ArrayList<Box2D> alEnemy = new ArrayList<Box2D>();
    TiledMap tMapLvl1;
    TiledPolyLines tplLvl1;
    Vector2 v2Target, vDir, vMousePosition, vbulletPosition;
    int nLevelHeight, nLevelWidth, nMax = 0, nSpawn, nCount = 0;
    Texture txBackground;

    static boolean isShowing = false;

    public ScrLvl1(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        fixedBatch = new SpriteBatch();
        this.camera = game.camera;
        world = new World(new Vector2(0f, -18f), false);
        world.setContactListener(new ContactListener1());
        world.setVelocityThreshold(0f);
        fX = Constants.SCREENWIDTH / 2;
        fY = Constants.SCREENHEIGHT / 2;
        fW = 32;
        fH = 32;
        txBackground = new Texture("level1Background.png");

        createButtons();
        tMapLvl1 = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        tplLvl1 = new TiledPolyLines(world, tMapLvl1.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL,
                (short)(Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0);
        otmr = new OrthogonalTiledMapRenderer(tMapLvl1);

        MapProperties props = tMapLvl1.getProperties();
        nLevelWidth = props.get("width", Integer.class) ;
        nLevelHeight = props.get("height", Integer.class);

        b2Player = new Box2D(world, "PLAYER", fX, fY, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", false, false,
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL | Constants.BIT_ENEMY), (short) 0, new Vector2(0,0));
       // createEnemy();
        v2Target = new Vector2(Constants.SCREENWIDTH / 2, Constants.SCREENHEIGHT / 2);

        nSpawn = 10;
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
            game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
            game.fMouseY = Constants.SCREENHEIGHT;
            if (isShowing == false) { //its like a pop up menu, if you want to go back press p to bring up back button
                isShowing = true;
            } else {
                isShowing = false;
            }
        }
        if(Gdx.input.isKeyJustPressed((Input.Keys.J))) {
            game.updateScreen(1);
        }
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(txBackground,0,0,Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.end();

        createEnemy();

        b2Player.Update();
        moveEnemy();
        for(int i = 0; i < alBullet.size();i++) {
            alBullet.get(i).Update();
            if(isShowing) {
                alBullet.get(i).body.setAwake(false);
            } else if(!isShowing && !alBullet.get(i).isStuck){
                alBullet.get(i).body.setAwake(true);
            }
            if(alBullet.get(i).canCollect) {
                if (b2Player.body.getPosition().x - (b2Player.body.getMass() / 2) <= alBullet.get(i).body.getPosition().x + (alBullet.get(i).body.getMass() *2) &&
                        b2Player.body.getPosition().x + (b2Player.body.getMass() / 2) >= alBullet.get(i).body.getPosition().x - (alBullet.get(i).body.getMass()*2) &&
                        b2Player.body.getPosition().y - (b2Player.body.getMass() /2) <= alBullet.get(i).body.getPosition().y + (alBullet.get(i).body.getMass()*2) &&
                        b2Player.body.getPosition().y + (b2Player.body.getMass() / 2) >= alBullet.get(i).body.getPosition().y - (alBullet.get(i).body.getMass()*2)) {
                    alBullet.get(i).world.destroyBody(alBullet.get(i).body);
                    nMax--;
                    alBullet.remove(i);
                }
            }
        }

        otmr.setView(camera);
        otmr.render();
        //b2dr.render(world, camera.combined.scl(PPM));


        if (isShowing == true) {
            drawButtons();
            b2Player.body.setAwake(false);
            b2Player.isMoving = false;
        } else {
            b2Player.body.setAwake(true);
            b2Player.isMoving = true;
        }
        //System.out.println(game.fMouseX + " " + game.fMouseY);
    }

    public void createEnemy() {
        if(nSpawn > 200 && nCount< 11) {
            alEnemy.add(new Box2D(world, "ENEMY", fX + 100, fY + 50, fW - 10, fH, batch, 9.2f,
                    0, 0, 0, 4, 1, "MTMsprite.png", true, false,
                    Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                    new Vector2(0, 0)));
//            alEnemy.add(new Box2D(world, "ENEMY", fX - 100, fY + 50, fW - 10, fH, batch, 9.2f,
//                    0, 0, 0, 4, 1, "MTMsprite.png", true, false,
//                    Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
//                    new Vector2(0, 0)));
            nCount++;
            nSpawn = 0;
        }
        nSpawn++;
    }
    public void moveEnemy() {
        for(int i = 0; i < alEnemy.size(); i++) {
            alEnemy.get(i).Update();
            if(isShowing) {
                alEnemy.get(i).body.setAwake(false);
                alEnemy.get(i).isMoving = false;
            } else {
                alEnemy.get(i).body.setAwake(true);
                alEnemy.get(i).isMoving = true;
            }
            if (b2Player.body.getPosition().y < alEnemy.get(i).body.getPosition().y + 100 / PPM &&
                    b2Player.body.getPosition().y >= alEnemy.get(i).body.getPosition().y ||
                    b2Player.body.getPosition().y > alEnemy.get(i).body.getPosition().y - 100 / PPM &&
                            b2Player.body.getPosition().y < alEnemy.get(i).body.getPosition().y) {
                if (b2Player.body.getPosition().x < alEnemy.get(i).body.getPosition().x + 100 / PPM &&
                        b2Player.body.getPosition().x >= alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                    alEnemy.get(i).nDir = 1;
                } else if (b2Player.body.getPosition().x > alEnemy.get(i).body.getPosition().x - 100 / PPM &&
                        b2Player.body.getPosition().x < alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).nDir = 2;
                    alEnemy.get(i).isInRange = true;
                }
            } else {
                alEnemy.get(i).isInRange = false;
            }
            if(alEnemy.get(i).isDeath) {
                alEnemy.get(i).world.destroyBody(alEnemy.get(i).body);
                nCount--;
                alEnemy.remove(i);
            }

        }
    }

    public void cameraUpdate() {
        CameraStyles.lerpAverageBetweenTargets(camera, v2Target,b2Player.body.getPosition().scl(PPM));
        float fStartX = camera.viewportWidth / 2;
        float fStartY = camera.viewportHeight / 2;
        camera.zoom = 0.8f;
        CameraStyles.boundary(camera, fStartX, fStartY, nLevelWidth * 32 - fStartX * 2, nLevelHeight * 32 - fStartY * 2);
        camera.update();

    }

    public void createButtons() {
        alButtons.add(new Buttons("backButton", fixedBatch, -8, 0, 96, 32));
    }

    public void drawButtons() {
        for (int i = 0; i < alButtons.size(); i++) {
            alButtons.get(i).Update();
            if (game.fMouseX > alButtons.get(i).fX && game.fMouseX < alButtons.get(i).fX + alButtons.get(i).fW
                    && game.fMouseY > alButtons.get(i).fY && game.fMouseY < alButtons.get(i).fY + alButtons.get(i).fH) {
                System.out.println("move to main menu ");
                game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                game.fMouseY = Constants.SCREENHEIGHT;
                game.updateScreen(0);
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
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
        fixedBatch.dispose();
        b2dr.dispose();
        world.dispose();
        b2Player.cleanup();
        otmr.dispose();
        game.dispose();
        game.scrLvl1.dispose();
        tMapLvl1.dispose();
        for(int i = 0; i < alEnemy.size(); i++) {
            alEnemy.get(i).cleanup();
        }
        for(int i =0 ; i < alBullet.size(); i++) {
            alBullet.get(i).cleanup();
        }
        txBackground.dispose();
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
        if(!isShowing) {
            if (nMax < 4) {
                vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                vbulletPosition = new Vector2(b2Player.body.getPosition().x * 32, b2Player.body.getPosition().y * 32);
                vDir = vMousePosition.sub(vbulletPosition);
                alBullet.add(new Box2D(world, "Bullet", vbulletPosition.x, vbulletPosition.y, fW, fH, batch, 9.2f, 0, 0,
                        0, 4, 6, "bulletTexture.png", false, true,
                        Constants.BIT_BULLET, (short) (Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                        vDir));
                nMax++;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        if(isShowing) {  // going to have to set fMouseX and fMouseY here because of the problem with setting the input processor
            game.fMouseX = Gdx.input.getX();
            game.fMouseY = Constants.SCREENHEIGHT - Gdx.input.getY();
        }
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