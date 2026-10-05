package voxelgame;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
import com.jme3.texture.Texture;

public class Block {

    private Geometry geom;

    public Block(AssetManager assetManager) {
        this(assetManager, "grass-texture-top.png");
    }

    public Block(AssetManager assetManager, String textureName) {

        Box box = new Box(0.5f, 0.5f, 0.5f);
        geom = new Geometry("Block", box);

        Material material = new Material(
            assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        );

        Texture texture = assetManager.loadTexture(textureName);

        material.setTexture("DiffuseMap", texture);

        geom.setMaterial(material);
    }

    public Geometry getGeometry() {
        return geom;
    }
}