package gdx.contactlistener;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import gdx.contactlistener.Box2D;
import gdx.contactlistener.ContactListener1;

public class Main extends ApplicationAdapter {

    SpriteBatch batch;
    Texture txPlayer;
    OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Box2D playerBody, obj1;
    public static float PPM = 32;

    @Override
    public void create() {
        batch = new SpriteBatch();
        txPlayer = new Texture("badlogic.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        world = new World(new Vector2(0, -9.8f), false);
        world.setContactListener(new ContactListener1());
        playerBody = new Box2D(world, "PLAYER", 0, 0, 32, 32);
        obj1 = new Box2D(world, "OBJ1", 75, 75, 600, 32);
        
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
        float fHForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fHForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fHForce += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playerBody.body.applyForceToCenter(0, 300, false);
        }
        playerBody.body.setLinearVelocity(fHForce * 5, playerBody.body.getLinearVelocity().y);

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
