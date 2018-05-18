package gdx.box2dshooting;

import com.badlogic.gdx.physics.box2d.*;

public class ContactListener1 implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA == null || fixB == null) return;
        if(fixA.getUserData() == null || fixB.getUserData() == null) return;

//        if(isBoxContact(fixA, fixB)) {
//            TiledPolyLines tMap1 = (TiledPolyLines) fixA.getUserData();
//            Box2D bulletBody = (Box2D) fixB.getUserData();
//        }

//        System.out.println("A collision happened");
        if(isBulletOnWall(fixA, fixB)) {
            Box2D bulletBody = (Box2D) fixB.getUserData();
            bulletBody.isStuck = true;
            System.out.println("Stuck");
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    private boolean isBulletOnWall(Fixture fixA, Fixture fixB) {
        if(fixA.getUserData() instanceof TiledPolyLines && fixB.getUserData() instanceof Box2D) {
            return true;
        }
        return false;
    }
    private boolean isBoxContact(Fixture fixA, Fixture fixB) {
        return (fixB.getUserData() instanceof TiledPolyLines && fixB.getUserData() instanceof Box2D);
    }
}
