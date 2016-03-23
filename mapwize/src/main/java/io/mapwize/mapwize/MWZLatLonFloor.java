package io.mapwize.mapwize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZLatLonFloor extends MWZLatLon {

    private Integer floor;

    public MWZLatLonFloor() {
        super();
    }

    public MWZLatLonFloor(Double latitude, Double longitude, Integer floor) {
        super(latitude, longitude);
        this.floor = floor;
    }

    public String toJSONString() {
        return "{\"accuracy\":0,\"lat\":"+this.getLatitude()+",\"latitude\":"+this.getLatitude()+",\"lng\":"+this.getLongitude()+",\"longitude\":"+this.getLongitude()+",\"lon\":"+this.getLongitude()+",\"floor\":"+this.floor+"}";
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
