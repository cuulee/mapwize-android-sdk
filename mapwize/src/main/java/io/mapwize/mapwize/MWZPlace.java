package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZPlace {

    String identifier;
    String name;
    String alias;
    String venueId;

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

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public MWZPlace(){
        super();
    }

    public MWZPlace(HashMap<String, String> map) {
        super();
        this.identifier = map.get("_id");
        this.name = map.get("name");
        this.alias = map.get("alias");
        this.venueId = map.get("venueId");
    }

    public static MWZPlace getMWZPlace(String json) {
        MWZPlace result = null;
        try {
            result = new ObjectMapper().readValue(json, MWZPlace.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public String toString() {
        return "Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId;
    }
}
