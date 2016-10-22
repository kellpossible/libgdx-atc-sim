package com.atc.simulator.display.view.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * A shader to render prediction gradients.
 * Created by luke on 4/10/16.
 * @author Luke Frisken
 */
public class PredictionShader implements Shader {
    private ShaderProgram program;
    private Camera camera;
    private RenderContext context;
    private int u_projViewTrans;
    private int u_worldTrans;
    private int u_color;


    /**
     * Initializes the Shader, must be called before the Shader can be used. This typically compiles a {@link ShaderProgram},
     * fetches uniform locations and performs other preparations for usage of the Shader.
     */
    @Override
    public void init() {
        String vert = Gdx.files.internal("assets/shaders/prediction.vert").readString();
        String frag = Gdx.files.internal("assets/shaders/prediction.frag").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
        {
            throw new GdxRuntimeException(program.getLog());
        }

        u_projViewTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
    }

    /**
     * Compare this shader against the other, used for sorting, light weight shaders are rendered first.
     *
     * @param other
     */
    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    /**
     * Checks whether this shader is intended to render the {@link Renderable}. Use this to make sure a call to the
     * {@link #render(Renderable)} method will succeed. This is expected to be a fast, non-blocking method. Note that this method
     * will only return true if it is intended to be used. Even when it returns false the Shader might still be capable of
     * rendering, but it's not preferred to do so.
     *
     * @param instance The renderable to check against this shader.
     * @return true if this shader is intended to render the {@link Renderable}, false otherwise.
     */
    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    /**
     * Initializes the context for exclusive rendering by this shader. Use the {@link #render(Renderable)} method to render a
     * {@link Renderable}. When done rendering the {@link #end()} method must be called.
     *
     * @param camera  The camera to use when rendering
     * @param context The context to be used, which must be exclusive available for the shader until the call to the {@link #end()}
     */
    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.begin();
        program.setUniformMatrix(u_projViewTrans, camera.combined);
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }

    /**
     * Renders the {@link Renderable}, must be called between {@link #begin(Camera, RenderContext)} and {@link #end()}. The Shader
     * instance might not be able to render every type of {@link Renderable}s. Use the {@link #canRender(Renderable)} method to
     * check if the Shader is capable of rendering a specific {@link Renderable}.
     *
     * @param renderable The renderable to render, all required fields (e.g. {@link Renderable#material} and others) must be set.
     *                   The {@link Renderable#shader} field will be ignored.
     */
    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        program.setUniformf(u_color, MathUtils.random(), MathUtils.random(), MathUtils.random());
        renderable.meshPart.render(program);
    }

    /**
     * Cleanup the context so other shaders can render. Must be called when done rendering using the {@link #render(Renderable)}
     * method, which must be preceded by a call to {@link #begin(Camera, RenderContext)}. After a call to this method an call to
     * the {@link #render(Renderable)} method will fail until the {@link #begin(Camera, RenderContext)} is called.
     */
    @Override
    public void end() {
        program.end();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        program.dispose();
    }
}
