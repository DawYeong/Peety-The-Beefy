/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author benny
 */

// haven't implemented but will use in the future
public class Enemies {

    SpriteBatch batch;
    World world;
    float fX, fY, fW, fH;
    Body eBody;

    public Enemies(float _fX, float _fY, float _fW, float _fH, World _world, SpriteBatch _batch) {
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
        def.position.set(x / 32, y / 32);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) width / 2 / 32, (float) height / 2 / 32);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }
    
    public void move() {
        int nDir = (int) Math.random() + 1, nCount = 0;
        float fhForce = 0;
        if(eBody.getLinearVelocity().x == 0) {
            if(nDir == 1) {
                nDir = 2;
            } else if(nDir == 2) {
                nDir = 1;
            }
        }
        if(nCount == 500) {
            eBody.applyForceToCenter(0, 500, false);
            nCount = 0;
        }
        if(nDir == 1) {
            fhForce = 3;
        } else if(nDir == 2) {
            fhForce = -3;
        }
        eBody.setLinearVelocity(fhForce * 5, eBody.getLinearVelocity().y);
        nCount++;
    }
}
