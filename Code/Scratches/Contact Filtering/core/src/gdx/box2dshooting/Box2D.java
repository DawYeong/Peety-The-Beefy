package gdx.box2dshooting;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gdx.box2dshooting.Constants;
import com.badlogic.gdx.Gdx;


public class Box2D {
    public Body body;
    public String sId;
    int nCount = 0, nJump, nDir = 1, nHealth = 5;
        World world;
    Vector2 vDir, vGravity;
    boolean isDeath = false, canCollect =false, isStuck;
    public FixtureDef fD = new FixtureDef();

    public Box2D(World world, String sId, float fX, float fY, float fWidth, float fHeight, boolean IsBullet, Vector2 vDir, short cBits, short mBits, short gIndex) {
        this.sId = sId;
        this.createBody(world, fX, fY, fWidth, fHeight, IsBullet, cBits, mBits, gIndex);
        this.vDir = vDir;
        this.world = world;
        vGravity = new Vector2(0,0);
        vDir.setLength(0.7f);
    }

    private void createBody(World world, float fX, float fY, float fWidth, float fHeight, boolean isBullet, short cBits, short mBits, short gIndex) {
        BodyDef bdef = new BodyDef();
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
            bdef.fixedRotation = true;
        } else {
            fixtureDef.friction = 10f;
            fixtureDef.restitution = 0.1f;
            //bdef.fixedRotation = false;
            bdef.bullet =true;
        }
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void enemyMove() {
        float fhForce = 0;
        if(body.getLinearVelocity().x == 0) {
            if(nDir == 1) {
                nDir = 2;
            } else if(nDir == 2) {
                nDir = 1;
            }
        }
        nJump = (int) (Math.random() * 99 + 1);
        if(nDir == 1) {
            fhForce = 1;
        } else if(nDir == 2) {
            fhForce = -1;
        }
        if (nJump == 5 && body.getLinearVelocity().y == 0) {
            body.applyForceToCenter(0, 500, false);
        }
        if (body.getPosition().y < 0) {
            body.setTransform(body.getPosition().x, Gdx.graphics.getHeight() / 32, 0);
        }
        body.setLinearVelocity(fhForce * 2, body.getLinearVelocity().y);
        if(nHealth <= 0) {
            isDeath = true;
            world.destroyBody(body);
        }
    }

    public void bulletMove() {
        if(nCount < 3) {
            body.applyLinearImpulse(vDir, body.getWorldCenter(), false);
        }
        if(body.getPosition().y < 0 && body.getLinearVelocity().y < 0) {
            body.setTransform(body.getPosition().x, Gdx.graphics.getHeight()/32, 0);
        } else if(body.getPosition().y > Gdx.graphics.getHeight() /32 && body.getLinearVelocity().y > 0) {
            body.setTransform(body.getPosition().x, 0, 0);
        }
        if(nCount >= 120) {
            canCollect = true;
        }
        if(isStuck) {
            body.setAwake(false);
        }
        nCount++;
    }
}
