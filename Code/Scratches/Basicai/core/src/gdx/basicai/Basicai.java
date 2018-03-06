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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Basicai extends ApplicationAdapter{

    SpriteBatch batch;
    Texture img;
    Sprite Player;
    OrthographicCamera camera;
    World world;
    Body player, platform;
    Box2DDebugRenderer b2dr;
    float PPM = 32;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        Player = new Sprite(img);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        world = new World(new Vector2(0, -9.8f), false);
        player = createBox(Gdx.graphics.getWidth() / 2, 100, 32, 32, false);
        platform = createBox(Gdx.graphics.getWidth() / 2, 0, 600, 32, true);
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(Player, player.getPosition().x*PPM - 16, player.getPosition().y*PPM -16, 32, 32);
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
        Vector3 position = camera.position;
        position.x = player.getPosition().x * PPM;
        position.y = player.getPosition().y * PPM;
        camera.position.set(0,0,0);
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
            player.applyForceToCenter(0, 300, false);
        }
        player.setLinearVelocity(fHForce * 5, player.getLinearVelocity().y);

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

        CircleShape shape = new CircleShape();
       //shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        shape.setRadius(width/2/PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        b2dr.dispose();
        world.dispose();
        
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
}
