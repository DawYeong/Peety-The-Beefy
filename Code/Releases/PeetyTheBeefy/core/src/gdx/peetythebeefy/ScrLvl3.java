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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import gdx.peetythebeefy.cookiecutters.*;

import java.util.ArrayList;

import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;
import static gdx.peetythebeefy.cookiecutters.Constants.isPlayerDead;


public class ScrLvl3 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    Buttons backButton;
    ScrLvl1 scrLvl1;
    SpriteBatch batch;
    OrthographicCamera camera;
    ShapeRenderer SR;
    BitmapFont font;
    FreeTypeFontParameter parameter;
    FreeTypeFontGenerator generator;
    World world;
    EntityCreation ecPlayer;
    float fX, fY, fW, fH, fminWidth, fMaxWidth, fSectionAdd = 0;
    Box2DDebugRenderer b2dr;
    OrthogonalTiledMapRenderer otmr;
    TiledMap tMapLvl3;
    TiledPolyLines tplLvl3;
    Vector2 v2Target, vMousePosition;
    int nLevelWidth, nLevelHeight, nCameraType, nSection;
    boolean isLocked;
    Texture txBack1, txBack2;
    boolean bCameraTransition, bWallCreated;
    PlayerGUI pGUI;
    InvisibleWalls iwSection1, iwSection2;
    Text tLvl3;
    ArrayList<EntityCreation> alBullet = new ArrayList<EntityCreation>();


    public ScrLvl3(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = game.camera;
        this.SR = game.SR;
        this.font = game.font;
        this.parameter = game.parameter;
        scrLvl1 = new ScrLvl1(game);

        txBack1 = assetManager.get("sky.png");
        txBack2 = assetManager.get("skyDay.png");

        bCameraTransition = false;
        world = new World(new Vector2(0f, -18f), false);
        world.setContactListener(new ContactListener1());
        generator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));

        b2dr = new Box2DDebugRenderer();

        tMapLvl3 = new TmxMapLoader().load("PeetytheBeefy3.tmx");
        tplLvl3 = new TiledPolyLines(world, tMapLvl3.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL,
                (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0);
        otmr = new OrthogonalTiledMapRenderer(tMapLvl3);

        MapProperties props = tMapLvl3.getProperties();
        nLevelWidth = props.get("width", Integer.class);
        nLevelHeight = props.get("height", Integer.class);

        fX = (float) nLevelWidth * PPM / 2;
        fY = (float) nLevelHeight * PPM / 2;
        fW = 32;
        fH = 32;
        fminWidth = Constants.SCREENWIDTH * (float) 0.4;
        fMaxWidth = Constants.SCREENWIDTH * (float) 0.6;
        nSection = 0;


        backButton = new Buttons("backButton", scrLvl1.fixedBatch, -8, 0, 96, 32);

        ecPlayer = new EntityCreation(world, "PLAYER", 50, fY, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", 1, Constants.BIT_PLAYER,
                (short) (Constants.BIT_WALL | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0, new Vector2(0, 0),
                scrLvl1.ecPlayer.fHealth, nLevelWidth, nLevelHeight);
        pGUI = new PlayerGUI(scrLvl1.fixedBatch, batch, ecPlayer.body.getPosition(), new Vector2(0, 0), font, generator, parameter);
        v2Target = new Vector2((float) nLevelWidth * PPM / 6, (float) nLevelHeight * PPM / 2);
        iwSection1 = new InvisibleWalls(world, (float) Constants.SCREENWIDTH + 32, 368, 32, 96, Constants.BIT_WALL,
                (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0);
        Constants.nBulletCount = 4;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        tLvl3 = new Text(generator, parameter, font, "Peety The Beefy Fights the Freaky Beasty", 26, Gdx.graphics.getWidth()/2,
                Gdx.graphics.getHeight()/2, scrLvl1.fixedBatch, 1, 1, "Level");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(txBack1, 0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.draw(txBack2, Constants.SCREENWIDTH, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        batch.end();

        otmr.setView(camera);
        otmr.render();

        b2dr.render(world, camera.combined.scl(PPM));

        ecPlayer.Update();

        scrLvl1.playerShoot(ecPlayer.body.getPosition(), vMousePosition, alBullet, world, nLevelWidth, nLevelHeight);

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
                        || isPlayerDead) {
                    alBullet.get(i).world.destroyBody(alBullet.get(i).body);
                    Constants.nBulletCount++;
                    alBullet.remove(i);
                }
            }
        }

        tLvl3.Update();

        sectionBlockage();
        vMousePosition = new Vector2(Gdx.input.getX() + fSectionAdd, Gdx.graphics.getHeight() - Gdx.input.getY());
        pGUI.vMousePosition = vMousePosition;
        pGUI.v2PlayerPosition = ecPlayer.body.getPosition();
        pGUI.Update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) { //button is currently being drawn behind the tiled map
            game.fMouseX = Constants.SCREENWIDTH; // just moves mouse away from button
            game.fMouseY = Constants.SCREENHEIGHT;
            Constants.isShowing = !Constants.isShowing; //its like a pop up menu, if you want to go back press p to bring up back button
        }

        //will clean up this code
        if (Constants.isShowing) {
            drawButtons();
            ecPlayer.body.setAwake(false);
            ecPlayer.isMoving = false;
        } else {
            ecPlayer.isMoving = true;
        }
        if (bCameraTransition) {
            ecPlayer.body.setAwake(false);
            ecPlayer.isMoving = false;
        } else {
            ecPlayer.isMoving = true;
        }

        if (!bCameraTransition) {
            if (ecPlayer.body.getPosition().x * PPM >= nLevelWidth * PPM / 3 && nCameraType == 0) {
                nCameraType = 1;
                bCameraTransition = true;
            } else if (ecPlayer.body.getPosition().x * PPM >= nLevelWidth * PPM / 3 * 2 && nCameraType == 1) {
                nCameraType = 2;
                bCameraTransition = true;
            }
        }
        cameraTransition();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println(ecPlayer.body.getPosition().x * PPM);
        }
    }

    private void sectionBlockage() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            if(iwSection1.body.isActive() && nSection == 0) {
                world.destroyBody(iwSection1.body);
            }
            if(bWallCreated) {
                if (iwSection2.body.isActive() && nSection == 1) {
                    world.destroyBody(iwSection2.body);
                    bWallCreated = false;
                }
            }
        }
    }

    private void cameraTransition() {
        if (bCameraTransition) {
            if (nCameraType == 1) {
                if (camera.position.x >= Constants.SCREENWIDTH * (float) 1.4) {
                    v2Target.x = (float) nLevelWidth * PPM / 2;
                    fminWidth = Constants.SCREENWIDTH * (float) 1.4;
                    fMaxWidth = Constants.SCREENWIDTH * (float) 1.6;
                    fSectionAdd = Constants.SCREENWIDTH;
                    iwSection1 = new InvisibleWalls(world, (float) Constants.SCREENWIDTH - 32, 368, 32, 96, Constants.BIT_WALL,
                            (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0);
                    iwSection2 = new InvisibleWalls(world, (float) Constants.SCREENWIDTH * 2 + 32, 368, 32, 96, Constants.BIT_WALL,
                            (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0);
                    bWallCreated = true;
                    bCameraTransition = false;
                    nSection = 1;
                }
                camera.position.x += 4;
            } else if (nCameraType == 2) {
                if (camera.position.x >= Constants.SCREENWIDTH * (float) 2.4) {
                    v2Target.x = nLevelWidth * PPM - ((float)nLevelWidth * PPM / 6);
                    fminWidth = Constants.SCREENWIDTH * (float) 2.4;
                    fMaxWidth = Constants.SCREENWIDTH * (float) 2.6;
                    fSectionAdd = Constants.SCREENWIDTH * 2;
                    iwSection1.body.setTransform((float) (Constants.SCREENWIDTH * 2 - 32) / PPM, iwSection1.body.getPosition().y, 0);
                    bCameraTransition = false;
                    nSection = 2;
                }
                camera.position.x += 4;
            }
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
        float fStartX = camera.viewportWidth / 2;
        float fStartY = camera.viewportHeight / 2;
        camera.zoom = 0.8f;
        if (!bCameraTransition) {
            CameraStyles.lerpAverageBetweenTargets(camera, v2Target, ecPlayer.body.getPosition().scl(PPM), isLocked);
            CameraStyles.boundary(camera, fStartX, fStartY, fminWidth, fMaxWidth, nLevelHeight * PPM);
        }
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
        tMapLvl3.dispose();
        otmr.dispose();
        generator.dispose();
        scrLvl1.dispose();
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
