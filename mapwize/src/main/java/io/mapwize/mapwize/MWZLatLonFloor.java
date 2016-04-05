package io.mapwize.mapwize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZLatLonFloor extends MWZLatLon {

    private Integer floor;
    private Integer accuracy;

    public MWZLatLonFloor() {
        super();
    }

    public MWZLatLonFloor(Double latitude, Double longitude, Integer floor) {
        super(latitude, longitude);
        this.floor = floor;
        this.accuracy = 0;
    }

    public MWZLatLonFloor(Double latitude, Double longitude, Integer floor, Integer accuracy) {
        super(latitude, longitude);
        this.floor = floor;
        this.accuracy = accuracy;
    }

    public String toJSONString() {
        return "{\"accuracy\":"+this.accuracy+",\"lat\":"+this.getLatitude()+",\"latitude\":"+this.getLatitude()+",\"lng\":"+this.getLongitude()+",\"longitude\":"+this.getLongitude()+",\"lon\":"+this.getLongitude()+",\"floor\":"+this.floor+"}";
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
