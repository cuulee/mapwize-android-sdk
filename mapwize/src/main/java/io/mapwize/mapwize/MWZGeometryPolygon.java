package io.mapwize.mapwize;

import java.util.ArrayList;
import java.util.List;

public class MWZGeometryPolygon extends MWZGeometry {

    private List<List<List<Double>>> coordinates = new ArrayList<List<List<Double>>>();

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}
