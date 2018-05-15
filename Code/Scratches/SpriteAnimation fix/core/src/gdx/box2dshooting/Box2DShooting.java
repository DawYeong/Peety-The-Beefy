package gdx.box2dshooting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.box2dshooting.Box2D;


public class Box2DShooting extends ApplicationAdapter {

    SpriteBatch batch;
    Texture img;
    OrthographicCamera camera;
    private World world;
    private Box2D b2bPlayer, b2bEnemy;
    private Box2DDebugRenderer b2dr;
    OrthogonalTiledMapRenderer otmr;
    TiledMap tMap;
    SpriteAnimation aniPeety, aniMeaty;

    int nJumpInterval, nJumpCount;
    boolean isCounterStart;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        world = new World(new Vector2(0f, -18f), false);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);
        b2dr = new Box2DDebugRenderer();
        tMap = new TmxMapLoader().load("PeetytheBeefy1.tmx");
        otmr = new OrthogonalTiledMapRenderer(tMap);

        b2bPlayer = new Box2D(world, "PLAYER", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 32, 32);
        b2bEnemy = new Box2D(world, "ENEMY1", Gdx.graphics.getWidth()/2 +100, Gdx.graphics.getHeight()/2 +50, 32, 32);
        aniPeety = new SpriteAnimation(9.2f, 0, 0, 0, 4, 6, "PTBsprite.png", b2bPlayer, batch, false);
        aniMeaty = new SpriteAnimation(9.2f,0,0,0,4,1,"MTMsprite.png", b2bEnemy, batch, true);

        TiledPolyLines.parseTiledObjectLayer(world, tMap.getLayers().get("collision-layer").getObjects());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        aniPeety.Update();
        aniMeaty.Update();

        b2bPlayer.playerMove();
        b2bEnemy.enemyMove();

        otmr.setView(camera);
        otmr.render();
        b2dr.render(world, camera.combined.scl(32));
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
