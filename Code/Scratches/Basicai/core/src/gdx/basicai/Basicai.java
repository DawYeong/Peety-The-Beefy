package gdx.basicai;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Basicai extends ApplicationAdapter{

    SpriteBatch batch;
    Texture txPlayer;
    OrthographicCamera camera;
    World world;
    Body playerBody, platformBody;
    Box2DDebugRenderer b2dr;
    float PPM = 32;

    @Override
    public void create() {
        batch = new SpriteBatch();
        txPlayer = new Texture("badlogic.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        world = new World(new Vector2(0, -9.8f), false);
        playerBody = createBox(Gdx.graphics.getWidth() / 2, 100, 32, 32, false);
        platformBody = createBox(Gdx.graphics.getWidth() / 2, 0, 600, 32, true);
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(txPlayer, playerBody.getPosition().x*PPM - 16, playerBody.getPosition().y*PPM -16, 32, 32);
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
        v3Position.x = playerBody.getPosition().x * PPM;
        v3Position.y = playerBody.getPosition().y * PPM;
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            playerBody.applyForceToCenter(0, 300, false);
        }
        playerBody.setLinearVelocity(fHForce * 5, playerBody.getLinearVelocity().y);

    }

    public Body createBox(float x, float y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
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
