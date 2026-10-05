package voxelgame;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Lighting {

    public static void setup(Node rootNode) {

        DirectionalLight sun = new DirectionalLight();

        sun.setColor(ColorRGBA.White);

        sun.setDirection(
            new Vector3f(1.0f, -1.0f, -1.0f)
                .normalizeLocal()
        );

        AmbientLight ambient = new AmbientLight();

        ambient.setColor(
            new ColorRGBA(
                1.0f,
                1.0f,
                1.0f,
                1.0f
            )
        );

        rootNode.addLight(sun);
        rootNode.addLight(ambient);
    }
}