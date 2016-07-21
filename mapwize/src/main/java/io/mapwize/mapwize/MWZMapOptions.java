package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MWZMapOptions {

    private String apiKey;
    private MWZLatLonBounds maxBounds;
    private MWZLatLon center;
    private int zoom;
    private int floor;
    private boolean zoomControl;
    private String accessKey;
    private String language;
    private int minZoom;

    private boolean isLocationEnabled;
    private boolean isBeaconsEnabled;
    private boolean useBrowserLocation = false;

    public boolean isUseBrowserLocation() {
        return useBrowserLocation;
    }

    public MWZMapOptions() {
        this.isBeaconsEnabled = true;
        this.isLocationEnabled = true;
    }

    public boolean isLocationEnabled() {
        return isLocationEnabled;
    }

    public void setIsLocationEnabled(boolean isLocationEnabled) {
        this.isLocationEnabled = isLocationEnabled;
    }

    public boolean isBeaconsEnabled() {
        return isBeaconsEnabled;
    }

    public void setIsBeaconsEnabled(boolean isBeaconsEnabled) {
        this.isBeaconsEnabled = isBeaconsEnabled;
    }

    public boolean isZoomControl() {
        return zoomControl;
    }

    public void setZoomControl(boolean zoomControl) {
        this.zoomControl = zoomControl;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public MWZLatLonBounds getMaxBounds() {
        return maxBounds;
    }

    public void setMaxBounds(MWZLatLonBounds maxBounds) {
        this.maxBounds = maxBounds;
    }

    public MWZLatLon getCenter() {
        return center;
    }

    public void setCenter(MWZLatLon center) {
        this.center = center;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public String toString() {
        return "MWZMapOptions : ApiKey="+apiKey+" MaxBounds="+maxBounds+" Center="+center+" Zoom="+zoom+" Floor="+floor+" LocationEnabled="+this.isLocationEnabled+" BeaconEnabled="+this.isBeaconsEnabled;
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
