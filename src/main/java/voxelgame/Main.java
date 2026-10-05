package voxelgame;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(854, 480);
        settings.setFullscreen(false);

        Main game = new Main();
        game.setSettings(settings);
        game.start();
    }

    @Override
    public void simpleInitApp() {

        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        World world = new World(rootNode, getAssetManager());

        Player.setup(cam, inputManager);

        Lighting.setup(rootNode);
    }

    @Override
    public void simpleUpdate(float tpf) {
        Player.update(cam, tpf);
    }
}