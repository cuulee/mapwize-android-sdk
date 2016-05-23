package io.mapwize.mapwize;

import java.util.Map;

public class MWZLatLon {

    private Double latitude;
    private Double longitude;

    public MWZLatLon() {
        super();
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

    public Double[] toArray() {
        Double[] result = new Double[2];
        result[0] = this.latitude;
        result[1] = this.longitude;
        return result;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String toString() {
        return "Latitude="+this.latitude+" Longitude="+this.longitude;
    }



}
