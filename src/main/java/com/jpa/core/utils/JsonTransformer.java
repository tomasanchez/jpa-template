package com.jpa.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String render(Object model) throws Exception {

        String result = "{}";

        try {
            result = objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            result = String.format(
                    "{\"error\": \"%s - Could not convert to JSON object of type %s\" ",
                    e.getMessage(), model.getClass().getSimpleName());

        }

        return result;
    }

}
