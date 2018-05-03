/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author benny
 */
// this class will be changed in the future
public class Box2D {


    public Body body;
    public String sId;
    public int nJumpInterval, nJumpCount, nJump, nDir = 1;
    boolean isCounterStart;
    public boolean isInRange;

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
        fixtureDef.friction = 0.01f;
        fixtureDef.density = 1.0F;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void playerMove() {
        float fHForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fHForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fHForce += 1;
        }
        if (body.getLinearVelocity().y == 0) {
            nJumpCount = 2;
            nJumpInterval = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (nJumpCount == 2) {
                body.applyForceToCenter(0, 500, false);
                isCounterStart = true;
            }
            if (nJumpCount == 1) {
                if (nJumpInterval >= 20) {
                    body.setLinearVelocity(0, 0);
                    body.applyForceToCenter(0, 400, false);
                    nJumpInterval = 0;
                }
            }
            if (nJumpCount > 0) {
                nJumpCount--;
            }
        }
        if (isCounterStart) {
            nJumpInterval++;
            if (nJumpInterval >= 20) {
                isCounterStart = false;
            }
        }
        if (body.getPosition().y < 0) {
            body.setTransform(body.getPosition().x, Gdx.graphics.getHeight() / 32, 0);
        }
        body.setLinearVelocity(fHForce * 5, body.getLinearVelocity().y);

    }

    public void enemyMove() {
        float fhForce = 0;
        if (!isInRange) {
            if (body.getLinearVelocity().x == 0) {
                if (nDir == 1) {
                    nDir = 2;
                } else if (nDir == 2) {
                    nDir = 1;
                }
            }
        }
        nJump = (int) (Math.random() * 99 + 1);
        if (nDir == 1) {
            fhForce = (isInRange) ? (float)1.5 : 1;
        } else if (nDir == 2) {
            fhForce = (isInRange) ? (float) -1.5: -1;
        }
        if (nJump == 5 && body.getLinearVelocity().y == 0) {
            body.applyForceToCenter(0, 500, false);
        }
        if (body.getPosition().y < 0) {
            body.setTransform(body.getPosition().x, Gdx.graphics.getHeight() / 32, 0);
        }
        body.setLinearVelocity(fhForce * 2, body.getLinearVelocity().y);
    }

}
