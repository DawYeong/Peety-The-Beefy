package gdx.box2dshooting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile {
    public Body body;
    Vector2 v2Pos, v2Dir,grav = new Vector2(0, (float) 0.1);;
    int nCount = 0;
    ShapeRenderer SR;
    SpriteBatch batch;
    boolean canMove = true;

    public Projectile(World world, Vector2 Position, Vector2 Direction, ShapeRenderer _SR, SpriteBatch _batch) {
        this.v2Pos = Position;
        this.v2Dir = Direction;
        this.SR = _SR;
        this.batch = _batch;
        v2Dir.setLength(10f);
        //this.createBody(world, Position.x, Position.y);
    }

    private void createBody(World world, float fX, float fY) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = false;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(fX / 32.0F, fY / 32.0F);
        //PolygonShape shape = new PolygonShape();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ 32);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0F;
        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void Update() {
        drawProjectile();
        move();
    }

    public void drawProjectile() {
        SR.begin(ShapeRenderer.ShapeType.Filled);
        SR.setColor(Color.CYAN);
        SR.ellipse(v2Pos.x, v2Pos.y, 10, 10);
        SR.end();
    }

    public void move() {
        if(canMove) {
            v2Pos.add(v2Dir);
            if(v2Pos.y >= 7) {
                v2Dir.sub(grav);
            }
        }
    }
}

