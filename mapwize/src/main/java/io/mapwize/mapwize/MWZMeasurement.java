package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MWZMeasurement {

    private Double latitude;
    private Double longitude;
    private Integer floor;
    private Integer accuracy;
    private Long validUntil;
    private Integer valididy;
    private String source;

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

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Long validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getValididy() {
        return valididy;
    }

    public void setValididy(Integer valididy) {
        this.valididy = valididy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MWZMeasurement() {
        super();
    }

    public MWZMeasurement(Double latitude, Double longitude, Integer floor, Integer accuracy, Long validUntil, Integer valididy, String source) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
        this.accuracy = accuracy;
        this.validUntil = validUntil;
        this.valididy = valididy;
        this.source = source;
    }

    public static MWZMeasurement getMWZMeasurement(String json) {
        MWZMeasurement result = null;
        try {
            result = new ObjectMapper().readValue(json, MWZMeasurement.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }

    public String toString() {
        return "Latitude="+this.latitude+" Longitude="+this.longitude+" Floor="+this.floor+" Accuracy="+this.accuracy+" ValidUntil="+this.validUntil+" Validity="+this.valididy+" Source="+this.source;
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
