package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

public class TextBox {

    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout layout;
    BitmapFont font;
    public Text tText;
    SpriteBatch fixedBatch;
    Sprite sprPeety, sprMatty, sprText;
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
        sprPeety = new Sprite(assetManager.get("TextBoxPeety.png", Texture.class));
        sprMatty = new Sprite(assetManager.get("TextBoxMatty.png", Texture.class));
        sprText = new Sprite(assetManager.get("TextBox.png", Texture.class));
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
        sprPeety.setAlpha(fOpacity);
        if (nType == 1) {
            sprPeety.draw(fixedBatch);
        } else if (nType == 2) {
            sprMatty.draw(fixedBatch);
        } else if(nType == 3) {
            sprText.draw(fixedBatch);
        }
        font.setColor(1, 1, 1, fOpacity2);
        font.draw(fixedBatch, "Press space", Gdx.graphics.getWidth() - (layout.width * (float) 1.25), 30);
        fixedBatch.end();
    }
}
