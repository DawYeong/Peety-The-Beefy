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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author benny
 */
public class Box2D {

    float fX, fY, fW, fH, PPM = 32;
    boolean isStatic;
    World world;
    Body player;
    Texture img;
    Sprite sprPlayer;
    SpriteBatch batch;

    public Box2D(float X, float Y, float Width, float Height, boolean Static, World tempWorld, SpriteBatch _batch) {
        this.fX = X;
        this.fY = Y;
        this.fW = Width;
        this.fH = Height;
        this.isStatic = Static;
        this.batch = _batch;
        this.world = tempWorld;
        img = new Texture("badlogic.jpg");
        sprPlayer = new Sprite(img);
    }

    public void Update() {
        player = createBody(fX, fY, fW, fH, isStatic);
//        batch.begin();
//        batch.draw(sprPlayer, fX, fY, 32, 32);
//        batch.end();
    }

    public Body createBody(float x, float y, float width, float height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    public void move() {
        float fhForce = 0, fvForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fhForce -= 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fhForce += 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            fvForce += 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            fvForce -= 1;
        }
        player.setLinearVelocity(fhForce * 5, fvForce * 5);
    }
}
