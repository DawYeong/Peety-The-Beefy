package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener1 implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        if(fixA == null || fixB == null) return;
        if(fixA.getUserData() == null || fixB.getUserData() == null) return;

        if(isBulletOnWall(fixA, fixB)) {
            Box2D bulletBody = (Box2D) fixB.getUserData();
            bulletBody.isStuck = true;
            //System.out.println("Stuck");
        }
        if(isBulletHitEnemy(fixA, fixB)) {
            Box2D bulletBody = (Box2D) fixB.getUserData();
            Box2D enemyBody = (Box2D) fixA.getUserData();
            if(!bulletBody.isStuck && bulletBody.sId == "Bullet") {
                enemyBody.nHealth --;
            }
        }
        //System.out.println("A collision happened");
    }

    @Override
    public void endContact(Contact contact) {
        
    }

    @Override
    public void preSolve(Contact contact, Manifold mnfld) {
        
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
        
    }
    private boolean isBulletOnWall(Fixture fixA, Fixture fixB) {
        if(fixA.getUserData() instanceof TiledPolyLines && fixB.getUserData() instanceof Box2D) {
            return true;
        }
        return false;
    }
    private boolean isBulletHitEnemy(Fixture fixA, Fixture fixB) {
        if(fixA.getUserData() instanceof  Box2D && fixB.getUserData() instanceof Box2D) {
            return true;
        }
        return false;
    }
    
}
