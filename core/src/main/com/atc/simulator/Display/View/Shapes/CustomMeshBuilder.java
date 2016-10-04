package com.atc.simulator.Display.View.Shapes;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.*;

/**
 * Created by luke on 4/10/16.
 */
public class CustomMeshBuilder implements MeshPartBuilder {

    /**
     * @return The {@link MeshPart} currently building.
     */
    @Override
    public MeshPart getMeshPart() {
        return null;
    }

    /**
     * @return The primitive type used for building, e.g. {@link GL20#GL_TRIANGLES} or {@link GL20#GL_LINES}.
     */
    @Override
    public int getPrimitiveType() {
        return 0;
    }

    /**
     * @return The {@link VertexAttributes} available for building.
     */
    @Override
    public VertexAttributes getAttributes() {
        return null;
    }

    /**
     * Set the color used to tint the vertex color, defaults to white. Only applicable for {@link Usage#ColorPacked} or
     * {@link Usage#ColorUnpacked}.
     *
     * @param color
     */
    @Override
    public void setColor(Color color) {

    }

    /**
     * Set the color used to tint the vertex color, defaults to white. Only applicable for {@link Usage#ColorPacked} or
     * {@link Usage#ColorUnpacked}.
     *
     * @param r
     * @param g
     * @param b
     * @param a
     */
    @Override
    public void setColor(float r, float g, float b, float a) {

    }

    /**
     * Set range of texture coordinates used (default is 0,0,1,1).
     *
     * @param u1
     * @param v1
     * @param u2
     * @param v2
     */
    @Override
    public void setUVRange(float u1, float v1, float u2, float v2) {

    }

    /**
     * Set range of texture coordinates from the specified TextureRegion.
     *
     * @param r
     */
    @Override
    public void setUVRange(TextureRegion r) {

    }

    /**
     * Get the current vertex transformation matrix.
     *
     * @param out
     */
    @Override
    public Matrix4 getVertexTransform(Matrix4 out) {
        return null;
    }

    /**
     * Set the current vertex transformation matrix and enables vertex transformation.
     *
     * @param transform
     */
    @Override
    public void setVertexTransform(Matrix4 transform) {

    }

    /**
     * Indicates whether vertex transformation is enabled.
     */
    @Override
    public boolean isVertexTransformationEnabled() {
        return false;
    }

    /**
     * Sets whether vertex transformation is enabled.
     *
     * @param enabled
     */
    @Override
    public void setVertexTransformationEnabled(boolean enabled) {

    }

    /**
     * Increases the size of the backing vertices array to accommodate the specified number of additional vertices. Useful before
     * adding many vertices to avoid multiple backing array resizes.
     *
     * @param numVertices The number of vertices you are about to add
     */
    @Override
    public void ensureVertices(int numVertices) {

    }

    /**
     * Increases the size of the backing indices array to accommodate the specified number of additional indices. Useful before
     * adding many indices to avoid multiple backing array resizes.
     *
     * @param numIndices The number of indices you are about to add
     */
    @Override
    public void ensureIndices(int numIndices) {

    }

    /**
     * Increases the size of the backing vertices and indices arrays to accommodate the specified number of additional vertices and
     * indices. Useful before adding many vertices and indices to avoid multiple backing array resizes.
     *
     * @param numVertices The number of vertices you are about to add
     * @param numIndices  The number of indices you are about to add
     */
    @Override
    public void ensureCapacity(int numVertices, int numIndices) {

    }

    /**
     * Increases the size of the backing indices array to accommodate the specified number of additional triangles. Useful before
     * adding many triangles using {@link #triangle(short, short, short)} to avoid multiple backing array resizes. The actual
     * number of indices accounted for depends on the primitive type (see {@link #getPrimitiveType()}).
     *
     * @param numTriangles The number of triangles you are about to add
     */
    @Override
    public void ensureTriangleIndices(int numTriangles) {

    }

    /**
     * Increases the size of the backing indices array to accommodate the specified number of additional rectangles. Useful before
     * adding many rectangles using {@link #rect(short, short, short, short)} to avoid multiple backing array resizes.
     *
     * @param numRectangles The number of rectangles you are about to add
     */
    @Override
    public void ensureRectangleIndices(int numRectangles) {

    }

    /**
     * Add one or more vertices, returns the index of the last vertex added. The length of values must a power of the vertex size.
     *
     * @param values
     */
    @Override
    public short vertex(float... values) {
        return 0;
    }

    /**
     * Add a vertex, returns the index. Null values are allowed. Use {@link #getAttributes} to check which values are available.
     *
     * @param pos
     * @param nor
     * @param col
     * @param uv
     */
    @Override
    public short vertex(Vector3 pos, Vector3 nor, Color col, Vector2 uv) {
        return 0;
    }

    /**
     * Add a vertex, returns the index. Use {@link #getAttributes} to check which values are available.
     *
     * @param info
     */
    @Override
    public short vertex(VertexInfo info) {
        return 0;
    }

    /**
     * @return The index of the last added vertex.
     */
    @Override
    public short lastIndex() {
        return 0;
    }

    /**
     * Add an index, MeshPartBuilder expects all meshes to be indexed.
     *
     * @param value
     */
    @Override
    public void index(short value) {

    }

    /**
     * Add multiple indices, MeshPartBuilder expects all meshes to be indexed.
     *
     * @param value1
     * @param value2
     */
    @Override
    public void index(short value1, short value2) {

    }

    /**
     * Add multiple indices, MeshPartBuilder expects all meshes to be indexed.
     *
     * @param value1
     * @param value2
     * @param value3
     */
    @Override
    public void index(short value1, short value2, short value3) {

    }

    /**
     * Add multiple indices, MeshPartBuilder expects all meshes to be indexed.
     *
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     */
    @Override
    public void index(short value1, short value2, short value3, short value4) {

    }

    /**
     * Add multiple indices, MeshPartBuilder expects all meshes to be indexed.
     *
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @param value5
     * @param value6
     */
    @Override
    public void index(short value1, short value2, short value3, short value4, short value5, short value6) {

    }

    /**
     * Add multiple indices, MeshPartBuilder expects all meshes to be indexed.
     *
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @param value5
     * @param value6
     * @param value7
     * @param value8
     */
    @Override
    public void index(short value1, short value2, short value3, short value4, short value5, short value6, short value7, short value8) {

    }

    /**
     * Add a line by indices. Requires GL_LINES primitive type.
     *
     * @param index1
     * @param index2
     */
    @Override
    public void line(short index1, short index2) {

    }

    /**
     * Add a line. Requires GL_LINES primitive type.
     *
     * @param p1
     * @param p2
     */
    @Override
    public void line(VertexInfo p1, VertexInfo p2) {

    }

    /**
     * Add a line. Requires GL_LINES primitive type.
     *
     * @param p1
     * @param p2
     */
    @Override
    public void line(Vector3 p1, Vector3 p2) {

    }

    /**
     * Add a line. Requires GL_LINES primitive type.
     *
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     */
    @Override
    public void line(float x1, float y1, float z1, float x2, float y2, float z2) {

    }

    /**
     * Add a line. Requires GL_LINES primitive type.
     *
     * @param p1
     * @param c1
     * @param p2
     * @param c2
     */
    @Override
    public void line(Vector3 p1, Color c1, Vector3 p2, Color c2) {

    }

    /**
     * Add a triangle by indices. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param index1
     * @param index2
     * @param index3
     */
    @Override
    public void triangle(short index1, short index2, short index3) {

    }

    /**
     * Add a triangle. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param p1
     * @param p2
     * @param p3
     */
    @Override
    public void triangle(VertexInfo p1, VertexInfo p2, VertexInfo p3) {

    }

    /**
     * Add a triangle. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param p1
     * @param p2
     * @param p3
     */
    @Override
    public void triangle(Vector3 p1, Vector3 p2, Vector3 p3) {

    }

    /**
     * Add a triangle. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param p1
     * @param c1
     * @param p2
     * @param c2
     * @param p3
     * @param c3
     */
    @Override
    public void triangle(Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3) {

    }

    /**
     * Add a rectangle by indices. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param corner00
     * @param corner10
     * @param corner11
     * @param corner01
     */
    @Override
    public void rect(short corner00, short corner10, short corner11, short corner01) {

    }

    /**
     * Add a rectangle. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param corner00
     * @param corner10
     * @param corner11
     * @param corner01
     */
    @Override
    public void rect(VertexInfo corner00, VertexInfo corner10, VertexInfo corner11, VertexInfo corner01) {

    }

    /**
     * Add a rectangle. Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param corner00
     * @param corner10
     * @param corner11
     * @param corner01
     * @param normal
     */
    @Override
    public void rect(Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal) {

    }

    /**
     * Add a rectangle Requires GL_POINTS, GL_LINES or GL_TRIANGLES primitive type.
     *
     * @param x00
     * @param y00
     * @param z00
     * @param x10
     * @param y10
     * @param z10
     * @param x11
     * @param y11
     * @param z11
     * @param x01
     * @param y01
     * @param z01
     * @param normalX
     * @param normalY
     * @param normalZ
     */
    @Override
    public void rect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ) {

    }

    /**
     * Copies a mesh to the mesh (part) currently being build.
     *
     * @param mesh The mesh to copy, must have the same vertex attributes and must be indexed.
     */
    @Override
    public void addMesh(Mesh mesh) {

    }

    /**
     * Copies a MeshPart to the mesh (part) currently being build.
     *
     * @param meshpart The MeshPart to copy, must have the same vertex attributes, primitive type and must be indexed.
     */
    @Override
    public void addMesh(MeshPart meshpart) {

    }

    /**
     * Copies a (part of a) mesh to the mesh (part) currently being build.
     *
     * @param mesh        The mesh to (partly) copy, must have the same vertex attributes and must be indexed.
     * @param indexOffset The zero-based offset of the first index of the part of the mesh to copy.
     * @param numIndices  The number of indices of the part of the mesh to copy.
     */
    @Override
    public void addMesh(Mesh mesh, int indexOffset, int numIndices) {

    }

    /**
     * Copies a mesh to the mesh (part) currently being build. The entire vertices array is added, even if some of the vertices are
     * not indexed by the indices array. If you want to add only the vertices that are actually indexed, then use the
     * {@link #addMesh(float[], short[], int, int)} method instead.
     *
     * @param vertices The vertices to copy, must be in the same vertex layout as the mesh being build.
     * @param indices  Array containing the indices to copy, each index should be valid in the vertices array.
     */
    @Override
    public void addMesh(float[] vertices, short[] indices) {

    }

    /**
     * Copies a (part of a) mesh to the mesh (part) currently being build.
     *
     * @param vertices    The vertices to (partly) copy, must be in the same vertex layout as the mesh being build.
     * @param indices     Array containing the indices to (partly) copy, each index should be valid in the vertices array.
     * @param indexOffset The zero-based offset of the first index of the part of indices array to copy.
     * @param numIndices  The number of indices of the part of the indices array to copy.
     */
    @Override
    public void addMesh(float[] vertices, short[] indices, int indexOffset, int numIndices) {

    }

    /**
     * @param corner00
     * @param corner10
     * @param corner11
     * @param corner01
     * @param divisionsU
     * @param divisionsV
     * @deprecated use PatchShapeBuilder.build instead.
     */
    @Override
    public void patch(VertexInfo corner00, VertexInfo corner10, VertexInfo corner11, VertexInfo corner01, int divisionsU, int divisionsV) {

    }

    /**
     * @param corner00
     * @param corner10
     * @param corner11
     * @param corner01
     * @param normal
     * @param divisionsU
     * @param divisionsV
     * @deprecated use PatchShapeBuilder.build instead.
     */
    @Override
    public void patch(Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal, int divisionsU, int divisionsV) {

    }

    /**
     * @param x00
     * @param y00
     * @param z00
     * @param x10
     * @param y10
     * @param z10
     * @param x11
     * @param y11
     * @param z11
     * @param x01
     * @param y01
     * @param z01
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param divisionsU
     * @param divisionsV
     * @deprecated use PatchShapeBuilder.build instead.
     */
    @Override
    public void patch(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, int divisionsU, int divisionsV) {

    }

    /**
     * @param corner000
     * @param corner010
     * @param corner100
     * @param corner110
     * @param corner001
     * @param corner011
     * @param corner101
     * @param corner111
     * @deprecated use BoxShapeBuilder.build instead.
     */
    @Override
    public void box(VertexInfo corner000, VertexInfo corner010, VertexInfo corner100, VertexInfo corner110, VertexInfo corner001, VertexInfo corner011, VertexInfo corner101, VertexInfo corner111) {

    }

    /**
     * @param corner000
     * @param corner010
     * @param corner100
     * @param corner110
     * @param corner001
     * @param corner011
     * @param corner101
     * @param corner111
     * @deprecated use BoxShapeBuilder.build instead.
     */
    @Override
    public void box(Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {

    }

    /**
     * @param transform
     * @deprecated use BoxShapeBuilder.build instead.
     */
    @Override
    public void box(Matrix4 transform) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @deprecated use BoxShapeBuilder.build instead.
     */
    @Override
    public void box(float width, float height, float depth) {

    }

    /**
     * @param x
     * @param y
     * @param z
     * @param width
     * @param height
     * @param depth
     * @deprecated use BoxShapeBuilder.build instead.
     */
    @Override
    public void box(float x, float y, float z, float width, float height, float depth) {

    }

    /**
     * @param radius
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {

    }

    /**
     * @param radius
     * @param divisions
     * @param center
     * @param normal
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal) {

    }

    /**
     * @param radius
     * @param divisions
     * @param center
     * @param normal
     * @param tangent
     * @param binormal
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {

    }

    /**
     * @param radius
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param tangentX
     * @param tangentY
     * @param tangentZ
     * @param binormalX
     * @param binormalY
     * @param binormalZ
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {

    }

    /**
     * @param radius
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {

    }

    /**
     * @param radius
     * @param divisions
     * @param center
     * @param normal
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {

    }

    /**
     * @param radius
     * @param divisions
     * @param center
     * @param normal
     * @param tangent
     * @param binormal
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {

    }

    /**
     * @param radius
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param tangentX
     * @param tangentY
     * @param tangentZ
     * @param binormalX
     * @param binormalY
     * @param binormalZ
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param center
     * @param normal
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param center
     * @param normal
     * @param tangent
     * @param binormal
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param tangentX
     * @param tangentY
     * @param tangentZ
     * @param binormalX
     * @param binormalY
     * @param binormalZ
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param center
     * @param normal
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param center
     * @param normal
     * @param tangent
     * @param binormal
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param tangentX
     * @param tangentY
     * @param tangentZ
     * @param binormalX
     * @param binormalY
     * @param binormalZ
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param innerWidth
     * @param innerHeight
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param tangentX
     * @param tangentY
     * @param tangentZ
     * @param binormalX
     * @param binormalY
     * @param binormalZ
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param innerWidth
     * @param innerHeight
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @param angleFrom
     * @param angleTo
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param innerWidth
     * @param innerHeight
     * @param divisions
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param normalX
     * @param normalY
     * @param normalZ
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {

    }

    /**
     * @param width
     * @param height
     * @param innerWidth
     * @param innerHeight
     * @param divisions
     * @param center
     * @param normal
     * @deprecated Use EllipseShapeBuilder.build instead.
     */
    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, Vector3 center, Vector3 normal) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisions
     * @deprecated Use CylinderShapeBuilder.build instead.
     */
    @Override
    public void cylinder(float width, float height, float depth, int divisions) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisions
     * @param angleFrom
     * @param angleTo
     * @deprecated Use CylinderShapeBuilder.build instead.
     */
    @Override
    public void cylinder(float width, float height, float depth, int divisions, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisions
     * @param angleFrom
     * @param angleTo
     * @param close
     * @deprecated Use CylinderShapeBuilder.build instead.
     */
    @Override
    public void cylinder(float width, float height, float depth, int divisions, float angleFrom, float angleTo, boolean close) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisions
     * @deprecated Use ConeShapeBuilder.build instead.
     */
    @Override
    public void cone(float width, float height, float depth, int divisions) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisions
     * @param angleFrom
     * @param angleTo
     * @deprecated Use ConeShapeBuilder.build instead.
     */
    @Override
    public void cone(float width, float height, float depth, int divisions, float angleFrom, float angleTo) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisionsU
     * @param divisionsV
     * @deprecated Use SphereShapeBuilder.build instead.
     */
    @Override
    public void sphere(float width, float height, float depth, int divisionsU, int divisionsV) {

    }

    /**
     * @param transform
     * @param width
     * @param height
     * @param depth
     * @param divisionsU
     * @param divisionsV
     * @deprecated Use SphereShapeBuilder.build instead.
     */
    @Override
    public void sphere(Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV) {

    }

    /**
     * @param width
     * @param height
     * @param depth
     * @param divisionsU
     * @param divisionsV
     * @param angleUFrom
     * @param angleUTo
     * @param angleVFrom
     * @param angleVTo
     * @deprecated Use SphereShapeBuilder.build instead.
     */
    @Override
    public void sphere(float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {

    }

    /**
     * @param transform
     * @param width
     * @param height
     * @param depth
     * @param divisionsU
     * @param divisionsV
     * @param angleUFrom
     * @param angleUTo
     * @param angleVFrom
     * @param angleVTo
     * @deprecated Use SphereShapeBuilder.build instead.
     */
    @Override
    public void sphere(Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {

    }

    /**
     * @param radius
     * @param height
     * @param divisions
     * @deprecated Use CapsuleShapeBuilder.build instead.
     */
    @Override
    public void capsule(float radius, float height, int divisions) {

    }

    /**
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param capLength
     * @param stemThickness
     * @param divisions
     * @deprecated Use ArrowShapeBuilder.build instead.
     */
    @Override
    public void arrow(float x1, float y1, float z1, float x2, float y2, float z2, float capLength, float stemThickness, int divisions) {

    }
}
