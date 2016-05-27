package io.mapwize.mapwize;

import java.util.Map;

public class MWZPlaceList {

    private String identifier;
    private String name;
    private String alias;
    private String venueId;
    private String[] placeIds;
    private Map<String, Object> data;

    public MWZPlaceList() {
        super();
    }

    public MWZPlaceList(String identifier, String name, String alias, String venueId, String[] placeIds) {
        super();
        this.identifier = identifier;
        this.name = name;
        this.alias = alias;
        this.venueId = venueId;
        this.placeIds = placeIds;
    }

    public String getIdentifier() {
        return identifier;
    }

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

    public String[] getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(String[] placeIds) {
        this.placeIds = placeIds;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String toString() {
        return "Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId+" #Places="+this.placeIds.length;
    }

}
