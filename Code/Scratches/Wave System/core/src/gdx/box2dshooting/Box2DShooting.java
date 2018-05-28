package gdx.box2dshooting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import gdx.box2dshooting.Constants;
import java.util.ArrayList;


public class Box2DShooting extends ApplicationAdapter implements InputProcessor{

    SpriteBatch batch;
    Sprite sprPlayer;
    Texture img;
    OrthographicCamera camera;
    TiledPolyLines tMap1;
    private World world;
    private Box2D playerBody;
    private Box2DDebugRenderer b2dr;
    OrthogonalTiledMapRenderer otmr;
    TiledMap tMap;
    Texture txPlayer;

    int nJumpInterval, nJumpCount, nWaveCount = 1, nEnemies = 0, nSpawnrate = 0, nMaxEnemies = 5, nSpawnLocation;
    boolean isCounterStart;
    Vector2 vDir, vMousePosition, bulletPosition, vPlayerPosition;
    float fX, fY;

    ArrayList<Box2D> alBullet = new ArrayList<Box2D>();
    ArrayList<Box2D> alEnemy = new ArrayList<Box2D>();
    int nMax = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        txPlayer = new Texture("badlogic.jpg");
        world = new World(new Vector2(0f, -18f), false);
        world.setContactListener(new ContactListener1());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);
        b2dr = new Box2DDebugRenderer();
        tMap = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        otmr = new OrthogonalTiledMapRenderer(tMap);
        sprPlayer = new Sprite(txPlayer);
        sprPlayer.setSize(32, 32);


        playerBody = new Box2D(world, "PLAYER", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 32, 32, false, new Vector2(0,0),
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL | Constants.BIT_ENEMY), (short)0);
        //createEnemy();
        tMap1 = new TiledPolyLines(world, tMap.getLayers().get("collision-layer").getObjects(), Constants.BIT_WALL, (short) (Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short) 0);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprPlayer.draw(batch);
        batch.end();
        createEnemy();

        move();
        moveEnemy();
        for(int i = 0; i< alBullet.size(); i++) {
            alBullet.get(i).bulletMove();
            if(alBullet.get(i).canCollect) {
                if(playerBody.body.getPosition().x - (playerBody.body.getMass() / 2) <= alBullet.get(i).body.getPosition().x + (alBullet.get(i).body.getMass() *2) &&
                        playerBody.body.getPosition().x + (playerBody.body.getMass() / 2) >= alBullet.get(i).body.getPosition().x - (alBullet.get(i).body.getMass()*2) &&
                        playerBody.body.getPosition().y - (playerBody.body.getMass() /2) <= alBullet.get(i).body.getPosition().y + (alBullet.get(i).body.getMass()*2) &&
                        playerBody.body.getPosition().y + (playerBody.body.getMass() / 2) >= alBullet.get(i).body.getPosition().y - (alBullet.get(i).body.getMass()*2)) {
                    alBullet.get(i).world.destroyBody(alBullet.get(i).body);
                    nMax--;
                    alBullet.remove(i);
                }
            }
        }


        otmr.setView(camera);
        otmr.render();
        b2dr.render(world, camera.combined.scl(32));
    }

    public void createEnemy() {
        if(nSpawnrate > 200 && nEnemies < nMaxEnemies) {
            nSpawnLocation = (int) (Math.random() * 3 + 1);
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
            alEnemy.add(new Box2D(world, "ENEMY", fX, fY, 32, 32, false,
                    new Vector2(0, 0), Constants.BIT_ENEMY, (short) (Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_BULLET), (short) 0));
            System.out.println(nEnemies);
            nSpawnrate = 0;
        }
        if(alEnemy.size() == 0 && nEnemies == nMaxEnemies) {
            nWaveCount++;
            nMaxEnemies ++;
            nEnemies = 0;
            System.out.println("yes");
        }
        nSpawnrate ++;
    }

    public void moveEnemy() {
        for(int i = 0 ; i < alEnemy.size(); i++) {
            alEnemy.get(i).enemyMove();
            if(alEnemy.get(i).isDeath) {
                alEnemy.get(i).world.destroyBody(alEnemy.get(i).body);
                alEnemy.remove(i);
                System.out.println("Wave: " + nWaveCount + " Enemies Left: " + alEnemy.size());
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        txPlayer.dispose();
    }
    public void move() {
        sprPlayer.setPosition(playerBody.body.getPosition().x *32 - 16, playerBody.body.getPosition().y * 32 -16);
        float fHForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fHForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fHForce += 1;
        }
        if(playerBody.body.getLinearVelocity().y == 0) {
            nJumpCount = 2;
            nJumpInterval = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
           if(nJumpCount == 2) {
               playerBody.body.applyForceToCenter(0, 500, false);
               isCounterStart = true;
           }
           if(nJumpCount == 1) {
               if(nJumpInterval >= 20) {
                   playerBody.body.setLinearVelocity(0,0);
                   playerBody.body.applyForceToCenter(0,400, false);
                   nJumpInterval = 0;
               }
           }
           if(nJumpCount > 0) {
               nJumpCount--;
           }
        }
        if(isCounterStart) {
            nJumpInterval++;
            if(nJumpInterval >= 20) {
                isCounterStart = false;
            }
        }
        if (playerBody.body.getPosition().y < 0) {
//            player.getPosition().set(player.getPosition().x, 100);
            playerBody.body.setTransform(playerBody.body.getPosition().x, Gdx.graphics.getHeight() / 32, 0);
            //System.out.println(playerBody.getPosition());
        }
        playerBody.body.setLinearVelocity(fHForce * 5, playerBody.body.getLinearVelocity().y);

    }


    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        if(nMax < 4) {
            vMousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            bulletPosition = new Vector2(playerBody.body.getPosition().x *32, playerBody.body.getPosition().y *32);
            vDir = vMousePosition.sub(bulletPosition);
            System.out.println(vMousePosition);
            alBullet.add(new Box2D(world, "Bullet", bulletPosition.x, bulletPosition.y, 32, 32, true, vDir,
                    Constants.BIT_BULLET, (short) (Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_ENEMY), (short)0));
            nMax++;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
