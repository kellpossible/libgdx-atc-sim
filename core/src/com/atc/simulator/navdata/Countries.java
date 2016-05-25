package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by luke on 25/05/16.
 */
public class Countries {
    private Model model = null;

    public Countries(String filePath)
    {
        try {
            readFromJsonFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void readFromJsonFile(String filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        String jsonString = new String(encoded, java.nio.charset.StandardCharsets.UTF_8);
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(jsonString).getAsJsonObject();
        JsonArray features = object.get("features").getAsJsonArray();


        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "track",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.BLUE);

        for( JsonElement featureElement : features)
        {
            JsonObject feature = featureElement.getAsJsonObject();
            JsonObject properties = feature.get("properties").getAsJsonObject();
            String countryName = properties.get("sovereignt").getAsString();

            JsonObject geometry = feature.get("geometry").getAsJsonObject();
            String geometryType = geometry.get("type").getAsString();
            if (geometryType.equals("MultiPolygon"))
            {
                JsonArray coordinates = geometry.get("coordinates").getAsJsonArray();

                for (JsonElement polygonElement : coordinates)
                {
                    JsonArray polygon = polygonElement.getAsJsonArray();
                    JsonArray polygonElements = polygon.get(0).getAsJsonArray();

                    Vector3 prevDrawVector = null;
                    for (JsonElement coordinateElement: polygonElements)
                    {
                        JsonArray coordinate = coordinateElement.getAsJsonArray();
                        double lon = Math.toRadians(coordinate.get(0).getAsDouble());
                        double lat = Math.toRadians(coordinate.get(1).getAsDouble());

                        GeographicCoordinate geoCoord = new GeographicCoordinate(0, lat, lon);
                        Vector3 drawVector = geoCoord.getModelDrawVector();

                        if (prevDrawVector != null)
                        {
                            builder.line(prevDrawVector, drawVector);
                        }

                        prevDrawVector = drawVector;

                    }

                }
            } else if (geometryType.equals("Polygon"))
            {
                JsonArray coordinates = geometry.get("coordinates").getAsJsonArray();
                JsonArray polygonElements = coordinates.get(0).getAsJsonArray();

                Vector3 prevDrawVector = null;
                for (JsonElement coordinateElement : polygonElements) {
                    JsonArray coordinate = coordinateElement.getAsJsonArray();
                    double lon = Math.toRadians(coordinate.get(0).getAsDouble());
                    double lat = Math.toRadians(coordinate.get(1).getAsDouble());

                    GeographicCoordinate geoCoord = new GeographicCoordinate(0, lat, lon);
                    Vector3 drawVector = geoCoord.getModelDrawVector();

                    if (prevDrawVector != null) {
                        builder.line(prevDrawVector, drawVector);
                    }

                    prevDrawVector = drawVector;

                }

            }

        }

        this.model = modelBuilder.end();

    }

    public Model getModel()
    {
        return this.model;
    }
}
