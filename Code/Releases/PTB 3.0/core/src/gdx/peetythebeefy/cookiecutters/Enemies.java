/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author benny
 */
// this class will be used in the future
public class Enemies {

    SpriteBatch batch;
    World world;
    float fX, fY, fW, fH;
    public Body eBody;
    int nDir = (int) (Math.random() + 1), nJump;
    public String sId;

    public Enemies(float _fX, float _fY, float _fW, float _fH, World _world, SpriteBatch _batch, String _sId) {
        this.sId = _sId;
        this.fX = _fX;
        this.fY = _fY;
        this.fW = _fW;
        this.fH = _fH;
        this.world = _world;
        this.batch = _batch;
    }

    public void Update() {
        eBody = createBox(fX, fY, fW, fH, false);
    }

    public Body createBox(float x, float y, float width, float height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x / Constants.PPM, y / Constants.PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) width / 2 / Constants.PPM, (float) height / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        pBody.createFixture(fixtureDef);
        pBody.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        return pBody;
    }

    public void move() {
        float fhForce = 0;
        nJump = (int) (Math.random() * 99 + 1);
        if (eBody.getLinearVelocity().x == 0) {
            if (nDir == 1) {
                nDir = 2;
            } else if (nDir == 2) {
                nDir = 1;
            }
        }
        if (nJump == 5 && eBody.getLinearVelocity().y == 0) {
            eBody.applyForceToCenter(0, 500, false);
        }
        if (nDir == 1) {
            fhForce = 1;
        } else if (nDir == 2) {
            fhForce = -1;
        }
        if (eBody.getPosition().y < 0) {
            eBody.setTransform(eBody.getPosition().x, Gdx.graphics.getHeight() / 32, 0);
        }
        eBody.setLinearVelocity(fhForce * 2, eBody.getLinearVelocity().y);
    }

    public void isHit() {
        System.out.println(sId + ":ive been hit!");
    }
}
