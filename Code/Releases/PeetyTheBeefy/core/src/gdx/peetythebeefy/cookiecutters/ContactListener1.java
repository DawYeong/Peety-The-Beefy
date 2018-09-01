package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

public class ContactListener1 implements ContactListener {

    private Sound sHit = assetManager.get("sound/Hit.mp3", Sound.class),
            sPeetyHit = assetManager.get("sound/PeetyHit.mp3", Sound.class);
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
            if((!bulletBody.isStuck || !enemyBody.isStuck)) {
                if(bulletBody.sId.contentEquals("Bullet") && enemyBody.sId.contentEquals("ENEMY")) {
                    if(bulletBody.canDamage) {
                        sHit.play();
                        enemyBody.fHealth -= Constants.fPlayerDamage;
                    }
                    bulletBody.canDamage = false;
                } else if(enemyBody.sId.contentEquals("Bullet") && bulletBody.sId.contentEquals("ENEMY")) {
                    if(enemyBody.canDamage) {
                        sHit.play();
                        bulletBody.fHealth -= Constants.fPlayerDamage;
                    }
                    enemyBody.canDamage = false;
                }
                if(bulletBody.sId.contentEquals("EnemyBullet") && enemyBody.sId.contentEquals("PLAYER")) {
                    sPeetyHit.play();
                    Constants.nHealth--;
                    bulletBody.isStuck = true;
                } else if(enemyBody.sId.contentEquals("EnemyBullet") && bulletBody.sId.contentEquals("PLAYER")) {
                    sPeetyHit.play();
                    Constants.nHealth--;
                    enemyBody.isStuck = true;
                }
            }
        }
        if(isEnemyhitPlayer(fixA, fixB)) {
            EntityCreation playerBody = (EntityCreation) fixA.getUserData();
            EntityCreation enemyBody = (EntityCreation) fixB.getUserData();
            if(playerBody.body.getPosition().y + playerBody.body.getMass()/2 <=
                    enemyBody.body.getPosition().y + enemyBody.body.getMass()/2 && playerBody.sId.contentEquals("PLAYER") && enemyBody.sId.contentEquals("ENEMY")) {
                sPeetyHit.play();
                Constants.nHealth --;
            }
        }
        if(isPlayerhitBarrier(fixA, fixB)) {
            InvisibleWalls iwBarrier = (InvisibleWalls) fixA.getUserData();
            EntityCreation playerBody = (EntityCreation) fixB.getUserData();
            if(playerBody.sId.contains("PLAYER")) {
                iwBarrier.bPlayerInteract = true;
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

    private boolean isPlayerhitBarrier(Fixture fixA, Fixture fixB) {
        if(fixA.getUserData() instanceof InvisibleWalls && fixB.getUserData() instanceof EntityCreation) {
            return true;
        }
        return false;
    }
}
