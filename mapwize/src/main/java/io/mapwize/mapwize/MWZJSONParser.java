package io.mapwize.mapwize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MWZJSONParser {

    public static Double getLatitude(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            return MWZJSONParser.getLatitude(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double getLatitude(JSONObject object) {
        try {
            Double latitude = null;
            if (!object.isNull("latitude")) {
                latitude = object.getDouble("latitude");
            }
            else if (!object.isNull("lat")) {
                latitude = object.getDouble("lat");
            }
            return latitude;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double getLongitude(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            return MWZJSONParser.getLongitude(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double getLongitude(JSONObject object) {

        try {
            Double longitude = null;
            if (!object.isNull("longitude")) {
                longitude = object.getDouble("longitude");
            }
            else if (!object.isNull("lon")) {
                longitude = object.getDouble("lon");
            }
            else if (!object.isNull("lng")) {
                longitude = object.getDouble("lng");
            }
            return longitude;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZLatLon getLatLon(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            return MWZJSONParser.getLatLon(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZLatLon getLatLon(JSONObject object) {
        MWZLatLon latLon = new MWZLatLon();
        latLon.setLatitude(MWZJSONParser.getLatitude(object));
        latLon.setLongitude(MWZJSONParser.getLongitude(object));
        if (latLon.getLatitude() == null || latLon.getLongitude() == null) {
            return null;
        }
        return latLon;
    }

    public static MWZPlace getPlace(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            return MWZJSONParser.getPlace(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPlace getPlace(JSONObject object) {
        try {
            MWZPlace place = new MWZPlace();
            place.setIdentifier(object.getString("_id"));
            place.setName(object.getString("name"));
            place.setAlias(object.getString("alias"));
            place.setVenueId(object.getString("venueId"));
            place.setTranslations(MWZJSONParser.getTranslations(object.getJSONArray("translations")));
            if (!object.isNull("data")) {
                place.setData(MWZJSONParser.jsonToMap(object.getJSONObject("data")));
            }
            return place;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPlace[] getPlaces(String json) {
        try {
            JSONArray jArray = new JSONArray(json);
            return MWZJSONParser.getPlaces(jArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPlace[] getPlaces(JSONArray array) {
        try {
            MWZPlace[] places = new MWZPlace[array.length()];
            for (int i=0;i<array.length();i++) {
                JSONObject jObject = array.getJSONObject(i);
                MWZPlace place = MWZJSONParser.getPlace(jObject);
                places[i] = place;
            }
            return places;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZVenue getVenue(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return MWZJSONParser.getVenue(object);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZVenue getVenue(JSONObject object) {
        try {
            MWZVenue venue = new MWZVenue();
            venue.setIdentifier(object.getString("_id"));
            venue.setName(object.getString("name"));
            venue.setAlias(object.getString("alias"));
            if (!object.isNull("data")) {
                venue.setData(MWZJSONParser.jsonToMap(object.getJSONObject("data")));
            }
            return venue;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZTranslation getTranslation(JSONObject object) {
        try {
            MWZTranslation translation = new MWZTranslation();
            if (!object.isNull("title")) {
                translation.setTitle(object.getString("title"));
            }
            if (!object.isNull("subtitle")){
                translation.setSubtitle(object.getString("subtitle"));
            }
            if (!object.isNull("details")) {
                translation.setDetails(object.getString("details"));
            }
            if (!object.isNull("language")) {
                translation.setLanguage(object.getString("language"));
            }
            return translation;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZTranslation[] getTranslations(JSONArray array) {
        try {
            MWZTranslation[] translations = null;
            translations = new MWZTranslation[array.length()];
            for (int i=0;i<array.length();i++) {
                JSONObject jObject = array.getJSONObject(i);
                MWZTranslation translation = MWZJSONParser.getTranslation(jObject);
                translations[i] = translation;
            }
            return translations;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZMeasurement getMeasurement(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return MWZJSONParser.getMeasurement(object);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZMeasurement getMeasurement(JSONObject object) {
        try {
            MWZMeasurement measurement = new MWZMeasurement();

            measurement.setLatitude(MWZJSONParser.getLatitude(object));
            measurement.setLongitude(MWZJSONParser.getLongitude(object));
            if (!object.isNull("floor")) {
                measurement.setFloor(object.getInt("floor"));
            }
            if (!object.isNull("accuracy")) {
                measurement.setAccuracy(object.getInt("accuracy"));
            }
            if (!object.isNull("validUntil")) {
                measurement.setValidUntil(object.getLong("validUntil"));
            }
            if (!object.isNull("validity")) {
                measurement.setValididy(object.getInt("validity"));
            }
            if (!object.isNull("source")) {
                measurement.setSource(object.getString("source"));
            }
            return measurement;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPlaceList getPlaceList(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return MWZJSONParser.getPlaceList(object);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPlaceList getPlaceList(JSONObject object) {
        try {
            MWZPlaceList placeList = new MWZPlaceList();
            placeList.setIdentifier(object.getString("_id"));
            placeList.setName(object.getString("name"));
            placeList.setAlias(object.getString("alias"));
            placeList.setVenueId(object.getString("venueId"));
            if (!object.isNull("data")) {
                placeList.setData(MWZJSONParser.jsonToMap(object.getJSONObject("data")));
            }

            if (!object.isNull("placeIds")) {
                JSONArray places = object.getJSONArray("placeIds");
                placeList.setPlaceIds(new String[places.length()]);
                for (int i=0;i<places.length();i++) {
                    String place = places.getString(i);
                    placeList.getPlaceIds()[i] = place;
                }
            }
            else {
                placeList.setPlaceIds(new String[0]);
            }
            return placeList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPosition getPosition(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return MWZJSONParser.getPosition(object);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MWZPosition getPosition(JSONObject object) {
        try {
            MWZPosition position = new MWZPosition();

            position.setLatitude(MWZJSONParser.getLatitude(object));
            position.setLongitude(MWZJSONParser.getLongitude(object));
            if (!object.isNull("floor")) {
                position.setFloor(object.getInt("floor"));
            }
            if (!object.isNull("venueId")) {
                position.setVenueId(object.getString("venueId"));
            }
            if (!object.isNull("placeId")) {
                position.setPlaceId(object.getString("placeId"));
            }
            if (!object.isNull("placeListId")) {
                position.setPlaceListId(object.getString("placeListId"));
            }
            return position;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Utils to build dictionary from JSONObject
     */
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
