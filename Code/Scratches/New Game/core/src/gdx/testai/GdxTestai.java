package gdx.testai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.ArrayList;

public class GdxTestai extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    ShapeRenderer SR;
    float fPlayX, fPlayY, fPlayRad, velocity, gravity;
    boolean isOnGround = false, isJump;


    @Override
    public void create() {
        batch = new SpriteBatch();
        SR = new ShapeRenderer();
        fPlayRad = 250;
        fPlayX = Gdx.graphics.getWidth() / 2;
        fPlayY = Gdx.graphics.getHeight() / 2 - 120;
        gravity = (float) 1;
        velocity = (float) 20;

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapes();
        onGround();
        //Gravity();
        movement();
        jump();
        System.out.println(isOnGround);
               
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void shapes() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.BLACK);
        SR.rect(fPlayX - 25, fPlayY - 25, 50, 50);
        SR.end();
        SR.begin(ShapeType.Line);
        SR.setColor(Color.WHITE);
        SR.circle(fPlayX, fPlayY, fPlayRad);
        SR.end();
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.BLUE);
        SR.rect(0, 0, Gdx.graphics.getWidth(), 100);
        SR.end();
    }
    
    public void onGround() {
        if(fPlayY <= 125) {
            isOnGround = true;
        } else if(fPlayY > 125) {
            isOnGround = false;
        }
    }

    public void Gravity() {
        if(fPlayY > 125 && isOnGround == false) {
            fPlayY -= velocity;
        }
    }

    public void movement() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fPlayX -= 10;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            fPlayX += 10;
        }
    }
    public void jump() {
        if(isOnGround == true) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isJump = true;
            }
        }
        if(isJump == true) {
            fPlayY += velocity;
            velocity -= gravity;
            if(fPlayY <= 125) {
                velocity = 20;
                isJump = false;
            }
        }
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
