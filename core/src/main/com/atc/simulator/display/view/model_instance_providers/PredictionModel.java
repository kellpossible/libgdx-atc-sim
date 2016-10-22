package com.atc.simulator.display.view.model_instance_providers;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.display.model.Display;
import com.atc.simulator.display.model.DisplayAircraft;
import com.atc.simulator.display.model.DisplayPrediction;
import com.atc.simulator.display.view.display_renderable.GDXDisplayRenderable;
import com.atc.simulator.display.view.display_renderable.HiddenDisplayRenderable;
import com.atc.simulator.display.view.shapes.TrackLineMeshBuilder;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * A prediction to be displayed in the display.
 * @author Luke Frisken
 */
public class PredictionModel extends SimpleDisplayRenderableProvider {
    private static final boolean stippledPredictions = ApplicationConfig.getBoolean("settings.display.stippled-predictions");
    private DisplayAircraft aircraft;
    private Display display;
    public static Texture texture;
    public static Pixmap pixmap;
    public static int testVar = 0;

    static {
        createTexture();
    }
    /**
     * Constructor for PredictionModel
     * @param camera that will be rendering this
     * @param aircraft associated with this prediction
     * @param display that will be rendering this
     */
    public PredictionModel(Camera camera, DisplayAircraft aircraft, Display display) {
        super(camera);
        this.aircraft = aircraft;
        this.display = display;
        update();
    }

    private static void createTexture()
    {
        pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);

        for (int i = 0; i < 256; i++)
        {
            for (int j = 0; j < 256; j++ )
            {
                float intensity = ((float) i)/256.0f;
                intensity = (float) Math.min(1.0, Math.pow((double) intensity, 2.0));
                pixmap.drawPixel(j, i, Color.rgba8888(0, intensity*0.9f, 0, 1.0f));
            }
        }

//        PredictionModel.pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
//        int x = 0;
//        for (int i = 0; i < 256; i++)
//        {
//            int y = 0;
//            for (int j = 0; j < 256; j++ )
//            {
//                float intensity = 0;
//                if ((x%2) == (y%2)) {
//                    intensity = 1.0f;
//                } else {
//                    intensity = 0.7f;
//                }
//                pixmap.drawPixel(j, i, Color.rgba8888(intensity, intensity, intensity, intensity));
//                if (j%16 == 0) {
//                    y++;
//                }
//            }
//            if (i%16 == 0) {
//                x++;
//            }
//        }

        PredictionModel.texture = new Texture(pixmap);
        PredictionModel.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        switch(display.getPredictionDisplayMethod())
        {
            case WIREFRAME:
                updateWireframes();
                break;
            case GRADIENT:
                updateGradient();
                break;
            case TWO_LINE:
                updateTwoLine();
                break;
            case NONE:
                updateHidden();
                break;
            default:
                updateHidden();
                break;
        }
    }

    public void updateHidden()
    {
        super.update();
        setDisplayRenderable(new HiddenDisplayRenderable());
        return;
    }

    public void updateTwoLine()
    {
        super.update();

        DisplayPrediction prediction = aircraft.getPrediction();

        if (prediction == null)
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }


        DisplayAircraft aircraft = prediction.getAircraft();

        Track centreTrack = prediction.getCentreTrack();
        Track rightTrack = prediction.getRightTrack();

        Color yellow = Color.YELLOW;
        Color darkerYellow = new Color(Color.rgba8888(0.0f, 0.8f, 0.8f, 1.0f));

        Color purple = Color.PURPLE;
        Color darkerPurple = new Color(Color.rgba8888(0.8f, 0.0f, 0.8f, 1.0f));

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "rightPredictionTrack",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());

        if (stippledPredictions)
        {
            TrackLineMeshBuilder.buildStippled(builder, centreTrack, aircraft, 0, yellow, darkerYellow);
            TrackLineMeshBuilder.buildStippled(builder, rightTrack, aircraft, 0, purple, darkerPurple);
        } else {
            builder.setColor(yellow);
            TrackLineMeshBuilder.build(builder, centreTrack, aircraft, 0);
            builder.setColor(purple);
            TrackLineMeshBuilder.build(builder, rightTrack, aircraft, 0);
        }

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));

    }

    /**
     * Render the wireframe version of the prediction
     */
    public void updateGradient()
    {
        super.update();

        DisplayPrediction prediction = aircraft.getPrediction();

        if (prediction == null)
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }


        DisplayAircraft aircraft = prediction.getAircraft();
        Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
//        MeshPartBuilder builder = modelBuilder.part(
//                "rightPredictionTrack",
//                GL20.GL_LINES,
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
//                new Material());
//        builder.setColor(Color.YELLOW);

        Track leftTrack = prediction.getLeftTrack();
        Track centreTrack = prediction.getCentreTrack();
        Track rightTrack = prediction.getRightTrack();

        //start of prediction line is the current aircraft position.
//        Vector3 previousPositionDrawVector = aircraft.getPosition().getModelDrawVector();
//        for(int i = 0; i < rightTrack.size(); i++)
//        {
//            AircraftState state = rightTrack.get(i);
//            Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
//            builder.line(previousPositionDrawVector, positionDrawVector);
//            previousPositionDrawVector = positionDrawVector;
//        }

        MeshPartBuilder builder = modelBuilder.part(
                "predictionArea",
                GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(texture)));


        //for how to do textured version:
        //http://stackoverflow.com/questions/21161456/building-a-box-with-texture-on-one-side-in-libgdx-performance

        builder.setColor(Color.YELLOW);
        Vector3 prevLeftPosition = null;
        Vector3 prevRightPosition = null;
        Vector3 prevCentrePosition = null;

        Vector3 normal = aircraft.getPosition().getModelDrawVector().scl(-1).nor();

        for(int i = 0; i < prediction.size(); i++)
        {
            Vector3 leftPosition = leftTrack.get(i).getPosition().getModelDrawVector(0.1);
            Vector3 centrePosition = centreTrack.get(i).getPosition().getModelDrawVector(0.1);
            Vector3 rightPosition = rightTrack.get(i).getPosition().getModelDrawVector(0.1);

            float intensity = 1.0f - (float) Math.pow(((float) i)/((float) prediction.size()), 4.0);

            if(i > 0)
            {
                if (prediction.getPredictionState() == Prediction.State.RIGHT_TURN)
                {
                    builder.setUVRange(0, intensity, intensity, 0);
                    builder.rect(leftPosition, prevLeftPosition, prevCentrePosition, centrePosition, normal);
                    builder.setUVRange(0, 0, intensity, intensity);
                    builder.rect(centrePosition, prevCentrePosition, prevRightPosition, rightPosition, normal);
                }
                if (prediction.getPredictionState() == Prediction.State.LEFT_TURN)
                {
                    builder.setUVRange(0, 0, intensity, intensity);
                    builder.rect(centrePosition, prevCentrePosition , prevLeftPosition, leftPosition, normal);
                    builder.setUVRange(0, intensity, intensity, 0);
                    builder.rect(rightPosition, prevRightPosition, prevCentrePosition, centrePosition, normal);
                }
            }

            prevLeftPosition = leftPosition;
            prevCentrePosition = centrePosition;
            prevRightPosition = rightPosition;

        }

//        debug texture example
//        double size = 10000.0;
//        GeographicCoordinate pos = aircraft.getPosition();
//        pythagoras.d.Vector3 posCart = pos.getCartesian();
//        pythagoras.d.Vector3 thetaCart = pos.thetaCartesianUnitVector().mult(size);
//        pythagoras.d.Vector3 phiCart = pos.phiCartesianUnitVector().mult(size);
//
//        GeographicCoordinate corner0 = pos;
//        GeographicCoordinate corner1 = GeographicCoordinate.fromCartesian(posCart.add(thetaCart));
//        GeographicCoordinate corner2 = GeographicCoordinate.fromCartesian(posCart.add(thetaCart.add(phiCart)));
//        GeographicCoordinate corner3 = GeographicCoordinate.fromCartesian(posCart.add(phiCart));
//
//        builder.setUVRange(0, 1, 1, 0);
//        builder.rect(
//                corner0.getModelDrawVector(),
//                corner1.getModelDrawVector(),
//                corner2.getModelDrawVector(),
//                corner3.getModelDrawVector(),
//                normal);


        //wireframe debug
//        MeshPartBuilder meshBuilder = modelBuilder.part(
//                "predictionArea",
//                GL20.GL_LINES,
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
//                new Material());


        //for how to do textured version:
        //http://stackoverflow.com/questions/21161456/building-a-box-with-texture-on-one-side-in-libgdx-performance

//        meshBuilder.setColor(Color.BLACK);
//        prevLeftPosition = null;
//        prevRightPosition = null;
//        prevCentrePosition = null;
//
//        for(int i = 0; i < prediction.size(); i++)
//        {
//            Vector3 leftPosition = leftTrack.get(i).getPosition().getModelDrawVector();
//            Vector3 centrePosition = centreTrack.get(i).getPosition().getModelDrawVector();
//            Vector3 rightPosition = rightTrack.get(i).getPosition().getModelDrawVector();
//
//            if(i > 0)
//            {
//                normal = new Vector3(leftPosition).scl(-1).nor();
//                meshBuilder.triangle(leftPosition, prevLeftPosition, centrePosition);
//                meshBuilder.triangle(rightPosition, prevRightPosition, centrePosition);
//                meshBuilder.line(centrePosition, prevCentrePosition);
//            }
//
//            prevLeftPosition = leftPosition;
//            prevCentrePosition = centrePosition;
//            prevRightPosition = rightPosition;
//
//        }




        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }

    /**
     * Render the wireframe version of the prediction
     */
    public void updateWireframes()
    {
        super.update();

        DisplayPrediction prediction = aircraft.getPrediction();

        if (prediction == null)
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }


        DisplayAircraft aircraft = prediction.getAircraft();
        Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "rightPredictionTrack",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.YELLOW);

        Track leftTrack = prediction.getLeftTrack();
        Track centreTrack = prediction.getCentreTrack();
        Track rightTrack = prediction.getRightTrack();

        //start of prediction line is the current aircraft position.
        Vector3 previousPositionDrawVector = aircraft.getPosition().getModelDrawVector();
        for(int i = 0; i < rightTrack.size(); i++)
        {
            AircraftState state = rightTrack.get(i);
            Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
            builder.line(previousPositionDrawVector, positionDrawVector);
            previousPositionDrawVector = positionDrawVector;
        }

        builder = modelBuilder.part(
                "predictionArea",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());


        //for how to do textured version:
        //http://stackoverflow.com/questions/21161456/building-a-box-with-texture-on-one-side-in-libgdx-performance

        builder.setColor(Color.YELLOW);
        Vector3 prevLeftPosition = null;
        Vector3 prevRightPosition = null;
        Vector3 prevCentrePosition = null;

        for(int i = 0; i < prediction.size(); i++)
        {
            Vector3 leftPosition = leftTrack.get(i).getPosition().getModelDrawVector();
            Vector3 centrePosition = centreTrack.get(i).getPosition().getModelDrawVector();
            Vector3 rightPosition = rightTrack.get(i).getPosition().getModelDrawVector();

            if(i > 0)
            {
                Vector3 normal = new Vector3(leftPosition).scl(-1).nor();
                builder.triangle(leftPosition, prevLeftPosition, centrePosition);
                builder.triangle(rightPosition, prevRightPosition, centrePosition);
            }

            prevLeftPosition = leftPosition;
            prevCentrePosition = centrePosition;
            prevRightPosition = rightPosition;

        }

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }

    @Override
    public void dispose()
    {
        super.dispose();

//        if (texture != null)
//        {
//            texture.dispose();
//            texture = null;
//        }
//
//        if (pixmap != null)
//        {
//            pixmap.dispose();
//            pixmap = null;
//        }
    }
}


