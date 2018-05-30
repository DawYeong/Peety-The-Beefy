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
import com.badlogic.gdx.graphics.g2d.Sprite;
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

import java.util.ArrayList;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;


/**
 * @author tn200
 */
public class ScrLvl1 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch, fixedBatch;
    World world;
    float fX, fY, fW, fH, fAngle;
    EntityCreation ecPlayer;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    ArrayList<Buttons> alButtons = new ArrayList<Buttons>();
    ArrayList<EntityCreation> alBullet = new ArrayList<EntityCreation>();
    ArrayList<EntityCreation> alEnemy = new ArrayList<EntityCreation>();
    TiledMap tMapLvl1;
    TiledPolyLines tplLvl1;
    Vector2 v2Target, vDir, vMousePosition, vbulletPosition;
    int nLevelHeight, nLevelWidth, nBulletCount = 4, nSpawnrate = 0, nCount = 0, nBulletGUIPos = 583, nHealthGUIPosy = 108,
    nEnemies = 0, nMaxEnemies = 3, nWaveCount = 1;
    Texture txBackground, txGUI, txHeart, txBullet, txSky, txWatergun;
    Sprite sprWatergun;

    static boolean isShowing = false, isPlayerDead = false; //required

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
        txGUI = new Texture("GUI.png");
        txHeart = new Texture("Heart-Full.png");
        txBullet = new Texture("bulletTexture.png");
        txSky = new Texture("sky.png");
        txWatergun = new Texture("Watergun.png");
        sprWatergun = new Sprite(txWatergun);

        createButtons();
        tMapLvl1 = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        tplLvl1 = new TiledPolyLines(world, tMapLvl1.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL,
                (short)(Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0);
        otmr = new OrthogonalTiledMapRenderer(tMapLvl1);

        MapProperties props = tMapLvl1.getProperties();
        nLevelWidth = props.get("width", Integer.class) ;
        nLevelHeight = props.get("height", Integer.class);

        ecPlayer = new EntityCreation(world, "PLAYER", fX, fY, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", false, false,
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL | Constants.BIT_ENEMY), (short) 0, new Vector2(0,0), 4);
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
        fixedBatch.begin();
        fixedBatch.draw(txSky,0,0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        fixedBatch.end();

        batch.begin();
        batch.draw(txBackground,0,0,Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.end();

        ecPlayer.Update();
        moveEnemy();
        for(int i = 0; i < alBullet.size();i++) {
            alBullet.get(i).Update();
            if(isShowing) {
                alBullet.get(i).body.setAwake(false);
            } else if(!isShowing && !alBullet.get(i).isStuck){
                alBullet.get(i).body.setAwake(true);
            }
            if(alBullet.get(i).canCollect) {
                if (ecPlayer.body.getPosition().x - (ecPlayer.body.getMass() / 2) <= alBullet.get(i).body.getPosition().x + (alBullet.get(i).body.getMass() *2) &&
                        ecPlayer.body.getPosition().x + (ecPlayer.body.getMass() / 2) >= alBullet.get(i).body.getPosition().x - (alBullet.get(i).body.getMass()*2) &&
                        ecPlayer.body.getPosition().y - (ecPlayer.body.getMass() /2) <= alBullet.get(i).body.getPosition().y + (alBullet.get(i).body.getMass()*2) &&
                        ecPlayer.body.getPosition().y + (ecPlayer.body.getMass() / 2) >= alBullet.get(i).body.getPosition().y - (alBullet.get(i).body.getMass()*2)
                        || isPlayerDead) {
                    alBullet.get(i).world.destroyBody(alBullet.get(i).body);
                    nBulletCount++;
                    alBullet.remove(i);
                }
            }
        }

        otmr.setView(camera);
        otmr.render();

        fixedBatch.begin();
        fixedBatch.draw(txGUI,Constants.SCREENWIDTH - txGUI.getWidth(),0);
        bulletsGUI();
        healthGUI();
        fixedBatch.end();
        //b2dr.render(world, camera.combined.scl(PPM));
        if (isShowing == true) {
            drawButtons();
            ecPlayer.body.setAwake(false);
            ecPlayer.isMoving = false;
        } else {
            ecPlayer.body.setAwake(true);
            ecPlayer.isMoving = true;
            createEnemy();
        }
        Watergun();
        playerDeath();

        //System.out.println(game.fMouseX + " " + game.fMouseY);
    }

    public void createEnemy() {
        if(nSpawnrate > 200 && nEnemies < nMaxEnemies) {
            int nSpawnLocation = (int) (Math.random() * 3 + 1);
            if (nSpawnLocation == 1) {
                fX = Gdx.graphics.getWidth()/2 - 328;
                fY = Gdx.graphics.getHeight()/2 + 50;
            } else if(nSpawnLocation == 2) {
                fX = Gdx.graphics.getWidth()/2 + 328;
                fY = Gdx.graphics.getHeight()/2 + 50;
            } else if(nSpawnLocation == 3) {
                fX = Gdx.graphics.getWidth()/2;
                fY = Gdx.graphics.getHeight()/2 + 160;
            }
            nEnemies ++;
                alEnemy.add(new EntityCreation(world, "ENEMY", fX , fY , fW - 10, fH, batch, 9.2f,
                        0, 0, 0, 4, 1, "MTMsprite.png", true, false,
                        Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                        new Vector2(0, 0), 4));
                nCount++;
                nSpawnrate = 0;
            }
            if(alEnemy.size() == 0 && nEnemies == nMaxEnemies) {
                nWaveCount ++;
                nMaxEnemies++;
                nEnemies = 0;
            }
            nSpawnrate++;
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
            if (ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y + 100 / PPM &&
                    ecPlayer.body.getPosition().y >= alEnemy.get(i).body.getPosition().y ||
                    ecPlayer.body.getPosition().y > alEnemy.get(i).body.getPosition().y - 100 / PPM &&
                            ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y) {
                if (ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x + 100 / PPM &&
                        ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                    alEnemy.get(i).nDir = 1;
                } else if (ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x - 100 / PPM &&
                        ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).nDir = 2;
                    alEnemy.get(i).isInRange = true;
                }
            } else {
                alEnemy.get(i).isInRange = false;
            }
            if(alEnemy.get(i).isDeath || isPlayerDead) {
                alEnemy.get(i).world.destroyBody(alEnemy.get(i).body);
                nCount--;
                alEnemy.remove(i);
            }

        }
    }

    public void cameraUpdate() {
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
                game.updateScreen(0);
            }
        }
    }
    public void bulletsGUI() {
        if(nBulletCount >= 1) {
            fixedBatch.draw(txBullet,nBulletGUIPos,14,27,27);
            if(nBulletCount >= 2) {
                fixedBatch.draw(txBullet,nBulletGUIPos - 45,14,27,27);
                if(nBulletCount >= 3) {
                    fixedBatch.draw(txBullet,nBulletGUIPos - 90,14,27,27);
                    if(nBulletCount >= 4) {
                        fixedBatch.draw(txBullet,nBulletGUIPos - 135,14,27,27);
                    }
                }
            }
        }
    }
    public void healthGUI() {
        if(ecPlayer.nHealth >=1) {
            fixedBatch.draw(txHeart, (float)690.5, nHealthGUIPosy, txHeart.getWidth() + 5, txHeart.getHeight()+5);
            if(ecPlayer.nHealth >= 2) {
                fixedBatch.draw(txHeart, (float)690.5,  (float) (nHealthGUIPosy + 38.5), txHeart.getWidth() +5 , txHeart.getHeight() +5);
                if(ecPlayer.nHealth >= 3) {
                    fixedBatch.draw(txHeart, (float)690.5, nHealthGUIPosy + 77, txHeart.getWidth()+5, txHeart.getHeight()+5);
                    if(ecPlayer.nHealth >=4) {
                        fixedBatch.draw(txHeart, (float)690.5   , (float) (nHealthGUIPosy + 115.5), txHeart.getWidth()+5, txHeart.getHeight()+5);
                    }
                }
            }
        }
    }

    public void Watergun() {
        vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        batch.begin();
        sprWatergun.draw(batch);
        batch.end();
        if (!isShowing) {
            fAngle = MathUtils.radiansToDegrees * MathUtils.atan2(vMousePosition.y - sprWatergun.getY(), vMousePosition.x - sprWatergun.getX());
            if (fAngle < 0) {
                fAngle += 360;
            }
            sprWatergun.setPosition(ecPlayer.body.getPosition().x * PPM - 6, ecPlayer.body.getPosition().y * PPM - 6);
            if (fAngle > 90 && fAngle < 270) {
                sprWatergun.setFlip(true, true);

            } else {
                sprWatergun.setFlip(true, false);
            }
            sprWatergun.setRotation(fAngle);
        }
    }

    public void playerDeath() {
        if(ecPlayer.nHealth == 0) {
            isPlayerDead = true;
        }
        if(isPlayerDead && alEnemy.size() == 0 && alBullet.size() == 0) {
            ecPlayer.nHealth = 4;
            ecPlayer.body.setTransform(Gdx.graphics.getWidth()/2/PPM, Gdx.graphics.getHeight()/2/PPM, 0);
            nEnemies = 0;
            nMaxEnemies = 3;
            nWaveCount = 1;
            game.updateScreen(0);
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
        ecPlayer.cleanup();
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
        txHeart.dispose();
        txBullet.dispose();
        txGUI.dispose();
        txSky.dispose();
        txWatergun.dispose();
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
            if (nBulletCount > 0) {
//moved mouse position vector to draw cuz we need it for other things
                vbulletPosition = new Vector2(ecPlayer.body.getPosition().x * 32, ecPlayer.body.getPosition().y * 32);
                vDir = vMousePosition.sub(vbulletPosition);
                alBullet.add(new EntityCreation(world, "Bullet", vbulletPosition.x, vbulletPosition.y, fW, fH, batch, 9.2f, 0, 0,
                        0, 4, 6, "bulletTexture.png", false, true,
                        Constants.BIT_BULLET, (short) (Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                        vDir, 0));
                nBulletCount--;
            }
        }
        System.out.println(nBulletCount);
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