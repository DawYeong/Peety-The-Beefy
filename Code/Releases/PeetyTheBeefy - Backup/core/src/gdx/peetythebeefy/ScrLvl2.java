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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import static gdx.peetythebeefy.cookiecutters.Constants.isShowing;

/**
 * @author tn200
 */
public class ScrLvl2 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    BitmapFont font;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    Text tLvl2;
    ShapeRenderer SR;
    Buttons BackButton;
    TiledMap tMapLvl2;
    TiledPolyLines tplLvl2;
    World world;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    Box2DDebugRenderer b2dr;
    ScrLvl1 scrLvl1;
    EntityCreation ecPlayer;
    float fX, fY, fW, fH, fEX, fEY;
    int nLevelWidth, nLevelHeight;
    int nSpawnRate = 0, nEnemies = 0, nMaxEnemies = 2, nWaveCount = 1, nSpawnLocation;
    Texture txBackground, txSky;
    Vector2 vMousePosition, vEnemyShootDir, vEBulletPos, vTargetPos;
    ArrayList<EntityCreation> alEnemy = new ArrayList<EntityCreation>();
    ArrayList<EntityCreation> alEnemyBullet = new ArrayList<EntityCreation>();
    ArrayList<EntityCreation> alBullet = new ArrayList<EntityCreation>();
    static float fTransitWidth, fTransitHeight;

    public ScrLvl2(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = game.camera;
        this.SR = game.SR;
        this.font = game.font;
        scrLvl1 = new ScrLvl1(game);
        this.b2dr = scrLvl1.b2dr;
        this.parameter = game.parameter;
        world = new World(new Vector2(0f, -18f), false);
        world.setContactListener(new ContactListener1());
        world.setVelocityThreshold(3f);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));
        fX = Constants.SCREENWIDTH / 2;
        fY = Constants.SCREENHEIGHT / 2;
        fW = 32;
        fH = 32;
        fTransitHeight = Gdx.graphics.getHeight() * (float) 1.5;
        fTransitWidth = Gdx.graphics.getWidth() * (float) 1.5;

        BackButton = new Buttons("backButton", scrLvl1.fixedBatch, -8, 0, 96, 32);

        tMapLvl2 = new TmxMapLoader().load("PeetytheBeefy2.tmx");
        tplLvl2 = new TiledPolyLines(world, tMapLvl2.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL,
                (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0);
        txBackground = new Texture("level2Background.png");
        txSky = new Texture("skyDay.png");
        otmr = new OrthogonalTiledMapRenderer(tMapLvl2);


        MapProperties props = tMapLvl2.getProperties();
        nLevelWidth = props.get("width", Integer.class);
        nLevelHeight = props.get("height", Integer.class);

        ecPlayer = new EntityCreation(world, "PLAYER", fX - 300, fY - 215, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", 1,
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0, new Vector2(0, 0),
                scrLvl1.ecPlayer.nHealth);
        createText();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        scrLvl1.fixedBatch.begin();
        scrLvl1.fixedBatch.draw(txSky, 0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        scrLvl1.fixedBatch.end();

        batch.begin();
        batch.draw(txBackground, 0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.end();

        ecPlayer.Update();
        moveEnemy();
        otmr.setView(camera);
        otmr.render();

        if (Constants.isGameStart) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
                game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
                game.fMouseY = Constants.SCREENHEIGHT;
                Constants.isShowing = !Constants.isShowing; //its like a pop up menu, if you want to go back press p to bring up back button
            }

            scrLvl1.playerShoot(ecPlayer.body.getPosition(), vMousePosition, alBullet, world);

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
                            ecPlayer.body.getPosition().y + (ecPlayer.body.getMass() / 2) >= alBullet.get(i).body.getPosition().y - (alBullet.get(i).body.getMass() * 2)) {
                        alBullet.get(i).world.destroyBody(alBullet.get(i).body);
                        Constants.nBulletCount++;
                        alBullet.remove(i);
                    }
                }
            }
            tLvl2.Update();
        }

        //Un-comment this if you want to see the Box2D debug renderer
//        b2dr.render(world, camera.combined.scl(PPM));
        vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        game.playerGUI(scrLvl1.fixedBatch, batch, ecPlayer.body.getPosition(), vMousePosition, font, generator);

        if (!Constants.isGameStart) {
            ecPlayer.body.setAwake(false);
            ecPlayer.isMoving = false;
        } else {
            if (Constants.isShowing) {
                backButtonFunctionality();
                ecPlayer.body.setAwake(false);
                ecPlayer.isMoving = false;
            } else {
                ecPlayer.body.setAwake(true);
                ecPlayer.isMoving = true;
                if(tLvl2.isFinished) {
                    createEnemy();
                }
            }
        }
        screenTransition();
        transitionBlock();
    }

    public void backButtonFunctionality() {
        BackButton.Update();
        if (game.fMouseX > BackButton.fX && game.fMouseX < BackButton.fX + BackButton.fW
                && game.fMouseY > BackButton.fY && game.fMouseY < BackButton.fY + BackButton.fH) {
            System.out.println("moves to the main menu");
            Constants.isShowing = false;
            Constants.isFadeIn[1] = false;
            ScrLvl1.fTransitWidth = 0;
            ScrLvl1.fTransitHeight = 0;
            ScrMainMenu.fAlpha = 0;
            ScrStageSelect.fAlpha = 0;
            fTransitHeight = Gdx.graphics.getHeight() * (float) 1.5;
            fTransitWidth = Gdx.graphics.getWidth() * (float) 1.5;
            Constants.nCurrentScreen = 4;
            ScrLvl1.isChangedToLvl2 = true;
            game.updateScreen(0);
        }
    }

    public void createText(){
        tLvl2 = new Text(generator, parameter, font, "Peety The Beefy Meets a Dreamy Sweetie", 26, Gdx.graphics.getWidth()/2,
                Gdx.graphics.getHeight()/2, scrLvl1.fixedBatch, 1, 1, "Level");
    }

    public void createEnemy() {
        if(nSpawnRate > 200 && nEnemies < nMaxEnemies && nWaveCount != 3) {
            nSpawnLocation = (int) (Math.random() * 6 + 1);
            switch (nSpawnLocation) {
                case 1:
                    fEX = Gdx.graphics.getWidth()/2;
                    fEY = Gdx.graphics.getHeight()/2 + 81;
                    break;
                case 2:
                    fEX = Gdx.graphics.getWidth()/2 - 337;
                    fEY = Gdx.graphics.getHeight()/2 + 17;
                    break;
                case 3:
                    fEX = Gdx.graphics.getWidth()/2 + 336;
                    fEY = Gdx.graphics.getHeight()/2 + 17;
                    break;
                case 4:
                    fEX = Gdx.graphics.getWidth()/2 + 303;
                    fEY = Gdx.graphics.getHeight()/2 - 239;
                    break;
                case 5:
                    fEX = Gdx.graphics.getWidth()/2 - 226;
                    fEY = Gdx.graphics.getHeight()/2 + 273;
                    break;
                case 6:
                    fEX = Gdx.graphics.getWidth()/2 + (float) 224.5;
                    fEY = Gdx.graphics.getHeight()/2 + 273;
                    break;
            }
            System.out.println(nSpawnLocation);
            nEnemies++;
            alEnemy.add(new EntityCreation(world, "ENEMY", fEX, fEY, fW - 10, fH, batch, 9.2f,
                    0, 0, 0, 4, 1, "MTMsprite.png", 2,
                    Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                    new Vector2(0, 0), 2));
            nSpawnRate = 0;
        }
        if (alEnemy.size() == 0 && nEnemies == nMaxEnemies) {
            nWaveCount++;
            nMaxEnemies++;
            nEnemies = 0;
        }
        nSpawnRate++;
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
                if (alEnemy.get(i).isInRange) {
                    if (alEnemy.get(i).nShootCount == 100) {
                        vEBulletPos = new Vector2(alEnemy.get(i).body.getPosition().x * 32, alEnemy.get(i).body.getPosition().y * 32);
                        vTargetPos = new Vector2(ecPlayer.body.getPosition().x * 32, ecPlayer.body.getPosition().y * 32);
                        vEnemyShootDir = vTargetPos.sub(vEBulletPos);
                        System.out.println(vEnemyShootDir);
                        alEnemyBullet.add(new EntityCreation(world, "EnemyBullet", alEnemy.get(i).body.getPosition().x * 32, alEnemy.get(i).body.getPosition().y * 32,
                                fW, fH, batch, 9.2f, 0, 0, 0, 4, 6,
                                "Heart-Full.png", 3, Constants.BIT_ENEMYBULLET, (short)
                                (Constants.BIT_WALL | Constants.BIT_ENEMYBULLET | Constants.BIT_PLAYER), (short) 0,
                                vEnemyShootDir, 0));
                        System.out.println("here");
                        alEnemy.get(i).nShootCount = 0;
                    }
                    alEnemy.get(i).nShootCount++;
                }
            }
            if (ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y + 200 / PPM &&
                    ecPlayer.body.getPosition().y >= alEnemy.get(i).body.getPosition().y ||
                    ecPlayer.body.getPosition().y > alEnemy.get(i).body.getPosition().y - 200 / PPM &&
                            ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y) {
                if (ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x + 200 / PPM &&
                        ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                } else if (ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x - 200 / PPM &&
                        ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                }
            } else {
                alEnemy.get(i).isInRange = false;
            }
            if (alEnemy.get(i).isDeath) {
                alEnemy.get(i).world.destroyBody(alEnemy.get(i).body);
                alEnemy.remove(i);
            }
        }
        moveEnemyBullet();
    }

    public void moveEnemyBullet() {
        for (int i = 0; i < alEnemyBullet.size(); i++) {
            alEnemyBullet.get(i).Update();
            if (alEnemyBullet.get(i).isStuck) {
                alEnemyBullet.get(i).world.destroyBody(alEnemyBullet.get(i).body);
                alEnemyBullet.remove(i);
            }
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

    public void transitionBlock() {
        SR.begin(ShapeRenderer.ShapeType.Filled);
        SR.setColor(0, 0, 0, 1);
        SR.rect(Gdx.graphics.getWidth() / 2 - (fTransitWidth / 2), Gdx.graphics.getHeight() / 2 - (fTransitHeight / 2), fTransitWidth, fTransitHeight);
        SR.end();
    }

    public void screenTransition() {
        if (Constants.isFadeOut[1] && fTransitWidth >= 0) {
            fTransitHeight -= 16;
            fTransitWidth -= 16;
        }
        if (fTransitWidth <= 0) {
            Constants.isGameStart = true;
            Constants.isFadeOut[1] = false;
        }
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
        batch.dispose();
        scrLvl1.dispose();
        generator.dispose();
        font.dispose();
        tMapLvl2.dispose();
        scrLvl1.fixedBatch.dispose();
        otmr.dispose();
        b2dr.dispose();
        txBackground.dispose();
        txSky.dispose();
        for (int i = 0; i < alEnemy.size(); i++) {
            alEnemy.get(i).cleanup();
        }
        for (int i = 0; i < alEnemyBullet.size(); i++) {
            alEnemyBullet.get(i).cleanup();
        }
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