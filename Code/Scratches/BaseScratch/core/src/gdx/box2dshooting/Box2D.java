package gdx.box2dshooting;

import com.badlogic.gdx.physics.box2d.*;

public class Box2D {
    public Body body;
    public String sId;

    public Box2D(World world, String sId, float fX, float fY, float fWidth, float fHeight) {
        this.sId = sId;
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
        fixtureDef.density = 1.0F;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }
}
