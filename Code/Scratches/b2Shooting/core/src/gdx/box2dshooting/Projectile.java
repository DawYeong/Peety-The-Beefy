package gdx.box2dshooting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile {
    public Body body;
    Vector2 v2Pos, v2Dir;
    int nCount = 0;

    public Projectile(World world, Vector2 Position, Vector2 Direction) {
        this.v2Pos = Position;
        this.v2Dir = Direction;
        v2Dir.setLength(0.1f);
        this.createBody(world, Position.x, Position.y);
    }

    private void createBody(World world, float fX, float fY) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = false;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(fX / 32.0F, fY / 32.0F);
        //PolygonShape shape = new PolygonShape();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ 32);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0F;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void move() {
        if (nCount < 5) {
            body.applyLinearImpulse(v2Dir, body.getWorldCenter(), false);
            nCount++;
        }
    }
}

