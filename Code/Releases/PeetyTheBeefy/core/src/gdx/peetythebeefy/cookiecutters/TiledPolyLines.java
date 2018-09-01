package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

//Uses the tiled map information to create the boundaries (defined in TiledMap creation)
public class TiledPolyLines {

    Body tiledMap;

    public TiledPolyLines(World _World, MapObjects _Objects, short cBits, short mBits, short gIndex) {
        this.parseTiledObjectLayer(_World, _Objects, cBits, mBits, gIndex);
    }

    private void parseTiledObjectLayer(World world, MapObjects objects, short cBits, short mBits, short gIndex) {
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
            this.tiledMap = world.createBody(bdef);
            this.tiledMap.createFixture(fixtureDef).setUserData(this);
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
}
