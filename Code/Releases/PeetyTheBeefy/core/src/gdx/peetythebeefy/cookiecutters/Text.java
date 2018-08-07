package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Text {

    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout layout;
    BitmapFont font;
    SpriteBatch batch;
    String sText;
    StringBuilder sbDisplay = new StringBuilder("");
    float fX, fY, fB = 0;
    int nCount = 0, nDelay = 0;
    char[] arcText;
    GlyphLayout[] argTextLength;
    String[] sTemp;
    public boolean isFinished;
    boolean isForward = true;

    public Text(FreeTypeFontGenerator _generator, FreeTypeFontParameter _parameter, BitmapFont _font, String _sText, int _fSize, float _fX, float _fY,
                SpriteBatch _batch) {
        this.generator = _generator;
        this.parameter = _parameter;
        this.font = _font;
        this.batch = _batch;
        this.sText = _sText;
        this.fX = _fX;
        this.fY = _fY;
        arcText = sText.toCharArray();
        sTemp = new String[arcText.length];
        argTextLength = new GlyphLayout[arcText.length];
        parameter.size = _fSize;
        font = generator.generateFont(parameter);
        layout = new GlyphLayout(font, sText);
        for(int i = 0; i < arcText.length; i++) {
            sTemp[i] = "";
        }
        for(int i = 0; i < arcText.length; i++) {
            sTemp[i] += arcText[i];
            argTextLength[i] = new GlyphLayout(font, sTemp[i]);
        }
    }

    public void drawText() {
        if (nCount < arcText.length) {
            nDelay++;
            if (nDelay >= 5) {
                if (isForward) {
                    sbDisplay.append(arcText[nCount]);
                    nCount++;
                    nDelay = 0;
                } else {
                    sbDisplay.delete(0, 1);
                    fB += argTextLength[nCount].width;
                    nCount++;
                    nDelay = 0;
                }
            }
        }
        if (nCount == arcText.length) {
            isForward = false;
            nCount = 0;
        }
        if(nCount == 0 && !isForward) {
            isFinished = true;
        }
        batch.begin();
        font.draw(batch, sbDisplay, fX - (layout.width / 2) + fB, fY + 150);
        batch.end();
    }
}
