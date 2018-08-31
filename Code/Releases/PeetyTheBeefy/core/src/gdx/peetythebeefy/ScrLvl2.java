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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.peetythebeefy.cookiecutters.*;
import java.util.ArrayList;
import static gdx.peetythebeefy.cookiecutters.Constants.PPM;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;
import static gdx.peetythebeefy.cookiecutters.Constants.isPlayerDead;

/**
 * @author tn200
 */
public class ScrLvl2 implements Screen, InputProcessor {

    PeetyTheBeefy game;
    SpriteBatch batch;
    BitmapFont font;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    Text tLvl2;
    ShapeRenderer SR;
    Buttons BackButton;
    TiledMap tMapLvl2;
    TiledPolyLines tplLvl2;
    World world;
    PlayerGUI pGUI;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer otmr;
    Box2DDebugRenderer b2dr;
    ScrLvl1 scrLvl1;
    EntityCreation ecPlayer;
    float fX, fY, fW, fH, fEX, fEY;
    int nLevelWidth, nLevelHeight, nCharacter, nDialogue = 0;
    int nSpawnRate = 0, nEnemies = 0, nMaxEnemies = 2, nWaveCount = 1, nSpawnLocation;
    Texture txBackground, txSky;
    Vector2 vMousePosition, vEnemyShootDir, vEBulletPos, vTargetPos, v2Target;
    ArrayList<EntityCreation> alEnemy = new ArrayList<EntityCreation>();
    ArrayList<EntityCreation> alEnemyBullet = new ArrayList<EntityCreation>();
    ArrayList<EntityCreation> alBullet = new ArrayList<EntityCreation>();
    ArrayList<Text> alDialogue = new ArrayList<Text>();
    boolean isDialogueStart, isDialogueDone;
    static float fTransitWidth, fTransitHeight;
    TextBox tbCharacter;

    public ScrLvl2(PeetyTheBeefy game) {
        this.game = game;
        this.batch = game.batch;
        this.camera = game.camera;
        this.SR = game.SR;
        this.font = game.font;
        scrLvl1 = new ScrLvl1(game);
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
        txBackground = assetManager.get("level2Background.png");
        txSky = assetManager.get("skyDay.png");
        otmr = new OrthogonalTiledMapRenderer(tMapLvl2);


        MapProperties props = tMapLvl2.getProperties();
        nLevelWidth = props.get("width", Integer.class);
        nLevelHeight = props.get("height", Integer.class);

        ecPlayer = new EntityCreation(world, "PLAYER", fX - 300, fY - 215, fW, fH, batch, 9.2f, 0, 0,
                0, 4, 6, "PTBsprite.png", 1, Constants.BIT_PLAYER,
                (short) (Constants.BIT_WALL | Constants.BIT_ENEMY | Constants.BIT_ENEMYBULLET), (short) 0, new Vector2(0, 0),
                scrLvl1.ecPlayer.fHealth, nLevelWidth, nLevelHeight);
        v2Target = new Vector2(nLevelWidth * PPM / 2, nLevelHeight * PPM / 2);
        createText();
        pGUI = new PlayerGUI(scrLvl1.fixedBatch, batch, ecPlayer.body.getPosition(), new Vector2(0,0), font, generator, parameter);
        tbCharacter = new TextBox(scrLvl1.fixedBatch, nCharacter, isDialogueStart, font, generator, parameter, alDialogue.get(0));
        Constants.nBulletCount = 4;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        tLvl2 = new Text(generator, parameter, font, "Peety The Beefy Meets a Dreamy Sweetie", 26, Gdx.graphics.getWidth()/2,
                Gdx.graphics.getHeight()/2, scrLvl1.fixedBatch, 1, 1, "Level");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        lvl2Reset();

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

            scrLvl1.playerShoot(ecPlayer.body.getPosition(), vMousePosition, alBullet, world, nLevelWidth, nLevelHeight);

            playerDeath();

            for (int i = 0; i < alBullet.size(); i++) {
                alBullet.get(i).Update();
                if (Constants.isShowing && isDialogueStart) {
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
            tLvl2.Update();

            changeBox();

            vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            pGUI.vMousePosition = vMousePosition;
            pGUI.v2PlayerPosition = ecPlayer.body.getPosition();
            if(!isDialogueStart) {
                pGUI.Update();
            } else {
                if(alDialogue.size() != 0) {
                    tbCharacter.Update();
                }
            }

            dialogueLogic();
        }

        //Un-comment this if you want to see the Box2D debug renderer
//        b2dr.render(world, camera.combined.scl(PPM));


        if (!Constants.isGameStart || isPlayerDead || isDialogueStart) {
            ecPlayer.body.setAwake(false);
            ecPlayer.isMoving = false;
            if(Constants.isShowing && !isDialogueStart) {
                drawButtons();
            }
        } else {
            if (Constants.isShowing) {
                drawButtons();
                ecPlayer.body.setAwake(false);
                ecPlayer.isMoving = false;
            } else {
                ecPlayer.body.setAwake(true);
                ecPlayer.isMoving = true;
                if(tLvl2.isFinished && !isDialogueStart) {
                    createEnemy();
                }
            }
        }
        screenTransition();
        transitionBlock();
    }

    private void drawButtons() {
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
            game.mGame.stop();
            game.mBackground.setLooping(true);
            game.mBackground.setVolume(0.1f);
            game.mBackground.play();
        }
    }

    private void lvl2Reset() {
        if (game.isReset) {
            ecPlayer.body.setTransform((fX - 300) /PPM, (fY - 215) / PPM, 0);
            Constants.isFadeOut[1] = true;
            fTransitHeight = Gdx.graphics.getHeight() * (float) 1.5;
            fTransitWidth = Gdx.graphics.getWidth() * (float) 1.5;
            Constants.isGameStart = false;
            nEnemies = 0;
            nMaxEnemies = 3;
            nWaveCount = 1;
            nSpawnRate = 0;
            game.isReset = false;
        }
    }

    private void changeBox() {
        if(alDialogue.size() != 0) {
            if(alDialogue.get(0).sId.contains("Peety")) {
                tbCharacter.nType = 1;
            } else if(alDialogue.get(0).sId.contains("Matty")) {
                tbCharacter.nType = 2;
            }
        }
    }

    private void dialogueLogic() {
        tbCharacter.isTransition = isDialogueStart;
        if(alDialogue.size() != 0) {
            tbCharacter.tText = alDialogue.get(0);
            if(tLvl2.isFinished && !isDialogueDone) {
                alDialogue.get(0).Update();
                isDialogueStart = true;
            }
            if(alDialogue.get(0).isFinished && tbCharacter.fOpacity2 >= 1) {
                if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    if(isDialogueStart) {
                        nDialogue++;
                    }
                    if(nDialogue == 1) {
                        isDialogueDone = true;
                        isDialogueStart = false;
                    }
                    tbCharacter.fOpacity2 = 0;
                    alDialogue.get(0).sbDisplay.delete(0, alDialogue.get(0).sbDisplay.length());
                    alDialogue.remove(0);
                }

            }
        }
    }

    private void createText(){
        alDialogue.add(new Text(generator, parameter, font, "Peety: Wow I am an utter GOD! I can't believe how great I am. " +
                "I know for a fact that I am not delusional, don't get me twisted.", 26, 30, 200, scrLvl1.fixedBatch, 2, 15, "Peety"));
    }

    private void createEnemy() {
        if(nSpawnRate > 200 && nEnemies < nMaxEnemies && nWaveCount != 3) {
            nSpawnLocation = (int) (Math.random() * 6 + 1);
            switch (nSpawnLocation) {
                case 1:
                    fEX = (float) Gdx.graphics.getWidth()/2;
                    fEY = (float) Gdx.graphics.getHeight()/2 + 81;
                    break;
                case 2:
                    fEX = (float) Gdx.graphics.getWidth()/2 - 337;
                    fEY = (float) Gdx.graphics.getHeight()/2 + 17;
                    break;
                case 3:
                    fEX = (float) Gdx.graphics.getWidth()/2 + 336;
                    fEY = (float) Gdx.graphics.getHeight()/2 + 17;
                    break;
                case 4:
                    fEX = (float) Gdx.graphics.getWidth()/2 + 303;
                    fEY = (float) Gdx.graphics.getHeight()/2 - 239;
                    break;
                case 5:
                    fEX = (float) Gdx.graphics.getWidth()/2 - 226;
                    fEY = (float) Gdx.graphics.getHeight()/2 + 273;
                    break;
                case 6:
                    fEX = (float) Gdx.graphics.getWidth()/2 + (float) 224.5;
                    fEY = (float) Gdx.graphics.getHeight()/2 + 273;
                    break;
            }
            nEnemies++;
            alEnemy.add(new EntityCreation(world, "ENEMY", fEX, fEY, fW - 10, fH, batch, 9.2f,
                    0, 0, 0, 4, 1, "MTMsprite.png", 2,
                    Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0,
                    new Vector2(0, 0), 2, nLevelWidth, nLevelHeight));
            nSpawnRate = 0;
        }
        if (alEnemy.size() == 0 && nEnemies == nMaxEnemies) {
            nWaveCount++;
            nMaxEnemies++;
            nEnemies = 0;
        }
        if(nWaveCount == 3  && (ecPlayer.body.getPosition().x * PPM > 367  && ecPlayer.body.getPosition().x * PPM < 400) &&
                (ecPlayer.body.getPosition().y * PPM > 289 && ecPlayer.body.getPosition().y * PPM < 338)) {
            game.updateScreen(5);
        }
        nSpawnRate++;
    }

    private void moveEnemy() {
        for (int i = 0; i < alEnemy.size(); i++) {
            alEnemy.get(i).Update();
            if (Constants.isShowing || !Constants.isGameStart || isDialogueStart) {
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
                                vEnemyShootDir, 0, nLevelWidth, nLevelHeight));
                        System.out.println("here");
                        alEnemy.get(i).nShootCount = 0;
                    }
                    alEnemy.get(i).nShootCount++;
                }
            }
            if (ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y + 400 / PPM &&
                    ecPlayer.body.getPosition().y >= alEnemy.get(i).body.getPosition().y ||
                    ecPlayer.body.getPosition().y > alEnemy.get(i).body.getPosition().y - 400 / PPM &&
                            ecPlayer.body.getPosition().y < alEnemy.get(i).body.getPosition().y) {
                if (ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x + 400 / PPM &&
                        ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                } else if (ecPlayer.body.getPosition().x > alEnemy.get(i).body.getPosition().x - 400 / PPM &&
                        ecPlayer.body.getPosition().x < alEnemy.get(i).body.getPosition().x) {
                    alEnemy.get(i).isInRange = true;
                }
            } else {
                alEnemy.get(i).isInRange = false;
            }
            if (alEnemy.get(i).isDeath || isPlayerDead) {
                Constants.fBeefyProgression ++;
                alEnemy.get(i).world.destroyBody(alEnemy.get(i).body);
                alEnemy.remove(i);
            }
        }
        moveEnemyBullet();
    }
    private void playerDeath() {
        if (isPlayerDead && alEnemy.size() == 0 && alBullet.size() == 0) {
            Constants.nCurrentScreen = 4;
            game.updateScreen(15);
        }
    }

    private void moveEnemyBullet() {
        for (int i = 0; i < alEnemyBullet.size(); i++) {
            alEnemyBullet.get(i).Update();
            if (alEnemyBullet.get(i).isStuck) {
                alEnemyBullet.get(i).world.destroyBody(alEnemyBullet.get(i).body);
                alEnemyBullet.remove(i);
            }
        }
    }

    private void cameraUpdate() {
        CameraStyles.lerpAverageBetweenTargets(camera, v2Target, ecPlayer.body.getPosition().scl(PPM), false);
        float fStartX = camera.viewportWidth / 2;
        float fStartY = camera.viewportHeight / 2;
        camera.zoom = 0.8f;
        CameraStyles.boundary(camera, fStartX, fStartY, Constants.SCREENWIDTH * (float) 0.4, Constants.SCREENWIDTH * (float) 0.6, nLevelHeight * PPM);
        camera.update();
    }

    private void transitionBlock() {
        SR.begin(ShapeRenderer.ShapeType.Filled);
        SR.setColor(0, 0, 0, 1);
        SR.rect(Gdx.graphics.getWidth() / 2 - (fTransitWidth / 2), Gdx.graphics.getHeight() / 2 - (fTransitHeight / 2), fTransitWidth, fTransitHeight);
        SR.end();
    }

    private void screenTransition() {
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
        scrLvl1.dispose();
        generator.dispose();
        tMapLvl2.dispose();
        otmr.dispose();
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
