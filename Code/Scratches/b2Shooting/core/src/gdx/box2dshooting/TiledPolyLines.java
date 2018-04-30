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
    public Body tMap;

    public TiledPolyLines(World world, MapObjects objects) {
        this.parseTiledObjectLayer(world, objects);
    }
        public void parseTiledObjectLayer(World _world, MapObjects _objects) {
        for (MapObject object : _objects) {
            Shape shape;
            if (object instanceof PolylineMapObject) {
                shape = createPolyLine((PolylineMapObject) object);
            } else {
                continue;
            }
            
            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = _world.createBody(bdef);
            body.createFixture(shape,1.0f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0F;
            this.tMap = _world.createBody(bdef);
            this.tMap.createFixture(fixtureDef).setUserData(this);
            shape.dispose();
        }
    }
    private ChainShape createPolyLine(PolylineMapObject polyline) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length /2];

        for(int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i*2] / 32, vertices[i*2 + 1] / 32);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
    public void isHit() {
        System.out.println("On the ground!");
    }
}
