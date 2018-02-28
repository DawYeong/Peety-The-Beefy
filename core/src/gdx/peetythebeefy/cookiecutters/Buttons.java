/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author benny
 */
public class Buttons {
    
    SpriteBatch batch;
    Texture img;
    Sprite sprButton;
    public float fX, fY, fW, fH;
    public Buttons(String Image, SpriteBatch batch, float X, float Y, float Width, float Height) {
        this.batch = batch;
        img = new Texture(Image);
        sprButton = new Sprite(img);
        this.fX = X;
        this.fY = Y;
        this.fW = Width;
        this.fH = Height;
    }
    
    public void Update() {
        createButtons();
    }
    
    public void createButtons() {
        batch.begin();
        batch.draw(sprButton, fX, fY, fW, fH);
        batch.end();
    }
}
