package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


public class MWZLatLon {

    private Double latitude;
    private Double longitude;

    public MWZLatLon() {
        super();
    }

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

    public MWZLatLon(Double latitude, Double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MWZLatLon(Map<String, Double> map) {
        super();
        this.latitude = map.get("latitude");
        this.longitude = map.get("longitude");
    }

    public MWZLatLon(Double[] arr) {
        super();
        this.latitude = arr[0];
        this.latitude = arr[1];
    }

    public static MWZLatLon getMWZLatLon(String json) {
        MWZLatLon result = null;
        try {
            result = new ObjectMapper().readValue(json, MWZLatLon.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public Double[] toArray() {
        Double[] result = new Double[2];
        result[0] = this.latitude;
        result[1] = this.longitude;

        return result;
    }

    public String toString() {
        return "Latitude="+this.latitude+" Longitude="+this.longitude;
    }



}
