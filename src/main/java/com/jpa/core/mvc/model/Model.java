package com.jpa.core.mvc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A model implementation for the TS-JPA Model-View-Controller concept.
 * 
 * @author Tomás Sánchez
 * @since 1.0
 */
public class Model {

    private Map<String, Object> model = new HashMap<>();

    public Model() {}

    /**
     * Creates a new instance of a model from a Properties file object.
     * 
     * @param properties the properties file object
     */
    public Model(Properties properties) {
        model = properties.entrySet().stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()),
                        e -> String.valueOf(e.getValue()), (prev, next) -> next, HashMap::new));
    }

    /**
     * Creates a new instance of a model from a JSON.
     * 
     * @param json a JSON string.
     */
    @SuppressWarnings("unchecked")
    public Model(String json) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            model = objectMapper.readValue(json, model.getClass());
        } catch (JsonProcessingException e) {
            model = new HashMap<>();
        }
    }

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

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

    public Model join(Model other) {
        this.model.putAll(other.model);
        return this;
    }

    public Model replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
        model.replaceAll(function);
        return this;
    }

    public Map<String, Object> getData() {
        Map<String, Object> finalModel = new HashMap<String, Object>();
        finalModel.putAll(model);
        finalModel.replaceAll((k, v) -> {
            try {
                return v.getClass().equals(Model.class) ? ((Model) v).getData() : v;
            } catch (NullPointerException e) {
                return v;
            }
        });
        return finalModel;
    }
}
