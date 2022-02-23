package com.jpa.core.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * A Superclass implementation of a Entity which has a stable entity identifier.
 * 
 * @author Tomás Sánchez
 * @version 2.0
 */
@MappedSuperclass
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class PersistentEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;


    /**
     * Converts an entity to JSON representation.
     * 
     * @return a JSON string.
     */
    public String toJSON() {

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "{}";

        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            jsonString = String.format("{\"error\": \"%s\"}", e.getMessage());
        }

        return jsonString;
    }

}
