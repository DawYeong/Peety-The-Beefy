package gdx.contactlistener;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter {

    SpriteBatch batch;
    Texture txPlayer;
    OrthographicCamera camera;
    World world;
    Box2DDebugRenderer b2dr;
    b2dBox playerBody, obj1, obj2;
    b2dBox[] arbEnemies = new b2dBox[2];
    String sEnemyName = "ENEMY0";
    StringBuilder sbEnemyName = new StringBuilder(sEnemyName);
    public static float PPM = 32;

    @Override
    public void create() {
        batch = new SpriteBatch();
        txPlayer = new Texture("badlogic.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new ContactListener1());
        playerBody = new b2dBox(world, "PLAYER", 0, 0, 32, 32);
        for(int i = 0; i < arbEnemies.length; i++ ) {
            sbEnemyName.deleteCharAt(sbEnemyName.length() - 1);
            sbEnemyName.append(i);
            sEnemyName = sbEnemyName.toString();
            arbEnemies[i] = new b2dBox(world, sEnemyName, -50, 50, 32, 32);
            System.out.println(sEnemyName);
        }

        
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(txPlayer, playerBody.body.getPosition().x * PPM - 16, playerBody.body.getPosition().y * PPM - 16, 32, 32);
        batch.end();
        b2dr.render(world, camera.combined.scl(PPM));
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

//        Array<Body> bodies = ContacListener1.getBodiesToRemove();
//        for(int i = 0; i > bodies.size(); i++) {
//            Body b = bodies.get(i);
//            arbEnemies[i].removeValue((b2dBox) b.getUserData(), true);
//            world.destroyBody(b);
//        }
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        move();
    }

    public void cameraUpdate() {
        Vector3 v3Position = camera.position;
        v3Position.x = playerBody.body.getPosition().x * PPM;
        v3Position.y = playerBody.body.getPosition().y * PPM;
        camera.position.set(v3Position);
        camera.update();
    }

    public void move() {
        float fHForce = 0, fVForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fHForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fHForce += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            fVForce += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            fVForce -= 1;
        }
        playerBody.body.setLinearVelocity(fHForce * 5,fVForce * 5);

    }

    @Override
    public void dispose() {
        batch.dispose();
        txPlayer.dispose();
        b2dr.dispose();
        world.dispose();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
}
