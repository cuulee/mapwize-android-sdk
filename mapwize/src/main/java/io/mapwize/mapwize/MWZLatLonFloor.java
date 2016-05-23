package io.mapwize.mapwize;

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

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public String toJSONString() {
        return "{\"accuracy\":"+this.accuracy+",\"lat\":"+this.getLatitude()+",\"latitude\":"+this.getLatitude()+",\"lng\":"+this.getLongitude()+",\"longitude\":"+this.getLongitude()+",\"lon\":"+this.getLongitude()+",\"floor\":"+this.floor+"}";
    }
}
