package gdx.peetythebeefy.cookiecutters;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Camera;

public class CameraStyles {

    public static void lockOnTarget(Camera camera, Vector2 v2Target) {

        Vector3 position = camera.position;
        position.x = v2Target.x;
        position.y = v2Target.y;
        camera.position.set(position);
        camera.update();
    }

    public static void lockAverageBetweenTargets(Camera camera, Vector2 v2TargetA, Vector2 v2TargetB) {
        Vector3 position = camera.position;
        position.x = (v2TargetA.x + v2TargetB.x) /2;
        position.y = (v2TargetA.y + v2TargetB.y) /2;
        camera.position.set(position);
        camera.update();
    }
    public static void lerpAverageBetweenTargets(Camera camera, Vector2 v2TargetA, Vector2 v2TargetB) {
        float fAvgX = (v2TargetA.x + v2TargetB.x) /2;
        float fAvgY = (v2TargetA.y + v2TargetB.y) /2;
        Vector3 position = camera.position;
        position.x = camera.position.x + (fAvgX - camera.position.x) * .1f;
        position.y = camera.position.y + (fAvgY - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
    public static void boundary(Camera camera, float nStartX, float nStartY, float nWidth, float nHeight) {

        camera.position.x = MathUtils.clamp(camera.position.x, 320, 440);
        camera.position.y = MathUtils.clamp(camera.position.y, 320, 440);

//        Vector3 position = camera.position;
//        if (position.x < nStartX) {
//            position.x = nStartX;
//        }
//        if(position.y < nStartY) {
//            position.y = nStartY;
//        }
//        if(position.x > nStartX + nWidth) {
//            position.x = nStartX + nWidth;
//        }
//        if(position.y > nStartY + nHeight) {
//            position.y = nStartY + nHeight;
//        }
//        camera.position.set(position);
//        camera.update();

    }
}
