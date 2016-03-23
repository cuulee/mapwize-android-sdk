package io.mapwize.mapwize;

/**
 * Created by Etienne on 22/03/16.
 */
public class MWZLatLonBounds {

    private MWZLatLon northEast;
    private MWZLatLon southWest;

    public MWZLatLon getNorthEast() {
        return northEast;
    }

    public void setNorthEast(MWZLatLon northEast) {
        this.northEast = northEast;
    }

    public MWZLatLon getSouthWest() {
        return southWest;
    }

    public void setSouthWest(MWZLatLon southWest) {
        this.southWest = southWest;
    }

    public MWZLatLonBounds (MWZLatLon northEast, MWZLatLon southWest) {
        super();
        this.northEast = northEast;
        this.southWest = southWest;
    }

    public Double[][] toArray() {
        Double[][] result = new Double[2][2];
        result[0][0] = this.northEast.getLatitude();
        result[0][1] = this.northEast.getLongitude();
        result[1][0] = this.southWest.getLatitude();
        result[1][1] = this.southWest.getLongitude();

        return result;
    }

    public String toString() {
        return "NorthEast="+this.northEast+" SouthEast="+this.southWest;
    }

}
