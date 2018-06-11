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
            EntityCreation bulletBody = (EntityCreation) fixB.getUserData();
            bulletBody.isStuck = true;
            //System.out.println("Stuck");
        }
        if(isBulletHit(fixA, fixB)) {
            EntityCreation bulletBody = (EntityCreation) fixB.getUserData();
            EntityCreation enemyBody = (EntityCreation) fixA.getUserData();
            if(!bulletBody.isStuck) {
                if(bulletBody.sId == "Bullet" && enemyBody.sId == "ENEMY") {
                    enemyBody.nHealth--;
                    System.out.println("hit");
                } else if(enemyBody.sId == "Bullet" && bulletBody.sId == "ENEMY") {
                    bulletBody.nHealth--;
                    System.out.println("Boom");
                }
                if(bulletBody.sId == "EnemyBullet" && enemyBody.sId == "PLAYER") {
                    Constants.nHealth--;
                } else if(enemyBody.sId == "EnemyBullet" && bulletBody.sId == "PLAYER") {
                    Constants.nHealth--;
                }
            }
        }
        if(isEnemyhitPlayer(fixA, fixB)) {
            EntityCreation playerBody = (EntityCreation) fixA.getUserData();
            EntityCreation enemyBody = (EntityCreation) fixB.getUserData();
            if(playerBody.body.getPosition().y + playerBody.body.getMass()/2 <=
                    enemyBody.body.getPosition().y + enemyBody.body.getMass()/2 && playerBody.sId == "PLAYER") {
                Constants.nHealth --;
            }
        }
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
        if(fixA.getUserData() instanceof TiledPolyLines && fixB.getUserData() instanceof EntityCreation) {
            return true;
        }
        return false;
    }

    private boolean isBulletHit(Fixture fixA, Fixture fixB) {
        if(fixA.getUserData() instanceof EntityCreation && fixB.getUserData() instanceof EntityCreation) {
            return true;
        }
        return false;
    }

    private boolean isEnemyhitPlayer(Fixture fixA, Fixture fixB) {
        if(fixA.getUserData() instanceof EntityCreation && fixB.getUserData() instanceof EntityCreation) {
            return true;
        }
        return false;
    }
}
