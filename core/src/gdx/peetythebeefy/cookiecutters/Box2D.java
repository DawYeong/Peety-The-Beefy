/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
    public Box2D(float X, float Y, int Width, int Height, boolean Static, World tempWorld) {
        this.fX = X;
        this.fY = Y;
        this.fW = Width;
        this.fH = Height;
        this.isStatic = Static;
    }
    
    public void Update() {
        createBox();
    }
    
    public void createBox() {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(fX/ PPM, fY/ PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float)fW/ 2 / PPM, (float)fH / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
    }
}
