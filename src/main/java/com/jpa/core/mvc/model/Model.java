package com.jpa.core.mvc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import com.google.gson.Gson;

public class Model {

    private Map<String, Object> model = new HashMap<>();

    public Model() {}

    public Model(Properties properties) {
        model = properties.entrySet().stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()),
                        e -> String.valueOf(e.getValue()), (prev, next) -> next, HashMap::new));
    }

    @SuppressWarnings("unchecked")
    public Model(String json) {
        Gson gson = new Gson();
        model = gson.fromJson(json, Map.class);
    }

    /**
     * Gets the value of the property.
     * 
     * @param property the property name
     * @return the value object
     */
    public Object get(String property) {
        return model.get(property);
    }

    /**
     * Sets property in the model.
     * 
     * @param property the property name
     * @param value the object value
     * @return the model itself
     */
    public Model set(String property, Object value) {
        model.put(property, value);
        return this;
    }
}
