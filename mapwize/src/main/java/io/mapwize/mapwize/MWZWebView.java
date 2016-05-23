package io.mapwize.mapwize;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class MWZWebView extends WebView {

    final private String SERVER_URL = "https://www.mapwize.io";
    final private String SDK_VERSION = "1.5.x";
    final private String ANDROID_SDK_VERSION = "1.5.0";
    final private String ANDROID_SDK_NAME = "ANDROID SDK";
    private static String CLIENT_APP_NAME;
    private MWZMapViewListener listener;
    private Integer floor;
    private Integer[] floors;
    private Integer zoom;
    private boolean followUserMode;
    private MWZLatLon center;
    private MWZMeasurement userPosition;
    private HashMap<String, Object> callbackMemory;

    public MWZWebView(Context context) {
        super(context);
        CLIENT_APP_NAME = context.getPackageName();
    }

    public MWZWebView(Context context, AttributeSet attrs) {

        super(context, attrs);
        CLIENT_APP_NAME = context.getPackageName();
    }

    public MWZWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CLIENT_APP_NAME = context.getPackageName();
    }

    public void setupMap(MWZMapOptions options) {
        callbackMemory = new HashMap<>();
        this.setupWebView(options);
    }

    private void setupWebView(final MWZMapOptions options) {
        // Debugger
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(this, true );
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.addJavascriptInterface(this, "android");
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.getSettings().setGeolocationEnabled(options.isLocationEnabled());
        this.setWebChromeClient(new MWZWebView.GeoWebChromeClient());
        this.loadUrl(SERVER_URL + "/sdk/mapwize-android-sdk/" + SDK_VERSION + "/map.html");
        final MWZWebView self = this;

        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                self.initMap(options);
                if(listener != null) {
                    listener.onMapLoaded();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.M ) {
                    if (listener != null) {
                        listener.onReceivedError(description);
                    }
                    self.loadUrl("about:blank");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    if (listener != null) {
                        listener.onReceivedError("" + error.getDescription());
                    }
                    self.loadUrl("about:blank");
                }
            }

        });
    }

    public void initMap(MWZMapOptions options) {
        this.executeJS("Mapwize.config.SERVER = '" + SERVER_URL + "';");
        this.executeJS("Mapwize.config.SDK_NAME = '" + ANDROID_SDK_NAME + "';");
        this.executeJS("Mapwize.config.SDK_VERSION = '" + ANDROID_SDK_VERSION + "';");
        this.executeJS("Mapwize.config.CLIENT_APP_NAME = '" + CLIENT_APP_NAME + "';");
        this.executeJS("var map = Mapwize.map('map'," + options.toJSONString() + ");");
        this.addListeners(options);
    }

    private void addListeners(MWZMapOptions options) {
        this.executeJS("map.on('zoomend', function(e){android.onZoomEnd((function(){return map.getZoom()})())});");
        this.executeJS("map.on('click', function(e){android.onClick((function(){return JSON.stringify(e.latlng)})())});");
        this.executeJS("map.on('contextmenu', function(e){android.onContextMenu((function(){return JSON.stringify(e.latlng)})())});");
        this.executeJS("map.on('floorChange', function(e){android.onFloorChange((function(){return map._floor})())});");
        this.executeJS("map.on('floorsChange', function(e){android.onFloorsChange((function(){return JSON.stringify(map._floors)})())});");
        this.executeJS("map.on('placeClick', function(e){android.onPlaceClick((function(){return JSON.stringify(e.place)})())});");
        this.executeJS("map.on('venueClick', function(e){android.onVenueClick((function(){return JSON.stringify(e.venue)})())});");
        this.executeJS("map.on('markerClick', function(e){android.onMarkerClick((function(){return JSON.stringify({lat:e.latlng.lat, lon:e.latlng.lng, floor:e.floor})})())});");
        this.executeJS("map.on('moveend', function(e){android.onMoveEnd((function(){return JSON.stringify(map.getCenter())})())});");
        this.executeJS("map.on('userPositionChange', function(e){android.onUserPositionChange((function(){return JSON.stringify(e.userPosition)})())});");
        this.executeJS("map.on('followUserModeChange', function(e){android.onFollowUserModeChange((function(){return JSON.stringify(e.active)})())});");
        this.executeJS("map.on('directionsStart', function(e){android.onDirectionsStart((function(){return 'Directions loaded'})())});");
        this.executeJS("map.on('directionsStop', function(e){android.onDirectionsStop((function(){return 'Directions stopped'})())});");
        this.executeJS("map.on('apiResponse', function(e){android.onApiResponse((function(){return JSON.stringify({type:e.type, returnedType: e.returnedType, hash:e.hash, response:e.response})})())});");
        if (options.isBeaconsEnabled()) {
            this.executeJS("map.on('monitoredUuidsChange', function(e){android.onMonitoredUuidsChange((function(){return JSON.stringify(e.uuids)})())});");
        }
    }

    protected void executeJS (String js) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJavascript(js, null);
        } else {
            this.loadUrl("javascript:" + js);
        }
    }


    /**
     * Javascript interface for event listener
     */
    @JavascriptInterface
    public void onZoomEnd(String value) {
        this.zoom = Integer.parseInt(value);
        if (this.listener != null) {
            this.listener.onZoomEnd(this.zoom);
        }
    }

    @JavascriptInterface
    public void onClick(String value) {
        MWZLatLon latlng = MWZLatLon.getMWZLatLon(value);
        if (this.listener != null) {
            this.listener.onClick(latlng);
        }
    }

    @JavascriptInterface
    public void onContextMenu(String value) {
        MWZLatLon latlng = MWZLatLon.getMWZLatLon(value);
        if (this.listener != null) {
            this.listener.onContextMenu(latlng);
        }
    }

    @JavascriptInterface
    public void onFloorChange(String value) {
        this.floor = Integer.parseInt(value);
        if (this.listener != null) {
            this.listener.onFloorChange(this.floor);
        }
    }

    @JavascriptInterface
    public void onFloorsChange(String value) {
        try {
            this.floors = new ObjectMapper().readValue(value, Integer[].class);
            if (this.listener != null) {
                this.listener.onFloorsChange(this.floors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onPlaceClick(String value) {
        MWZPlace place = MWZPlace.getMWZPlace(value);
        if (this.listener != null) {
            this.listener.onPlaceClick(place);
        }
    }

    @JavascriptInterface
    public void onVenueClick(String value) {
        MWZVenue venue = MWZVenue.getMWZVenue(value);
        if (this.listener != null) {
            this.listener.onVenueClick(venue);
        }
    }

    @JavascriptInterface
    public void onMarkerClick(String value) {
        MWZPosition position = MWZPosition.getMWZPosition(value);
        if (this.listener != null) {
            this.listener.onMarkerClick(position);
        }
    }

    @JavascriptInterface
    public void onMoveEnd(String value) {
        this.center = MWZLatLon.getMWZLatLon(value);
        if (this.listener != null) {
            this.listener.onMoveEnd(center);
        }
    }

    @JavascriptInterface
    public void onUserPositionChange(String value) {
        this.userPosition = MWZMeasurement.getMWZMeasurement(value);
        if (this.listener != null) {
            this.listener.onUserPositionChange(this.userPosition);
        }
    }

    @JavascriptInterface
    public void onFollowUserModeChange(String value) {
        this.followUserMode = Boolean.parseBoolean(value);
        if (this.listener != null) {
            this.listener.onFollowUserModeChange(this.followUserMode);
        }
    }

    @JavascriptInterface
    public void onDirectionsStart(String value) {
        if (this.listener != null) {
            this.listener.onDirectionsStart(value);
        }
    }

    @JavascriptInterface
    public void onDirectionsStop(String value) {
        if (this.listener != null) {
            this.listener.onDirectionsStop(value);
        }
    }

    @JavascriptInterface
    public void onMonitoredUuidsChange(String value) {
        String[] uuids = new String[0];
        try {
            uuids = new ObjectMapper().readValue(value, String[].class);
            if (this.listener != null) {
                this.listener.onMonitoredUuidsChange(uuids);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onApiResponse(String value) {
        try {

            JSONObject jObject = new JSONObject(value);
            String hash = jObject.getString("hash");
            String type = jObject.getString("returnedType");

            if (type.equals("place")) {
                PlaceCallbackInterface callback = (PlaceCallbackInterface)callbackMemory.get(hash);
                if (jObject.isNull("response")){
                    callback.onResponse(null);
                }
                else {
                    JSONObject response = jObject.getJSONObject("response");
                    if (!response.isNull("_id")) {
                        MWZPlace place = MWZPlace.getMWZPlace(response.toString());
                        callback.onResponse(place);
                    }
                    else {
                        callback.onResponse(null);
                    }
                }
            }
            if (type.equals("places")) {
                PlacesCallbackInterface callback = (PlacesCallbackInterface) callbackMemory.get(hash);
                if (jObject.isNull("response")){
                    callback.onResponse(null);
                }
                else {
                    JSONArray response = jObject.getJSONArray("response");
                    if (response.length() > 0) {
                        MWZPlace[] places = new MWZPlace[response.length()];
                        for (int i=0;i<response.length();i++) {
                            MWZPlace place = MWZPlace.getMWZPlace(response.get(i).toString());
                            places[i] = place;
                        }
                        callback.onResponse(places);
                    }
                    else {
                        callback.onResponse(null);
                    }
                }
            }
            if (type.equals("venue")) {
                VenueCallbackInterface callback = (VenueCallbackInterface)callbackMemory.get(hash);
                if (jObject.isNull("response")){
                    callback.onResponse(null);
                }
                else {
                    JSONObject response = jObject.getJSONObject("response");
                    if (!response.isNull("_id")) {
                        MWZVenue venue = MWZVenue.getMWZVenue(response.toString());
                        callback.onResponse(venue);
                    }
                    else {
                        callback.onResponse(null);
                    }
                }
            }
            if (type.equals("placeList")) {
                PlaceListCallbackInterface callback = (PlaceListCallbackInterface) callbackMemory.get(hash);
                if (jObject.isNull("response")){
                    callback.onResponse(null);
                }
                else {
                    JSONObject response = jObject.getJSONObject("response");
                    if (!response.isNull("_id")) {
                        MWZPlaceList placeList = MWZPlaceList.getMWZPlaceList(response.toString());
                        callback.onResponse(placeList);
                    }
                    else {
                        callback.onResponse(null);
                    }
                }
            }
            if (type.equals("placeLists")) {
                PlaceListsCallbackInterface callback = (PlaceListsCallbackInterface) callbackMemory.get(hash);
                if (jObject.isNull("response")){
                    callback.onResponse(null);
                }
                else {
                    JSONArray response = jObject.getJSONArray("response");
                    if (response.length() > 0) {
                        MWZPlaceList[] placeLists = new MWZPlaceList[response.length()];
                        for (int i=0;i<response.length();i++) {
                            MWZPlaceList placeList = MWZPlaceList.getMWZPlaceList(response.get(i).toString());
                            placeLists[i] = placeList;
                        }
                        callback.onResponse(placeLists);
                    }
                    else {
                        callback.onResponse(null);
                    }
                }
            }

            callbackMemory.remove(hash);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * Getters and Setters
     */
    public Integer getFloor() {
        return floor;
    }

    public Integer[] getFloors() {
        return floors;
    }

    public Integer getZoom() {
        return zoom;
    }

    public boolean isFollowUserMode() {
        return followUserMode;
    }

    public MWZLatLon getCenter() {
        return center;
    }

    public MWZMeasurement getUserPosition() {
        return userPosition;
    }

    public void setListener(MWZMapViewListener listener) {
        this.listener = listener;
    }

    /**
     *  Mapwize.js interface methods
     */
    public void fitBounds(MWZLatLonBounds bounds) {
        Double latNW = bounds.getNorthEast().getLatitude();
        Double lonNW = bounds.getNorthEast().getLongitude();
        Double latSE = bounds.getSouthWest().getLatitude();
        Double lonSE = bounds.getSouthWest().getLongitude();
        this.executeJS("map.fitBounds(new L.LatLngBounds(new L.LatLng("+latNW+", "+lonNW+"), new L.LatLng("+latSE+", "+lonSE+")));");
    }

    public void centerOnCoordinates(Double lat, Double lon, Integer floor, Integer zoom) {
        if (floor == null && zoom == null) {
            this.executeJS("map.centerOnCoordinates("+lat+","+lon+")");
        }
        else if (floor == null) {
            this.executeJS("map.centerOnCoordinates("+lat+","+lon+",null,"+zoom+")");
        }
        else if (zoom == null) {
            this.executeJS("map.centerOnCoordinates("+lat+","+lon+","+floor+",null)");
        }
        else {
            this.executeJS("map.centerOnCoordinates("+lat+","+lon+","+floor+","+zoom+")");
        }
    }

    public void setFloor(Integer floor) {
        this.executeJS("map.setFloor("+floor+")");
    }

    public void setZoom(Integer zoom) {
        this.executeJS("map.setZoom("+zoom+")");
    }

    public void centerOnVenue(MWZVenue venue) {
        this.executeJS("map.centerOnVenue('"+venue.identifier+"')");
    }

    public void centerOnVenue(String id) {
        this.executeJS("map.centerOnVenue('"+id+"')");
    }

    public void centerOnPlace(MWZPlace place) {
        this.executeJS("map.centerOnPlace('"+place.identifier+"')");
    }

    public void centerOnPlace(String id) {
        this.executeJS("map.centerOnPlace('"+id+"')");
    }

    public void setFollowUserMode(boolean follow) {
        this.executeJS("map.setFollowUserMode("+follow+")");
    }

    public void centerOnUser(Integer zoom) {
        this.executeJS("map.centerOnUser('"+zoom+"')");
    }

    public void newUserPositionMeasurement(MWZMeasurement measurement) {
        this.executeJS("map.newUserPositionMeasurement("+measurement.toJSONString()+")");
    }

    public void setUserHeading(Double heading) {
        this.executeJS("map.setUserHeading("+heading+")");
    }

    public void setUserPosition(Double latitude, Double longitude, Integer floor) {
        if (latitude == null || longitude == null) {
            return;
        }
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor);
        this.executeJS("map.setUserPosition("+latLonFloor.toJSONString()+")");
    }
    public void setUserPosition(Double latitude, Double longitude, Integer floor, Integer accuracy) {
        if (latitude == null || longitude == null) {
            return;
        }
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor, accuracy);
        this.executeJS("map.setUserPosition("+latLonFloor.toJSONString()+")");
    }

    public void removeUserPosition() {
        this.executeJS("map.setUserPosition(null)");
    }

    public void unlockUserPosition() {
        this.executeJS("map.unlockUserPosition()");
    }

    public void loadURL(String url) {
        this.executeJS("map.loadUrl('"+url+"')");
    }

    public void addMarker(Double latitude, Double longitude, Integer floor) {
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor);
        this.executeJS("map.addMarker("+latLonFloor.toJSONString()+")");
    }

    public void addMarker(String placeId) {
        this.executeJS("map.addMarker({\"placeId\":'"+placeId+"'})");
    }

    public void removeMarkers() {
        this.executeJS("map.removeMarkers()");
    }

    public void showDirections(MWZPosition from, MWZPosition to) {
        this.executeJS("map.showDirections("+from.toJSONString()+","+to.toJSONString()+",null,function(){})");
    }

    public void stopDirections() {
        this.executeJS("map.stopDirections()");
    }

    public void access(String accesskey) {
        this.executeJS("map.access('"+accesskey+"')");
    }

    public void setStyle(String placeId, MWZPlaceStyle style) {
        this.executeJS("map.setPlaceStyle('"+placeId+"',"+style.toJSONString()+")");
    }

    public void setBottomMargin(Integer margin) {
        this.executeJS("map.setBottomMargin("+margin+")");
    }

    public void setTopMargin(Integer margin) {
        this.executeJS("map.setTopMargin("+margin+")");
    }

    public void getPlaceWithId (String placeId, PlaceCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlace('"+placeId+"', function(err, place){map.fire('apiResponse', {returnedType:'place', hash:'"+hash+"', response:place});});");
    }

    public void getPlaceWithName (String placeName, String venueId, PlaceCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlace({name:'"+placeName+"', venueId:'"+venueId+"'}, function(err, place){map.fire('apiResponse', {returnedType:'place', hash:'"+hash+"', response:place});});");
    }

    public void getPlaceWithAlias (String placeAlias, String venueId, PlaceCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlace({alias:'"+placeAlias+"', venueId:'"+venueId+"'}, function(err, place){map.fire('apiResponse', {returnedType:'place', hash:'"+hash+"', response:place});});");
    }

    public void getVenueWithId (String venueId, VenueCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getVenue('"+venueId+"', function(err, venue){map.fire('apiResponse', {returnedType:'venue', hash:'"+hash+"', response:venue});});");
    }

    public void getVenueWithName (String venueName, VenueCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getVenue({name:'"+venueName+"'}, function(err, venue){map.fire('apiResponse', {returnedType:'venue', hash:'"+hash+"', response:venue});});");
    }

    public void getVenueWithAlias (String venueAlias, VenueCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getVenue({alias:'"+venueAlias+"'}, function(err, venue){map.fire('apiResponse', {returnedType:'venue', hash:'"+hash+"', response:venue});});");
    }

    public void getPlaceListWithId (String placeListId, PlaceListCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlaceList('"+placeListId+"', function(err, placeList){map.fire('apiResponse', {returnedType:'placeList', hash:'"+hash+"', response:placeList});});");
    }

    public void getPlaceListWithName (String placeListName, String venueId, PlaceListCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlaceList({name:'"+placeListName+"', venueId:'"+venueId+"'}, function(err, placeList){map.fire('apiResponse', {returnedType:'placeList', hash:'"+hash+"', response:placeList});});");
    }

    public void getPlaceListWithAlias (String placeListAlias, String venueId, PlaceListCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlaceList({alias:'"+placeListAlias+"', venueId:'"+venueId+"'}, function(err, placeList){map.fire('apiResponse', {returnedType:'placeList', hash:'"+hash+"', response:placeList});});");
    }

    public void getPlaceListsForVenueId (String venueId, PlaceListsCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlaceLists({venueId:'"+venueId+"'}, function(err, placeLists){map.fire('apiResponse', {returnedType:'placeLists', hash:'"+hash+"', response:placeLists});});");
    }

    public void getPlacesWithPlaceListId (String placeListId, PlacesCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.api.getPlaces({placeListId:'"+placeListId+"'}, function(err, places){map.fire('apiResponse', {returnedType:'places', hash:'"+hash+"', response:places});});");
    }

    public void refresh() {
        this.executeJS("map.refresh()");
    }
    /**
     * Add geolocation prompt to WebChromeClient
     */
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (listener != null) listener.onJavascriptConsoleCallback(consoleMessage);
            return super.onConsoleMessage(consoleMessage);
        }
    }

}
