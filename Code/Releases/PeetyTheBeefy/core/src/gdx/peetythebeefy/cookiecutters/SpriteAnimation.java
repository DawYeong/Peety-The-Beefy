package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAnimation {

    public Float fAniSpeed;
    public int nPos, nFrame, nDirection, nRows, nColumns;
    public String sTexture;
    public Texture txSheet;
    public Sprite sprPeety;
    public Animation araniPeety[] = new Animation[24];

    public SpriteAnimation(float _fAniSpeed, int _nPos, int _nFrame, int _nDirection, int _nRows, int _nColumns, String _sTexture) {
        this.fAniSpeed = _fAniSpeed;
        this.nPos = _nPos;
        this.nFrame = _nFrame;
        this.nPos = _nPos;
        this.sTexture = _sTexture;
        this.nRows = _nRows;
        this.nColumns = _nColumns;

        txSheet = new Texture(sTexture);
        sprPeety = new Sprite(txSheet, 0, 0, 32, 32);

        int nW, nH, nSx, nSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
        nW = txSheet.getWidth() / nRows;
        nH = txSheet.getHeight() / nColumns;
        for (int i = 0; i < nColumns; i++) {
            Sprite[] arSprPeety = new Sprite[nRows];
            for (int j = 0; j < nRows; j++) {
                nSx = j * nW;
                nSy = i * nH;
                sprPeety = new Sprite(txSheet, nSx, nSy, nW, nH);
                arSprPeety[j] = new Sprite(sprPeety);
            }
            araniPeety[i] = new Animation(fAniSpeed, arSprPeety);
        }
    }

    public void cleanup() {
        txSheet.dispose();
    }

}