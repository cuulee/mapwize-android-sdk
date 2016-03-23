package io.mapwize.mapwize;

import android.graphics.Color;
import android.util.Log;

public class MWZPlaceStyle {

    private Integer strokeColor;
    private Integer strokeWidth;
    private Integer fillColor;
    private Integer labelBackgroundColor;
    private String markerUrl;

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    public void setLabelBackgroundColor(int labelBackgroundColor) {
        this.labelBackgroundColor = labelBackgroundColor;
    }

    public String getMarkerUrl() {
        return markerUrl;
    }

    public void setMarkerUrl(String markerUrl) {
        this.markerUrl = markerUrl;
    }

    public String toJSONString() {
        boolean isFirst = true;
        String s = "{";
        if (this.strokeColor != null) {
            isFirst = false;
            s+= "\"strokeColor\":\""+colorToString(this.strokeColor)+"\"";
            s+= ",\"strokeOpacity\":"+(Color.alpha(this.strokeColor)/255.0);
        }
        if (this.strokeWidth != null) {
            if (isFirst) {
                s+= "\"strokeWidth\":"+this.strokeWidth;
                isFirst = false;
            }
            else {
                s+= ",\"fillColor\":\""+colorToString(this.fillColor)+"\"";
            }
        }
        if (this.fillColor != null) {
            if (isFirst) {
                s+= "\"fillColor\":\""+colorToString(this.fillColor)+"\"";
                isFirst = false;
            }
            else {
                s+= ",\"fillColor\":\""+colorToString(this.fillColor)+"\"";
            }
            s+= ",\"fillOpacity\":"+(Color.alpha(this.fillColor)/255.0);
        }
        if (this.labelBackgroundColor != null) {
            if (isFirst) {
                s+= "\"labelBackgroundColor\":\""+colorToString(this.labelBackgroundColor)+"\"";
                isFirst = false;
            }
            else {
                s+= ",\"labelBackgroundColor\":\""+colorToString(this.labelBackgroundColor)+"\"";
            }
            s+= ",\"labelBackgroundOpacity\":"+(Color.alpha(this.labelBackgroundColor)/255.0);
        }
        if (this.markerUrl != null) {
            if (isFirst) {
                s+= "\"markerUrl\":\""+this.markerUrl+"\"";
                isFirst = false;
            }
            else {
                s+= ",\"markerUrl\":\""+this.markerUrl+"\"";
            }
        }
        s+= "}";

        return s;
    }

    private String colorToString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
