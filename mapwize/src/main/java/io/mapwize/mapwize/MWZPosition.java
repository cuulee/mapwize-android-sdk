package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZPosition {

    private Double latitude;
    private Double longitude;
    private Integer floor;
    private String venueId;
    private String placeId;

    private String placeListId;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonSetter("lat")
    public void setLatitudeWithLat(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonSetter("lon")
    public void setLongitudeWithLon(Double longitude) {
        this.longitude = longitude;
    }

    @JsonSetter("lng")
    public void setLongitudeWithLng(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceListId() {
        return placeListId;
    }

    public void setPlaceListId(String placeListId) {
        this.placeListId = placeListId;
    }

    public MWZPosition () {
        super();
    }

    public static MWZPosition getMWZPosition(String json) {
        MWZPosition result = null;
        try {
            result = new ObjectMapper().readValue(json, MWZPosition.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }

    public String toString() {
        return "VenueID="+this.venueId+" PlaceID="+this.placeId+" Latitude="+this.latitude+" Longitude="+this.longitude+" Floor="+this.floor +" PlaceListId="+this.placeListId;
    }

    public String toJSONString() {
        String jsonInString = null;
        try {
            jsonInString = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }
}
