package com.sam_chordas.android.stockhawk.data;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zang on 2016-09-14.
 */
public class StockDeserializer implements JsonDeserializer<List<Stock>> {
    @Override
    public List<Stock> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Gson().fromJson(
            json
                .getAsJsonObject().get("query")
                .getAsJsonObject().get("result")
                .getAsJsonObject().get("quote")
                .getAsJsonArray(), typeOfT);
    }
}
