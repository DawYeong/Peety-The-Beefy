package gdx.box2dshooting;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gdx.box2dshooting.Constants;

public class Box2D {
    public Body body;
    public String sId;
    int nCount = 0;
    Vector2 vDir;

    public Box2D(World world, String sId, float fX, float fY, float fWidth, float fHeight, boolean IsBullet, Vector2 vDir, short cBits, short mBits, short gIndex) {
        this.sId = sId;
        this.createBody(world, fX, fY, fWidth, fHeight, IsBullet, cBits, mBits, gIndex);
        this.vDir = vDir;
        vDir.setLength(0.7f);
    }

    private void createBody(World world, float fX, float fY, float fWidth, float fHeight, boolean isBullet, short cBits, short mBits, short gIndex) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(fX / 32.0F, fY / 32.0F);
        PolygonShape shape = new PolygonShape();
        if(isBullet) {
            shape.setAsBox(fWidth/ 32.0f / 8.0f, fHeight / 32.0f / 8.0f);
        } else {
            shape.setAsBox(fWidth / 32.0F / 2.0F, fHeight / 32.0F / 2.0F);
        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0F;
        if(!isBullet) {
        fixtureDef.friction = 0.01F;
        }
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void bulletMove() {
        if(nCount < 2) {
            body.applyLinearImpulse(vDir, body.getWorldCenter(), false);
            nCount++;
        }
    }
}
