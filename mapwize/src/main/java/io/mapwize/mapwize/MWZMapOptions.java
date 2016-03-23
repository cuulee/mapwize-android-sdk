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

    public boolean isZoomControl() {
        return zoomControl;
    }

    public void setZoomControl(boolean zoomControl) {
        this.zoomControl = zoomControl;
    }

    public MWZMapOptions() {
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

    public String toString() {
        return "MWZMapOptions : ApiKey="+apiKey+" MaxBounds="+maxBounds+" Center="+center+"Zoom="+zoom+"Floor="+floor;
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
