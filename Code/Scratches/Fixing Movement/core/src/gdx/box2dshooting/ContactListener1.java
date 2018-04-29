package gdx.box2dshooting;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class ContactListener1 implements ContactListener{
    private Array<Body> arBodiesToRemove;

    public ContactListener1() {
        super();
        arBodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA == null || fixB == null) return;
        if(fixA.getUserData() == null || fixB.getUserData() == null) return;

        if(isBoxContact(fixA, fixB)) {
            Box2D playerBody = (Box2D) fixB.getUserData();
           // Box2D enemyBody = (Box2D) fixB.getUserData();
            TiledPolyLines tMapLvl1 = (TiledPolyLines) fixA.getUserData();

            tMapLvl1.isHit();
            //System.out.println(playerBody.body.getPosition() + " " + tMapLvl1.tMap.getJointList());
            arBodiesToRemove.add(fixA.getBody());
        }

        //System.out.println("A collision happened!");
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        //System.out.println("A collision stopped!");

    }

    @Override
    public void preSolve(Contact contact, Manifold mnfld) {

    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {

    }
    private boolean isBoxContact(Fixture fixA, Fixture fixB) {
        return (fixB.getUserData() instanceof Box2D && fixB.getUserData() instanceof Box2D);
    }
    public Array<Body> getArBodiesToRemove() {
        return arBodiesToRemove;
    }
}
