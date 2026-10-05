
package voxelgame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.Camera;
import com.jme3.math.Vector3f;

public class Player {

    private static boolean forward = false;
    private static boolean backward = false;
    private static boolean left = false;
    private static boolean right = false;

    public static void setup(Camera cam, InputManager inputManager) {

        cam.setLocation(new Vector3f(0, 0, 5));
        cam.lookAt(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 1, 0)
        );

        inputManager.addMapping(
            "Forward",
            new KeyTrigger(KeyInput.KEY_W)
        );

        inputManager.addMapping(
            "Backward",
            new KeyTrigger(KeyInput.KEY_S)
        );

        inputManager.addMapping(
            "Left",
            new KeyTrigger(KeyInput.KEY_A)
        );

        inputManager.addMapping(
            "Right",
            new KeyTrigger(KeyInput.KEY_D)
        );

        ActionListener actionListener = new ActionListener() {

            @Override
            public void onAction(
                    String name,
                    boolean isPressed,
                    float tpf) {

                if (name.equals("Forward")) {
                    forward = isPressed;
                }

                if (name.equals("Backward")) {
                    backward = isPressed;
                }

                if (name.equals("Left")) {
                    left = isPressed;
                }

                if (name.equals("Right")) {
                    right = isPressed;
                }
            }
        };

        inputManager.addListener(
            actionListener,
            "Forward",
            "Backward",
            "Left",
            "Right"
        );
    }

    public static void update(Camera cam, float tpf) {

        if (forward) {
            cam.setLocation(
                cam.getLocation().add(
                    cam.getDirection().mult(5 * tpf)
                )
            );
        }

        if (backward) {
            cam.setLocation(
                cam.getLocation().subtract(
                    cam.getDirection().mult(5 * tpf)
                )
            );
        }

        if (left) {
            cam.setLocation(
                cam.getLocation().add(
                    cam.getLeft().mult(5 * tpf)
                )
            );
        }

        if (right) {
            cam.setLocation(
                cam.getLocation().subtract(
                    cam.getLeft().mult(5 * tpf)
                )
            );
        }
    }
}
