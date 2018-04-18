package gdx.contactlistener;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import gdx.contactlistener.Constants;

public class Box2D {

    public Body body;
    public String sId;

    public Box2D(World world, String sId, float fX, float fY, float fWidth, float fHeight) {
        this.sId = sId;
        createBody(world, fX, fY, fWidth, fHeight);
    }

    private void createBody(World world, float fX, float fY, float fWidth, float fHeight) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(fX / Constants.PPM, fY / Constants.PPM);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(fWidth / Constants.PPM / 2, fHeight / Constants.PPM / 2);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
        
        
    }
    public void hit() {
        System.out.println(sId + ": I've been hit!");
    }
}
