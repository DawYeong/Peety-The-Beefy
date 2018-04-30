package gdx.box2dshooting;

import com.badlogic.gdx.physics.box2d.*;

public class Projectile {
    public Body body;

    public Projectile(World world, float fX, float fY, float fWidth, float fHeight) {
        this.createBody(world, fX, fY, fWidth, fHeight);
    }

    private void createBody(World world, float fX, float fY, float fWidth, float fHeight) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(fX / 32.0F, fY / 32.0F);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(fWidth / 32.0F / 2.0F, fHeight / 32.0F / 2.0F);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.01f;  //determines the "stickiness to walls
        fixtureDef.density = 1.0F;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }
}

