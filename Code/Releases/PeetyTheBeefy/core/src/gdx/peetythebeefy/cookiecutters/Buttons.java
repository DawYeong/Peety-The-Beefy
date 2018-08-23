/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import static gdx.peetythebeefy.PeetyTheBeefy.assetManager;

/**
 *
 * @author benny
 */

    /* This handles button creation by taking in the texturePacker file and TextureAtlas and creating a sprite
    based on the location of the specified sprite */

public class Buttons {
    
    SpriteBatch batch;
    public TextureAtlas buttonAtlas;
    public Sprite sprButton;
    public float fX, fY, fW, fH;
    public int nCount = 0;
    public Buttons(String Image, SpriteBatch batch, float X, float Y, float Width, float Height) {
        this.batch = batch;
        buttonAtlas = assetManager.get("buttons.txt", TextureAtlas.class);
        sprButton = buttonAtlas.createSprite(Image);
        this.fX = X;
        this.fY = Y;
        this.fW = Width;
        this.fH = Height;
    }
    
    public void Update() {
        createButtons();
    }
    
    public void createButtons() {
        sprButton.setPosition(fX, fY);
        sprButton.setSize(fW, fH);
        batch.begin();
        sprButton.draw(batch);
        batch.end();
    }
}
