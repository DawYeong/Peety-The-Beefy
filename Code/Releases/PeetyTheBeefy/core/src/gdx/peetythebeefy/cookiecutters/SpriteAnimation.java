package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimation {
    public Texture txSheet;
    public Sprite sprAni;
    public Box2D b2Body;
    public int nPos, nFrame, nDirection, nRows, nColumns;
    public float fAniSpeed;
    public Animation[] araniCharacter;
    TextureRegion trTemp;
    SpriteBatch batch;
    boolean isEnemy;

    public SpriteAnimation(float _fAniSpeed, int _nPos, int _nFrame, int _nDirection, int _nRows, int _nColumns, String _sTexture, Box2D _b2Body, SpriteBatch _batch,
                           boolean _isEnemy) {
        txSheet = new Texture(_sTexture);
        araniCharacter = new Animation[_nRows*_nColumns];
        this.b2Body = _b2Body;
        this.nFrame = _nFrame;
        this.nPos = _nPos;
        this.nDirection = _nDirection;
        this.fAniSpeed = _fAniSpeed;
        this.nRows = _nRows;
        this.nColumns = _nColumns;
        this.batch = _batch;
        this.isEnemy = _isEnemy;
//        sprAni = new Sprite(txSheet, 0, 0, 32, 32);
        //Animation aniCharacter[] = new Animation[_nColumns * _nRows];
        //this.createSprite(_nRows, _nColumns, _fAniSpeed);
        this.playerSprite(araniCharacter);
    }
    private Animation[] playerSprite(Animation araniCharacter[]){
        int nW, nH, nSx, nSy;
        nW = txSheet.getWidth() / nRows;
        nH = txSheet.getHeight() / nColumns;
        for (int i = 0; i < nColumns; i++) {
            Sprite[] arSprPeety = new Sprite[nRows];
            for (int j = 0; j < nRows; j++) {
                nSx = j * nW;
                nSy = i * nH;
                sprAni = new Sprite(txSheet, nSx, nSy, nW, nH);
                arSprPeety[j] = new Sprite(sprAni);
            }
            araniCharacter[i] = new Animation(fAniSpeed, arSprPeety);
        }
        return araniCharacter;
    }
    public void Update() {
        frameAnimation();
        drawAnimation();
    }
    public void drawAnimation() {
        trTemp = (TextureRegion) araniCharacter[nPos].getKeyFrame(nFrame, true);
        batch.begin();
        batch.draw(trTemp, b2Body.body.getPosition().x * 32 - 32 / 2, b2Body.body.getPosition().y * 32 - 32 / 2, 32, 32);
        batch.end();
    }
    public void frameAnimation() {
        if(!isEnemy) {
            if (b2Body.body.getLinearVelocity().x != 0 || b2Body.body.getLinearVelocity().y != 0) {
                nFrame++;
            }
            if (nFrame > 32) {
                nFrame = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A) && b2Body.body.getLinearVelocity().y >= 0) { //going left
                nDirection = 1;
                nPos = 1;
                fAniSpeed = 9.2f;
                araniCharacter[nPos].setFrameDuration(fAniSpeed);
                //playerSprite(aniPeety.fAniSpeed);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && b2Body.body.getLinearVelocity().y >= 0) { //going right
                nDirection = 0;
                nPos = 0;
                fAniSpeed = 9.2f;
                araniCharacter[nPos].setFrameDuration(fAniSpeed);
            }
            if (b2Body.body.getLinearVelocity().y < 0) { //falling animation + speed up
                nPos = 5;
                if (fAniSpeed >= 2.3f) {
                    araniCharacter[nPos].setFrameDuration(fAniSpeed -= 0.1f);
                }
            }
            if (b2Body.body.getLinearVelocity().x == 0 && b2Body.body.getLinearVelocity().y == 0) { //reset to last direction when stationary
                if (nDirection == 1) {
                    nPos = 1;
                } else if (nDirection == 0) {
                    nPos = 0;
                }
                fAniSpeed = 9.2f;
                araniCharacter[nPos].setFrameDuration(fAniSpeed);
            }
        } else {
            if(nFrame > 32) {
                nFrame = 0;
            }
            nFrame++;
        }
        //        if(aniMeaty2.nFrame > 32) {
//            aniMeaty2.nFrame = 0;
//        }
//        aniMeaty.nFrame++;
//        aniMeaty2.nFrame++;
    }
    public void cleanup() {
        txSheet.dispose();
        batch.dispose();
    }
}
