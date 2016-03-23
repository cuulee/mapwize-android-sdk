package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZVenue {

    String identifier;
    String name;
    String alias;

    public String getIdentifier() {
        return identifier;
    }

    @JsonSetter("_id")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public MWZVenue(){
        super();
    }

    public static MWZVenue getMWZVenue(String json) {
        MWZVenue result = null;
        try {
            result = new ObjectMapper().readValue(json, MWZVenue.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public String toString() {
        return "Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias;
    }
}
