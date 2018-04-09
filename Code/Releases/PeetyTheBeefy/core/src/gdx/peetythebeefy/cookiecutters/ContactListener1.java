package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener1 implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;
        
        System.out.println("A collision happened");
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
    
}
