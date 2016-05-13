package com.atc.simulator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Created by luke on 13/05/16.
 */
public class ExampleJson {
    public static void main(String[] arg)
    {
        //an extremely dumb config file just using a hashmap of strings.
        HashMap<String, String> config = new HashMap<String, String>();
        HashMap<String, String> deserializedConfig = new HashMap<String, String>();

        config.put("somePortForOurProgram", Integer.toString(23454));
        config.put("someOtherPortForOurProgram", Integer.toString(23454));

        //set up gson for pretty printing which is easier for humans to read.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        //serialize our config hashmap to a string ready for writing to a file or whatever
        String s = gson.toJson(config);
        System.out.println("Serialized to JSON:");
        System.out.println(s);

        //here we could read the String s in from a json config file

        //de-serialize our string
        // method from: http://stackoverflow.com/questions/2779251/how-can-i-convert-json-to-a-hashmap-using-gson
        // another example here: https://github.com/google/gson/blob/master/UserGuide.md#collections-examples

        //will need something more complicated if we decide to store numbers not as strings, or use nested
        //structures.
        Gson deserializedGson = new GsonBuilder().setPrettyPrinting().create();
        deserializedConfig = deserializedGson.fromJson(s, deserializedConfig.getClass());

        System.out.println("Result from get on deserialized json object:");
        System.out.println(Integer.parseInt(deserializedConfig.get("somePortForOurProgram")));
        System.out.println(Integer.parseInt(deserializedConfig.get("someOtherPortForOurProgram")));


    }
}
