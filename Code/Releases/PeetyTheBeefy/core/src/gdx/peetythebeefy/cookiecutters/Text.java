package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;

public class Text {

    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout layout;
    BitmapFont font;
    SpriteBatch batch;
    String sText;
    public StringBuilder sbDisplay = new StringBuilder("");
    float fX, fY, fOpacity = 1, fLongevity = 0;
    int nCount = 0, nDelay = 0, nType;
    char[] arcText;
    GlyphLayout[] argTextLength;
    String[] sTemp;
    public boolean isFinished, isStart = false, isDone;
    boolean isForward = true;
    public String sId;

    public Text(FreeTypeFontGenerator _generator, FreeTypeFontParameter _parameter, BitmapFont _font, String _sText, int _fSize, float _fX, float _fY,
                SpriteBatch _batch, int _nType, int _LineSpacing, String _sId) {
        this.generator = _generator;
        this.parameter = _parameter;
        this.font = _font;
        this.batch = _batch;
        this.sText = _sText;
        this.fX = _fX;
        this.fY = _fY;
        this.nType = _nType;
        this.sId = _sId;
        arcText = sText.toCharArray();
        sTemp = new String[arcText.length];
        argTextLength = new GlyphLayout[arcText.length];
        parameter.size = _fSize;
        parameter.spaceY = _LineSpacing;
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

    public void Update() {
        if(nType == 1) {
            levelText();
        } else if(nType == 2) {
            characterText();
        }
        drawText();
    }

    public void drawText() {
        batch.begin();
        font.setColor(1, 1, 1, fOpacity);
        if(nType == 1) {
            font.draw(batch, sbDisplay, fX -(layout.width/2), fY + 150);
        } else if(nType == 2) {
            font.draw(batch, sbDisplay, fX, fY, Gdx.graphics.getWidth() - fX, Align.left, true);
        }
        batch.end();
    }

    public void levelText() {
        if (!Constants.isShowing) {
            if (nCount < arcText.length) {
                nDelay++;
                if (nDelay >= 4) {
                    if (isForward) {
                        sbDisplay.append(arcText[nCount]);
                        nCount++;
                        nDelay = 0;
                    }
                }
            }
            if (!isForward) {
                if (fLongevity >= 100) {
                    fOpacity -= 0.01f;
                } else {
                    fLongevity++;
                }
            }
            if (nCount == arcText.length) {
                isForward = false;
            }
            if (fOpacity <= 0) {
                sbDisplay.delete(0, sbDisplay.length());
                isFinished = true;
            }
        }
    }

    public void characterText() {
        if (!Constants.isShowing) {
            if (nCount < arcText.length) {
                nDelay++;
                if (nDelay >= 3) {
                    if (isForward) {
                        sbDisplay.append(arcText[nCount]);
                        nCount++;
                    }
                    nDelay = 0;
                }
            }
            if (nCount == arcText.length && !isFinished) {
                isFinished = true;
            }
        }
    }
}
