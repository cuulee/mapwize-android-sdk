package io.mapwize.mapwize;

import java.util.ArrayList;
import java.util.List;

public class MWZGeometryPoint extends MWZGeometry {

    private List<Double> coordinates = new ArrayList<Double>();

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
