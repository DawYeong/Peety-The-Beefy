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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import gdx.peetythebeefy.cookiecutters.*;

import java.util.ArrayList;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;
import static gdx.peetythebeefy.cookiecutters.Constants.isPlayerDead;


/**
 * @author tn200
 */
public class ScrLvl1 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch, fixedBatch;
    ShapeRenderer SR;
    World world;
    float fX, fY, fW, fH;
    EntityCreation ecPlayer;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();
    ArrayList<EntityCreation> alBullet = new ArrayList<EntityCreation>();
    ArrayList<EntityCreation> alEnemy = new ArrayList<EntityCreation>();
    TiledMap tMapLvl1;
    TiledPolyLines tplLvl1;
    Vector2 v2Target, vMousePosition;
    int nLevelHeight, nLevelWidth, nSpawnrate = 0, nCount = 0, nEnemies = 0, nMaxEnemies = 2, nWaveCount = 1;
    Texture txBackground, txSky, txWatergun;
    Sprite sprWatergun;
    static boolean isChangedToLvl2 = false;
    static float fAlpha = 1, fTransitWidth = 0, fTransitHeight = 0;

    public ScrLvl1(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.SR = game.SR;
        //Drawing things like GUI requires fixedBatch (not updated with camera)
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
        txSky = new Texture("sky.png");
        txWatergun = new Texture("Watergun.png");
        sprWatergun = new Sprite(txWatergun);

        createButtons();
        tMapLvl1 = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        tplLvl1 = new TiledPolyLines(world, tMapLvl1.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL,
                (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0);
        otmr = new OrthogonalTiledMapRenderer(tMapLvl1);

        //Gets the properties from the tiledmap, used for the Camera Boundary
        MapProperties props = tMapLvl1.getProperties();
        nLevelWidth = props.get("width", Integer.class);
        nLevelHeight = props.get("height", Integer.class);

        //Entity Creation handles all creation of objects
        ecPlayer = new EntityCreation(world, "PLAYER", fX, fY, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", 1,
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL | Constants.BIT_ENEMY), (short) 0, new Vector2(0, 0), 4);
        v2Target = new Vector2(Constants.SCREENWIDTH / 2, Constants.SCREENHEIGHT / 2);
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
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        fixedBatch.begin();
        fixedBatch.draw(txSky, 0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        fixedBatch.end();

        batch.begin();
        batch.draw(txBackground, 0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.end();

        if (isChangedToLvl2) { //puts the player outside the door so they don't trigger the transition
            ecPlayer.body.setTransform((float) (690 / PPM), (float) (450 / PPM), 0);
            isChangedToLvl2 = false;
        }

        ecPlayer.Update();
        moveEnemy();

        otmr.setView(camera);
        otmr.render();

        //used for the gun following the mouse
        vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        Constants.playerGUI(fixedBatch, batch, ecPlayer.body.getPosition(), vMousePosition);

        if (Constants.isGameStart) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
                game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                game.fMouseY = Constants.SCREENHEIGHT;
                Constants.isShowing = !Constants.isShowing; //its like a pop up menu, if you want to go back press p to bring up back button
            }
            if (Gdx.input.isKeyJustPressed((Input.Keys.J))) {
                Constants.isLevelFinished[0] = true;
                Constants.isLevelUnlocked[1] = true;
                game.updateScreen(4);
            }

            playerShoot(ecPlayer.body.getPosition(), vMousePosition, alBullet, world);

            playerDeath();

            //Bullet Collection
            for (int i = 0; i < alBullet.size(); i++) {
                alBullet.get(i).Update();
                if (Constants.isShowing) {
                    alBullet.get(i).body.setAwake(false);
                } else if (!Constants.isShowing && !alBullet.get(i).isStuck) {
                    alBullet.get(i).body.setAwake(true);
                }
                if (alBullet.get(i).canCollect) {
                    if (ecPlayer.body.getPosition().x - (ecPlayer.body.getMass() / 2) <= alBullet.get(i).body.getPosition().x + (alBullet.get(i).body.getMass() * 2) &&
                            ecPlayer.body.getPosition().x + (ecPlayer.body.getMass() / 2) >= alBullet.get(i).body.getPosition().x - (alBullet.get(i).body.getMass() * 2) &&
                            ecPlayer.body.getPosition().y - (ecPlayer.body.getMass() / 2) <= alBullet.get(i).body.getPosition().y + (alBullet.get(i).body.getMass() * 2) &&
                            ecPlayer.body.getPosition().y + (ecPlayer.body.getMass() / 2) >= alBullet.get(i).body.getPosition().y - (alBullet.get(i).body.getMass() * 2)
                            || Constants.isPlayerDead) {
                        alBullet.get(i).world.destroyBody(alBullet.get(i).body);
                        Constants.nBulletCount++;
                        alBullet.remove(i);
                    }
                }
            }

            //b2dr.render(world, camera.combined.scl(PPM));
        }

        if (!Constants.isGameStart) {
            ecPlayer.body.setAwake(false);
            ecPlayer.isMoving = false;
        } else {
            if (Constants.isShowing) {
                drawButtons();
                ecPlayer.body.setAwake(false);
                ecPlayer.isMoving = false;
            } else {
                ecPlayer.body.setAwake(true);
                ecPlayer.isMoving = true;
                createEnemy();
            }
        }

        screenTransition();
        transitionBlock();

    }

    public void createEnemy() { //Makes the enemies in entity creation, based on spawn locations and when they spawn
        if (nSpawnrate > 200 && nEnemies < nMaxEnemies && nWaveCount != 3) {
            int nSpawnLocation = (int) (Math.random() * 3 + 1);
            if (nSpawnLocation == 1) {
                fX = Gdx.graphics.getWidth() / 2 - 328;
                fY = Gdx.graphics.getHeight() / 2 + 50;
            } else if (nSpawnLocation == 2) {
                fX = Gdx.graphics.getWidth() / 2 + 328;
                fY = Gdx.graphics.getHeight() / 2 + 50;
            } else if (nSpawnLocation == 3) {
                fX = Gdx.graphics.getWidth() / 2;
                fY = Gdx.graphics.getHeight() / 2 + 160;
            }
            nEnemies++;
            alEnemy.add(new EntityCreation(world, "ENEMY", fX, fY, fW - 10, fH, batch, 9.2f,
                    0, 0, 0, 4, 1, "MTMsprite.png", 2,
                    Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                    new Vector2(0, 0), 2));
            nCount++;
            nSpawnrate = 0;
        }
        if (alEnemy.size() == 0 && nEnemies == nMaxEnemies) {
            nWaveCount++;
            nMaxEnemies++;
            nEnemies = 0;
        }
        if (nWaveCount == 3 && !isPlayerDead && (ecPlayer.body.getPosition().x * PPM > 710 && ecPlayer.body.getPosition().x * PPM < 750) &&
                (ecPlayer.body.getPosition().y * PPM > 410 && ecPlayer.body.getPosition().y * PPM < 480)) {
            Constants.isLevelFinished[0] = true;
            Constants.isLevelUnlocked[1] = true;
            Constants.isGameStart = false;
            Constants.isFadeIn[1] = true;
        }
        nSpawnrate++;
    }

    public void moveEnemy() {
        for (int i = 0; i < alEnemy.size(); i++) {
            alEnemy.get(i).Update();
            if (Constants.isShowing || !Constants.isGameStart) {
                alEnemy.get(i).body.setAwake(false);
                alEnemy.get(i).isMoving = false;
            } else {
                alEnemy.get(i).body.setAwake(true);
                alEnemy.get(i).isMoving = true;
            }
            if (ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y + 100 / PPM &&
                    ecPlayer.body.getPosition().y >= alEnemy.get(i).body.getPosition().y ||
                    ecPlayer.body.getPosition().y > alEnemy.get(i).body.getPosition().y - 100 / PPM &&
                            ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y) {
                if (ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x + 100 / PPM &&
                        ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                } else if (ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x - 100 / PPM &&
                        ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                }
            } else {
                alEnemy.get(i).isInRange = false;
            }
            if (alEnemy.get(i).isDeath || Constants.isPlayerDead) {
                alEnemy.get(i).world.destroyBody(alEnemy.get(i).body);
                nCount--;
                alEnemy.remove(i);
            }

        }
    }

    public void cameraUpdate() {
        //CameraStyles.java explains camera movement
        CameraStyles.lerpAverageBetweenTargets(camera, v2Target, ecPlayer.body.getPosition().scl(PPM));
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
                Constants.isShowing = false;
                ScrMainMenu.fAlpha = 0;
                ScrStageSelect.fAlpha = 0;
                Constants.nCurrentScreen = 3;
                game.updateScreen(0);
            }
        }
    }


    public void playerDeath() {
        if (Constants.isPlayerDead && alEnemy.size() == 0 && alBullet.size() == 0) {
            Constants.nHealth = 4;
            ecPlayer.body.setTransform(Gdx.graphics.getWidth() / 2 / PPM, Gdx.graphics.getHeight() / 2 / PPM, 0);
            nEnemies = 0;
            nMaxEnemies = 3;
            nWaveCount = 1;
            nSpawnrate = 0;
            game.updateScreen(5);
        }
    }

    public void playerShoot(Vector2 playerPosition, Vector2 mousePosition, ArrayList Bullets, World world) {
        //moved mouse position vector to draw because we need it for other things
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched()) {
            if (Constants.nBulletCount > 0 && !Constants.isShowing) {
                Vector2 vbulletPosition = new Vector2(playerPosition.x * 32, playerPosition.y * 32);
                Vector2 vDir = mousePosition.sub(vbulletPosition);
                Bullets.add(new EntityCreation(world, "Bullet", vbulletPosition.x, vbulletPosition.y, fW, fH, batch, 9.2f, 0, 0,
                        0, 4, 6, "bulletTexture.png", 3,
                        Constants.BIT_BULLET, (short) (Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                        vDir, 0));
                Constants.nBulletCount--;
            }
        }
    }

    public void transitionBlock() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SR.begin(ShapeRenderer.ShapeType.Filled);
        SR.setColor(new Color(0f, 0f, 0f, fAlpha));
        SR.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SR.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        SR.begin(ShapeRenderer.ShapeType.Filled);
        SR.setColor(0, 0, 0, 1);
        SR.rect(Gdx.graphics.getWidth() / 2 - (fTransitWidth / 2), Gdx.graphics.getHeight() / 2 - (fTransitHeight / 2), fTransitWidth, fTransitHeight);
        SR.end();
    }

    public void screenTransition() {
        if (Constants.isFadeOut[0] && fAlpha > 0) {
            fAlpha -= 0.01f;
        }
        if (fAlpha < 0 && !Constants.isFadeIn[1]) {
            Constants.isFadeOut[0] = false;
            Constants.isGameStart = true;
        }
        if (Constants.isFadeIn[1] && fTransitWidth <= Gdx.graphics.getWidth()) {
            System.out.println(Constants.isGameStart);
            fTransitHeight += 16;
            fTransitWidth += 16;
        }
        if (fTransitWidth > Gdx.graphics.getWidth()) {
            Constants.isFadeOut[1] = true;
            Constants.isFadeIn[1] = false;
            ScrLvl2.fTransitWidth = Gdx.graphics.getWidth() * (float) 1.5;
            ScrLvl2.fTransitHeight = Gdx.graphics.getHeight() * (float) 1.5;
            game.updateScreen(4);
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
        fixedBatch.dispose();
        b2dr.dispose();
        world.dispose();
        ecPlayer.cleanup();
        otmr.dispose();
        game.dispose();
        game.scrLvl1.dispose();
        tMapLvl1.dispose();
        for (int i = 0; i < alEnemy.size(); i++) {
            alEnemy.get(i).cleanup();
        }
        for (int i = 0; i < alBullet.size(); i++) {
            alBullet.get(i).cleanup();
        }
        txBackground.dispose();
        txSky.dispose();
        Constants.assetsDispose();
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
        if (Constants.isShowing) {  // going to have to set fMouseX and fMouseY here because of the problem with setting the input processor
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