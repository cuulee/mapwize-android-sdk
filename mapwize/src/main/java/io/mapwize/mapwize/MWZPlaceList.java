package io.mapwize.mapwize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MWZPlaceList {

    String identifier;
    String name;
    String alias;
    String venueId;
    String[] placeIds;

    public MWZPlaceList() {
        super();
    }

    public MWZPlaceList(String identifier, String name, String alias, String venueId, String[] placeIds) {
        super();
        this.identifier = identifier;
        this.name = name;
        this.alias = alias;
        this.venueId = venueId;
        this.placeIds = placeIds;
    }

    public static MWZPlaceList getMWZPlaceList(String json) {
        MWZPlaceList placeList = null;
        try {
            placeList = new MWZPlaceList();
            JSONObject jObject = new JSONObject(json);
            placeList.identifier = jObject.getString("_id");
            placeList.name = jObject.getString("name");
            placeList.alias = jObject.getString("alias");
            placeList.venueId = jObject.getString("venueId");

            JSONArray places = jObject.getJSONArray("placeIds");
            placeList.placeIds = new String[places.length()];
            for (int i=0;i<places.length();i++) {
                String place = places.getString(i);
                placeList.placeIds[i] = place;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return placeList;
    }

    public String toString() {
        return "Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId+" #Places="+this.placeIds.length;
    }

}
