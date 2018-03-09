/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 *
 * @author benny
 */
public class Buttons {
    
    SpriteBatch batch;
    TextureAtlas buttonAtlas;
    Sprite sprButton;
    public float fX, fY, fW, fH;
    public Buttons(String Image, SpriteBatch batch, float X, float Y, float Width, float Height) {
        this.batch = batch;
        buttonAtlas = new TextureAtlas("buttons.txt");
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
