package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.physics.box2d.*;

import static gdx.peetythebeefy.cookiecutters.Constants.PPM;

public class InvisibleWalls {

    public Body body;
    public boolean bPlayerInteract;

    public InvisibleWalls(World world, float fX, float fY, float fWidth, float fHeight, short cBits, short mBits, short gIndex) {
        this.createBody(world, fX, fY, fWidth, fHeight, cBits, mBits, gIndex);
    }

    private void createBody(World world, float fX, float fY, float fWidth, float fHeight, short cBits, short mBits, short gIndex) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(fX / PPM, fY / PPM);
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        shape.setAsBox(fWidth / PPM / 2, fHeight / PPM / 2);
        bdef.fixedRotation = true;
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef);
        shape.dispose();
    }
}
