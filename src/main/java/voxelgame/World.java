package voxelgame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import java.util.ArrayList;
import java.util.List;

public class World {

    // World size
    private static final int SIZE_X = 32;
    private static final int SIZE_Y = 6;
    private static final int SIZE_Z = 32;

    // Block types
    private static final int AIR = 0;
    private static final int GRASS = 1;
    private static final int COBBLESTONE = 2;

    // Our block data.
    // It stores what block exists at each X, Y and Z.
    private int[][][] blocks =
        new int[SIZE_X][SIZE_Y][SIZE_Z];

    private Node rootNode;
    private AssetManager assetManager;

    public World(Node rootNode, AssetManager assetManager) {

        this.rootNode = rootNode;
        this.assetManager = assetManager;

        generateWorld();

        // Build the two meshes.
        buildMesh(
            GRASS,
            "GrassMesh",
            "grass-texture-top.png"
        );

        buildMesh(
            COBBLESTONE,
            "CobblestoneMesh",
            "cobblestone.png"
        );
    }

    // Creates the 32 x 32 x 6 world.
    private void generateWorld() {

        for (int x = 0; x < SIZE_X; x++) {

            for (int z = 0; z < SIZE_Z; z++) {

                //int height = 1 + (x + z) % 6;

                // Grass is the top layer.
                blocks[x][0][z] = GRASS;

                // Five cobblestone layers underneath.
                for (int y = 1; y < SIZE_Y; y++) {
                    blocks[x][y][z] = COBBLESTONE;
                }
            }
        }
    }

    // Creates a mesh for one type of block.
    private void buildMesh(
        int blockType,
        String meshName,
        String textureName
    ) {

        List<Vector3f> positions = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (int x = 0; x < SIZE_X; x++) {

            for (int y = 0; y < SIZE_Y; y++) {

                for (int z = 0; z < SIZE_Z; z++) {

                    if (blocks[x][y][z] != blockType) {
                        continue;
                    }

                    // Array Y:
                    // 0 = grass
                    // 1 = first cobblestone layer
                    // 2 = second cobblestone layer
                    //
                    // World Y:
                    // 0 = grass
                    // -1 = first cobblestone layer
                    // -2 = second cobblestone layer
                    float worldY = -y;

                    addVisibleFaces(
                        x,
                        y,
                        z,
                        worldY,
                        positions,
                        normals,
                        texCoords,
                        indices
                    );
                }
            }
        }

        Mesh mesh = new Mesh();

        // Position data
        Vector3f[] positionArray =
            positions.toArray(new Vector3f[0]);

        mesh.setBuffer(
            VertexBuffer.Type.Position,
            3,
            BufferUtils.createFloatBuffer(positionArray)
        );

        // Normal data
        Vector3f[] normalArray =
            normals.toArray(new Vector3f[0]);

        mesh.setBuffer(
            VertexBuffer.Type.Normal,
            3,
            BufferUtils.createFloatBuffer(normalArray)
        );

        // Texture coordinates
        Vector2f[] texCoordArray =
            texCoords.toArray(new Vector2f[0]);

        mesh.setBuffer(
            VertexBuffer.Type.TexCoord,
            2,
            BufferUtils.createFloatBuffer(texCoordArray)
        );

        // Triangle indices
        int[] indexArray =
            new int[indices.size()];

        for (int i = 0; i < indices.size(); i++) {
            indexArray[i] = indices.get(i);
        }

        mesh.setBuffer(
            VertexBuffer.Type.Index,
            3,
            BufferUtils.createIntBuffer(indexArray)
        );

        mesh.updateBound();

        Geometry geometry =
            new Geometry(meshName, mesh);

        Material material = new Material(
            assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        );

        Texture texture =
            assetManager.loadTexture(textureName);

        material.setTexture(
            "DiffuseMap",
            texture
        );

        geometry.setMaterial(material);

        rootNode.attachChild(geometry);
    }

    /*
     * Check every side of the block.
     *
     * If the neighboring block is AIR,
     * we add that face.
     *
     * If another block is there,
     * we don't add the face.
     */
    private void addVisibleFaces(
        int x,
        int y,
        int z,
        float worldY,
        List<Vector3f> positions,
        List<Vector3f> normals,
        List<Vector2f> texCoords,
        List<Integer> indices
    ) {

        // Top
        if (isAir(x, y - 1, z)) {

            addFace(
                positions,
                normals,
                texCoords,
                indices,

                new Vector3f(x - 0.5f, worldY + 0.5f, z - 0.5f),
                new Vector3f(x - 0.5f, worldY + 0.5f, z + 0.5f),
                new Vector3f(x + 0.5f, worldY + 0.5f, z + 0.5f),
                new Vector3f(x + 0.5f, worldY + 0.5f, z - 0.5f),

                new Vector3f(0, 1, 0)
            );
        }

        // Bottom
        if (isAir(x, y + 1, z)) {

            addFace(
                positions,
                normals,
                texCoords,
                indices,

                new Vector3f(x - 0.5f, worldY - 0.5f, z - 0.5f),
                new Vector3f(x + 0.5f, worldY - 0.5f, z - 0.5f),
                new Vector3f(x + 0.5f, worldY - 0.5f, z + 0.5f),
                new Vector3f(x - 0.5f, worldY - 0.5f, z + 0.5f),

                new Vector3f(0, -1, 0)
            );
        }

        // Right
        if (isAir(x + 1, y, z)) {

            addFace(
                positions,
                normals,
                texCoords,
                indices,

                new Vector3f(x + 0.5f, worldY - 0.5f, z - 0.5f),
                new Vector3f(x + 0.5f, worldY + 0.5f, z - 0.5f),
                new Vector3f(x + 0.5f, worldY + 0.5f, z + 0.5f),
                new Vector3f(x + 0.5f, worldY - 0.5f, z + 0.5f),

                new Vector3f(1, 0, 0)
            );
        }

        // Left
        if (isAir(x - 1, y, z)) {

            addFace(
                positions,
                normals,
                texCoords,
                indices,

                new Vector3f(x - 0.5f, worldY - 0.5f, z + 0.5f),
                new Vector3f(x - 0.5f, worldY + 0.5f, z + 0.5f),
                new Vector3f(x - 0.5f, worldY + 0.5f, z - 0.5f),
                new Vector3f(x - 0.5f, worldY - 0.5f, z - 0.5f),

                new Vector3f(-1, 0, 0)
            );
        }

        // Front
        if (isAir(x, y, z + 1)) {

            addFace(
                positions,
                normals,
                texCoords,
                indices,

                new Vector3f(x - 0.5f, worldY - 0.5f, z + 0.5f),
                new Vector3f(x + 0.5f, worldY - 0.5f, z + 0.5f),
                new Vector3f(x + 0.5f, worldY + 0.5f, z + 0.5f),
                new Vector3f(x - 0.5f, worldY + 0.5f, z + 0.5f),

                new Vector3f(0, 0, 1)
            );
        }

        // Back
        if (isAir(x, y, z - 1)) {

            addFace(
                positions,
                normals,
                texCoords,
                indices,

                new Vector3f(x + 0.5f, worldY - 0.5f, z - 0.5f),
                new Vector3f(x - 0.5f, worldY - 0.5f, z - 0.5f),
                new Vector3f(x - 0.5f, worldY + 0.5f, z - 0.5f),
                new Vector3f(x + 0.5f, worldY + 0.5f, z - 0.5f),

                new Vector3f(0, 0, -1)
            );
        }
    }

    // Checks whether a block position is empty.
    private boolean isAir(int x, int y, int z) {

        // Outside the world counts as air.
        if (
            x < 0 || x >= SIZE_X ||
            y < 0 || y >= SIZE_Y ||
            z < 0 || z >= SIZE_Z
        ) {
            return true;
        }

        return blocks[x][y][z] == AIR;
    }

    /*
     * Adds one square to the mesh.
     *
     * A square is made from two triangles.
     */
    private void addFace(
        List<Vector3f> positions,
        List<Vector3f> normals,
        List<Vector2f> texCoords,
        List<Integer> indices,

        Vector3f a,
        Vector3f b,
        Vector3f c,
        Vector3f d,

        Vector3f normal
    ) {

        int start = positions.size();

        // Four corners
        positions.add(a);
        positions.add(b);
        positions.add(c);
        positions.add(d);

        // Normal for each corner
        normals.add(normal);
        normals.add(normal);
        normals.add(normal);
        normals.add(normal);

        // Texture coordinates
        texCoords.add(new Vector2f(0, 0));
        texCoords.add(new Vector2f(1, 0));
        texCoords.add(new Vector2f(1, 1));
        texCoords.add(new Vector2f(0, 1));

        // First triangle
        indices.add(start);
        indices.add(start + 1);
        indices.add(start + 2);

        // Second triangle
        indices.add(start);
        indices.add(start + 2);
        indices.add(start + 3);
    }
}