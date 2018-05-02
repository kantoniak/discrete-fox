package com.kantoniak.discrete_fox.communication;

import com.kantoniak.discrete_fox.gameplay.QuestionChest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class ContentObject {
    /**
     * Long name for Germany as in the Eurostat API.
     */
    static private final String GERMANYLONG = "Germany (until 1990 former territory of the FRG)";
    /**
     * More pleasant way of displaying Germany.
     */
    static private final String GERMANYSHORT = "Germany";

    /**
     * Maps country name to some value.
     */
    private HashMap<String, Double> data;

    /**
     * Setup PupulationWithNoToilet object and populate the hashmap in proper way.
     *
     * @param dimensions dimensions retrieved from Eurostat
     * @param values exact values retrieved from Eurostat
     */
    ContentObject(JSONObject dimensions, JSONObject values) {
        data = new HashMap<>();
        try {
            JSONObject indexCountry = dimensions.getJSONObject("geo").getJSONObject("category").getJSONObject("index");
            // foreach country
            Iterator<String> tempCountry = indexCountry.keys();
            while (tempCountry.hasNext()) {
                String key1 = tempCountry.next();
                int idx1 = indexCountry.getInt(key1);
                Double value = null;

                int backup = QuestionChest.LASTTIMEPERIODINT - 1;
                while (value == null && backup > -1) {
                    try {
                        value = values.getDouble(String.valueOf(idx1 * QuestionChest.LASTTIMEPERIODINT + backup));
                    } catch (Exception e) {
                        value = null;
                    }
                    backup--;
                }

                if (value != null) {
                    data.put(key1.toLowerCase(), value);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the map with values for each country
     * @return map Country -> Value
     */
    public HashMap<String, Double> getHashMap() {
        return data;
    }
}
