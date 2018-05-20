/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.box2dshooting;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 *
 * @author benny
 */
public class TiledPolyLines {
    Body tMap;
    public TiledPolyLines(World _World, MapObjects _Objects, short cBits, short mBits, short gIndex) {
        this.parseTiledObjectLayer(_World, _Objects, cBits, mBits, gIndex);
    }

        public void parseTiledObjectLayer(World world, MapObjects objects, short cBits, short mBits, short gIndex) {
        for (MapObject object : objects) {
            Shape shape;
            if (object instanceof PolylineMapObject) {
                shape = createPolyLine((PolylineMapObject) object);
            } else {
                continue;
            }
            
            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.filter.categoryBits = cBits;
            fixtureDef.filter.maskBits = mBits;
            fixtureDef.filter.groupIndex = gIndex;
            body = world.createBody(bdef);
            body.createFixture(shape,1.0f);
            this.tMap = world.createBody(bdef);
            this.tMap.createFixture(fixtureDef).setUserData(this);
            shape.dispose();
        }
    }
    private  ChainShape createPolyLine(PolylineMapObject polyline) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length /2];
        
        for(int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i*2] / 32, vertices[i*2 + 1] / 32);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
}
