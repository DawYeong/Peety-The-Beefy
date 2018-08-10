package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class TextBox {

    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout layout;
    BitmapFont font;
    public Text tText;
    SpriteBatch fixedBatch;
    public int nType;
    public boolean isTransition;
    public float fOpacity = 0, fOpacity2 = 0;

    public TextBox(SpriteBatch _fixedBatch, int _nType, boolean _isTransition, BitmapFont _font, FreeTypeFontGenerator _generator, FreeTypeFontParameter _parameter,
                   Text _tText) {
        this.generator = _generator;
        this.parameter = _parameter;
        this.font = _font;
        this.isTransition = _isTransition;
        this.tText = _tText;
        this.nType = _nType;
        this.fixedBatch = _fixedBatch;
        parameter.size = 15;
        font = generator.generateFont(parameter);
        layout = new GlyphLayout(font, "Press space");
    }

    public void Update() {
        if (isTransition && fOpacity <= 1) {
            fOpacity += 0.05f;
        }
        if (tText.isFinished && fOpacity2 < 1) {
            fOpacity2 += 0.02f;
        }
        fixedBatch.begin();
        Constants.sprTextPeety.setAlpha(fOpacity);
        if (nType == 1) {
            Constants.sprTextPeety.draw(fixedBatch);
        } else if (nType == 2) {
            Constants.sprTextMatty.draw(fixedBatch);
        }
        font.setColor(1, 1, 1, fOpacity2);
        font.draw(fixedBatch, "Press space", Gdx.graphics.getWidth() - (layout.width * (float) 1.25), 30);
        fixedBatch.end();
    }
}
