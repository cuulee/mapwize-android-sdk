package io.mapwize.mapwize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZCoordinateInVenue extends MWZCoordinate {

    private String venueId;

    public MWZCoordinateInVenue(Double latitude, Double longitude, Double floor, String venueId) {
        super(latitude, longitude, floor);
        this.venueId = venueId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
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


    @Override
    public MWZDirectionPointWrapper toDirectionWrapper() {
        MWZDirectionPointWrapper pw = new MWZDirectionPointWrapper();
        pw.setLatitude(getLatitude());
        pw.setLongitude(getLongitude());
        pw.setFloor(getFloor());
        pw.setVenueId(venueId);
        return pw;
    }

    @Override
    public String toString() {
        return "MWZCoordinateInVenue{" +
                "venueId='" + venueId + '\'' +
                '}';
    }
}
