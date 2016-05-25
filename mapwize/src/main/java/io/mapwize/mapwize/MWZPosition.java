package io.mapwize.mapwize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZPosition {

    private Double latitude;
    private Double longitude;
    private Integer floor;
    private String venueId;
    private String placeId;
    private String placeListId;

    public MWZPosition () {
        super();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLon() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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
