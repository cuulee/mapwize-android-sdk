package io.mapwize.mapwize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;


public class MWZWebView extends WebView implements LocationListener, BeaconConsumer, SensorEventListener {

    final private String TAG = "MWZWebView";
    final private String SERVER_URL = "https://www.mapwize.io";
    final private String SDK_VERSION = "1.7.x";
    final private String ANDROID_SDK_VERSION = "1.7.1";
    final private String ANDROID_SDK_NAME = "ANDROID SDK";
    private static String CLIENT_APP_NAME;
    private boolean isLoaded = false;
    private MWZMapViewListener listener;
    private MWZMapOptions options;
    private Integer floor;
    private Integer[] floors;
    private Integer zoom;
    private boolean followUserMode;
    private MWZLatLon center;
    private MWZMeasurement userPosition;
    private HashMap<String, Object> callbackMemory;
    private LocationManager locationManager;
    private BeaconManager beaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;

    private Context webviewContext;
    private Region[] monitoredRegions;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;

    private Activity act;

    private String initString = "map.on('zoomend', function(e){android.onZoomEnd((function(){return map.getZoom()})())});" +
            "map.on('click', function(e){android.onClick((function(){return JSON.stringify(e.latlng)})())});" +
            "map.on('contextmenu', function(e){android.onContextMenu((function(){return JSON.stringify(e.latlng)})())});" +
            "map.on('floorChange', function(e){android.onFloorChange((function(){return map._floor})())});" +
            "map.on('floorsChange', function(e){android.onFloorsChange((function(){return JSON.stringify(map._floors)})())});" +
            "map.on('placeClick', function(e){android.onPlaceClick((function(){return JSON.stringify(e.place)})())});" +
            "map.on('venueClick', function(e){android.onVenueClick((function(){return JSON.stringify(e.venue)})())});" +
            "map.on('markerClick', function(e){android.onMarkerClick((function(){return JSON.stringify({lat:e.latlng.lat, lon:e.latlng.lng, floor:e.floor})})())});" +
            "map.on('moveend', function(e){android.onMoveEnd((function(){return JSON.stringify(map.getCenter())})())});" +
            "Mapwize.Location.on('userPositionChange', function(e){android.onUserPositionChange((function(){return JSON.stringify(e.userPosition)})())});" +
            "map.on('followUserModeChange', function(e){android.onFollowUserModeChange((function(){return JSON.stringify(e.active)})())});" +
            "map.on('directionsStart', function(e){android.onDirectionsStart((function(){return 'Directions loaded'})())});" +
            "map.on('directionsStop', function(e){android.onDirectionsStop((function(){return 'Directions stopped'})())});" +
            "map.on('apiResponse', function(e){android.onApiResponse((function(){return JSON.stringify({type:e.type, returnedType: e.returnedType, hash:e.hash, response:e.response, error:e.error})})())});" +
            "Mapwize.Location.setUseBrowserLocation(false);";


    public MWZWebView(Context context) {
        super(context);
        CLIENT_APP_NAME = context.getPackageName();
        this.webviewContext = context;
    }

    public MWZWebView(Context context, AttributeSet attrs) {

        super(context, attrs);
        CLIENT_APP_NAME = context.getPackageName();
        this.webviewContext = context;
    }

    public MWZWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CLIENT_APP_NAME = context.getPackageName();
        this.webviewContext = context;

    }

    public void setupMap(Activity activity, MWZMapOptions options) {
        callbackMemory = new HashMap<>();
        this.options = options;
        this.act = activity;
        this.setupWebView(options);
    }

    private void setupWebView(final MWZMapOptions options) {
        // Debugger
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(this, true);
        }

        this.locationManager = (LocationManager) this.webviewContext.getSystemService(Context.LOCATION_SERVICE);
        this.beaconManager = BeaconManager.getInstanceForApplication(this.webviewContext);
        this.beaconManager.getBeaconParsers().add(0, new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        this.monitoredRegions = new Region[0];
        this.mSensorManager = (SensorManager) this.webviewContext.getSystemService(Context.SENSOR_SERVICE);
        backgroundPowerSaver = new BackgroundPowerSaver(this.webviewContext);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.addJavascriptInterface(this, "android");
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.setWebChromeClient(new MWZWebView.GeoWebChromeClient());
        this.loadUrl(SERVER_URL+"/sdk/mapwize-android-sdk/" + SDK_VERSION + "/map.html");
        final MWZWebView self = this;

        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                self.initMap(options);
                if (listener != null) {
                    listener.onMapLoaded();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (listener != null) {
                        listener.onReceivedError(description);
                    }
                    if (!self.isLoaded) {
                        self.loadUrl("about:blank");
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (listener != null) {
                        listener.onReceivedError("" + error.getDescription());
                    }
                    if (!self.isLoaded) {
                        self.loadUrl("about:blank");
                    }
                }
            }
        });
    }

    public void initMap(MWZMapOptions options) {
        this.executeJS("Mapwize.config.SERVER = '" + SERVER_URL + "';");
        this.executeJS("Mapwize.config.SDK_NAME = '" + ANDROID_SDK_NAME + "';");
        this.executeJS("Mapwize.config.SDK_VERSION = '" + ANDROID_SDK_VERSION + "';");
        this.executeJS("Mapwize.config.CLIENT_APP_NAME = '" + CLIENT_APP_NAME + "';");

        if (options.isBeaconsEnabled()) {
            this.initString += "Mapwize.Location.on('monitoredUuidsChange', function(e){android.onMonitoredUuidsChange((function(){return JSON.stringify(e.uuids)})())});";
        }
        this.executeJS("var map = Mapwize.map('map'," + options.toJSONString() + ", function () {" + this.initString + "android.onMapLoad();})");
        this.isLoaded = true;
    }

    protected void executeJS(final String js) {
        final MWZWebView self = this;
        this.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    self.evaluateJavascript(js, null);
                } else {
                    self.loadUrl("javascript:" + js);
                }
            }
        });
    }


    /**
     * Javascript interface for event listener
     */

    @JavascriptInterface
    public void onMapLoad() {
        if (this.listener != null) {
            this.listener.onMapLoad();
        }
        if (this.options.isLocationEnabled()) {
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (this.options.isBeaconsEnabled()) {
            this.beaconManager.bind(this);
        }
    }

    @JavascriptInterface
    public void onZoomEnd(String value) {
        this.zoom = Integer.parseInt(value);
        if (this.listener != null) {
            this.listener.onZoomEnd(this.zoom);
        }
    }

    @JavascriptInterface
    public void onClick(String value) {
        MWZLatLon latLng = MWZJSONParser.getLatLon(value);
        if (this.listener != null) {
            this.listener.onClick(latLng);
        }
    }

    @JavascriptInterface
    public void onContextMenu(String value) {
        MWZLatLon latLng = MWZJSONParser.getLatLon(value);
        if (this.listener != null) {
            this.listener.onContextMenu(latLng);
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
        MWZPlace place = MWZJSONParser.getPlace(value);
        if (this.listener != null) {
            this.listener.onPlaceClick(place);
        }
    }

    @JavascriptInterface
    public void onVenueClick(String value) {
        MWZVenue venue = MWZJSONParser.getVenue(value);
        if (this.listener != null) {
            this.listener.onVenueClick(venue);
        }
    }

    @JavascriptInterface
    public void onMarkerClick(String value) {
        MWZPosition position = MWZJSONParser.getPosition(value);
        if (this.listener != null) {
            this.listener.onMarkerClick(position);
        }
    }

    @JavascriptInterface
    public void onMoveEnd(String value) {
        this.center = MWZJSONParser.getLatLon(value);
        if (this.listener != null) {
            this.listener.onMoveEnd(this.center);
        }
    }

    @JavascriptInterface
    public void onUserPositionChange(String value) {
        this.userPosition = MWZJSONParser.getMeasurement(value);
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
            this.updateMonitoredUuids(uuids);
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
                PlaceCallbackInterface callback = (PlaceCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(null, error);
                } else if (jObject.isNull("response")) {
                    callback.onResponse(null, null);
                } else {
                    JSONObject response = jObject.getJSONObject("response");
                    if (!response.isNull("_id")) {
                        MWZPlace place = MWZJSONParser.getPlace(response);
                        callback.onResponse(place, null);
                    } else {
                        callback.onResponse(null, null);
                    }
                }
            }
            if (type.equals("places")) {
                PlacesCallbackInterface callback = (PlacesCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(null, error);
                } else if (jObject.isNull("response")) {
                    callback.onResponse(null, null);
                } else {
                    JSONArray response = jObject.getJSONArray("response");
                    MWZPlace[] places = MWZJSONParser.getPlaces(response);
                    callback.onResponse(places, null);
                }
            }
            if (type.equals("venue")) {
                VenueCallbackInterface callback = (VenueCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(null, error);
                } else if (jObject.isNull("response")) {
                    callback.onResponse(null, null);
                } else {
                    JSONObject response = jObject.getJSONObject("response");
                    if (!response.isNull("_id")) {
                        MWZVenue venue = MWZJSONParser.getVenue(response);
                        callback.onResponse(venue, null);
                    } else {
                        callback.onResponse(null, null);
                    }
                }
            }
            if (type.equals("placeList")) {
                PlaceListCallbackInterface callback = (PlaceListCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(null, error);
                } else if (jObject.isNull("response")) {
                    callback.onResponse(null, null);
                } else {
                    JSONObject response = jObject.getJSONObject("response");
                    if (!response.isNull("_id")) {
                        MWZPlaceList placeList = MWZJSONParser.getPlaceList(response);
                        callback.onResponse(placeList, null);
                    } else {
                        callback.onResponse(null, null);
                    }
                }
            }
            if (type.equals("placeLists")) {
                PlaceListsCallbackInterface callback = (PlaceListsCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(null, error);
                } else if (jObject.isNull("response")) {
                    callback.onResponse(null, null);
                } else {
                    JSONArray response = jObject.getJSONArray("response");
                    if (response.length() > 0) {
                        MWZPlaceList[] placeLists = new MWZPlaceList[response.length()];
                        for (int i = 0; i < response.length(); i++) {
                            MWZPlaceList placeList = MWZJSONParser.getPlaceList(response.getJSONObject(i));
                            placeLists[i] = placeList;
                        }
                        callback.onResponse(placeLists, null);
                    } else {
                        callback.onResponse(null, null);
                    }
                }
            }
            if (type.equals("showDirections")) {
                DirectionsCallbackInterface callback = (DirectionsCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(error);
                } else {
                    callback.onResponse(null);
                }
            }
            if (type.equals("access")) {
                AccessCallbackInterface callback = (AccessCallbackInterface) callbackMemory.get(hash);
                callback.onResponse(jObject.getBoolean("response"));
            }
            if (type.equals("loadUrl")) {
                LoadURLCallbackInterface callback = (LoadURLCallbackInterface) callbackMemory.get(hash);
                if (!"".equals(jObject.getString("error"))) {
                    Error error = new Error("MWZErrorDomain");
                    callback.onResponse(error);
                } else {
                    callback.onResponse(null);
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
        this.executeJS("map.fitBounds(new L.LatLngBounds(new L.LatLng(" + latNW + ", " + lonNW + "), new L.LatLng(" + latSE + ", " + lonSE + ")));");
    }

    public void centerOnCoordinates(Double lat, Double lon, Integer floor, Integer zoom) {
        if (floor == null && zoom == null) {
            this.executeJS("map.centerOnCoordinates(" + lat + "," + lon + ")");
        } else if (floor == null) {
            this.executeJS("map.centerOnCoordinates(" + lat + "," + lon + ",null," + zoom + ")");
        } else if (zoom == null) {
            this.executeJS("map.centerOnCoordinates(" + lat + "," + lon + "," + floor + ",null)");
        } else {
            this.executeJS("map.centerOnCoordinates(" + lat + "," + lon + "," + floor + "," + zoom + ")");
        }
    }

    public void setFloor(Integer floor) {
        this.executeJS("map.setFloor(" + floor + ")");
    }

    public void setZoom(Integer zoom) {
        this.executeJS("map.setZoom(" + zoom + ")");
    }

    public void centerOnVenue(MWZVenue venue) {
        this.executeJS("map.centerOnVenue('" + venue.getIdentifier() + "')");
    }

    public void centerOnVenue(String id) {
        this.executeJS("map.centerOnVenue('" + id + "')");
    }

    public void centerOnPlace(MWZPlace place) {
        this.executeJS("map.centerOnPlace('" + place.getIdentifier() + "')");
    }

    public void centerOnPlace(String id) {
        this.executeJS("map.centerOnPlace('" + id + "')");
    }

    public void setFollowUserMode(boolean follow) {
        this.executeJS("map.setFollowUserMode(" + follow + ")");
    }

    public void centerOnUser(Integer zoom) {
        this.executeJS("map.centerOnUser('" + zoom + "')");
    }

    public void newUserPositionMeasurement(MWZMeasurement measurement) {
        this.executeJS("Mapwize.Location.newUserPositionMeasurement(" + measurement.toJSONString() + ")");
    }

    public void setUserHeading(Double heading) {
        this.executeJS("Mapwize.Location.setUserHeading(" + heading + ")");
    }

    public void setUserPosition(Double latitude, Double longitude, Integer floor) {
        if (latitude == null || longitude == null) {
            return;
        }
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor);
        this.executeJS("Mapwize.Location.setUserPosition(" + latLonFloor.toJSONString() + ")");
    }

    public void setUserPosition(Double latitude, Double longitude, Integer floor, Integer accuracy) {
        if (latitude == null || longitude == null) {
            return;
        }
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor, accuracy);
        this.executeJS("Mapwize.Location.setUserPosition(" + latLonFloor.toJSONString() + ")");
    }

    public void removeUserPosition() {
        this.executeJS("Mapwize.Location.setUserPosition(null)");
    }

    public void unlockUserPosition() {
        this.executeJS("Mapwize.Location.unlockUserPosition()");
    }

    public void loadURL(String url, LoadURLCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("map.loadUrl('" + url + "',function(err){map.fire('apiResponse', {returnedType:'loadUrl', hash:'" + hash + "', error:err?'error':''});})");
    }

    public void addMarker(Double latitude, Double longitude, Integer floor) {
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor);
        this.executeJS("map.addMarker(" + latLonFloor.toJSONString() + ")");
    }

    public void addMarker(String placeId) {
        this.executeJS("map.addMarker({\"placeId\":'" + placeId + "'})");
    }

    public void removeMarkers() {
        this.executeJS("map.removeMarkers()");
    }

    public void showDirections(MWZPosition from, MWZPosition to, DirectionsCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("map.showDirections(" + from.toJSONString() + "," + to.toJSONString() + ",null,function(err){if (err) {map.fire('apiResponse', {returnedType:'showDirections', hash:'" + hash + "', response:{}, error:0});}})");
    }

    public void stopDirections() {
        this.executeJS("map.stopDirections()");
    }

    public void access(String accesskey, AccessCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("map.access('" + accesskey + "', function(isValid){map.fire('apiResponse', {returnedType:'access', hash:'" + hash + "', response:isValid})})");
    }

    public void setStyle(String placeId, MWZPlaceStyle style) {
        this.executeJS("map.setPlaceStyle('" + placeId + "'," + style.toJSONString() + ")");
    }

    public void setPreferredLanguage(String language) {
        this.executeJS("map.setPreferredLanguage('" + language + "');");
    }

    public void setBottomMargin(Integer margin) {
        this.executeJS("map.setBottomMargin(" + margin + ")");
    }

    public void setTopMargin(Integer margin) {
        this.executeJS("map.setTopMargin(" + margin + ")");
    }

    public void getPlaceWithId(String placeId, PlaceCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlace('" + placeId + "', function(err, place){map.fire('apiResponse', {returnedType:'place', hash:'" + hash + "', response:place, error:err?err.status:''});});");
    }

    public void getPlaceWithName(String placeName, String venueId, PlaceCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlace({name:'" + placeName + "', venueId:'" + venueId + "'}, function(err, place){map.fire('apiResponse', {returnedType:'place', hash:'" + hash + "', response:place, error:err?err.status:''});});");
    }

    public void getPlaceWithAlias(String placeAlias, String venueId, PlaceCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlace({alias:'" + placeAlias + "', venueId:'" + venueId + "'}, function(err, place){map.fire('apiResponse', {returnedType:'place', hash:'" + hash + "', response:place, error:err?err.status:''});});");
    }

    public void getVenueWithId(String venueId, VenueCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getVenue('" + venueId + "', function(err, venue){map.fire('apiResponse', {returnedType:'venue', hash:'" + hash + "', response:venue, error:err?err.status:''});});");
    }

    public void getVenueWithName(String venueName, VenueCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getVenue({name:'" + venueName + "'}, function(err, venue){map.fire('apiResponse', {returnedType:'venue', hash:'" + hash + "', response:venue, error:err?err.status:''});});");
    }

    public void getVenueWithAlias(String venueAlias, VenueCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getVenue({alias:'" + venueAlias + "'}, function(err, venue){map.fire('apiResponse', {returnedType:'venue', hash:'" + hash + "', response:venue, error:err?err.status:''});});");
    }

    public void getPlaceListWithId(String placeListId, PlaceListCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlaceList('" + placeListId + "', function(err, placeList){map.fire('apiResponse', {returnedType:'placeList', hash:'" + hash + "', response:placeList, error:err?err.status:''});});");
    }

    public void getPlaceListWithName(String placeListName, String venueId, PlaceListCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlaceList({name:'" + placeListName + "', venueId:'" + venueId + "'}, function(err, placeList){map.fire('apiResponse', {returnedType:'placeList', hash:'" + hash + "', response:placeList, error:err?err.status:''});});");
    }

    public void getPlaceListWithAlias(String placeListAlias, String venueId, PlaceListCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlaceList({alias:'" + placeListAlias + "', venueId:'" + venueId + "'}, function(err, placeList){map.fire('apiResponse', {returnedType:'placeList', hash:'" + hash + "', response:placeList, error:err?err.status:''});});");
    }

    public void getPlaceListsForVenueId(String venueId, PlaceListsCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlaceLists({venueId:'" + venueId + "'}, function(err, placeLists){map.fire('apiResponse', {returnedType:'placeLists', hash:'" + hash + "', response:placeLists, error:err?err.status:''});});");
    }

    public void getPlacesWithPlaceListId(String placeListId, PlacesCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("Mapwize.Api.getPlaces({placeListId:'" + placeListId + "'}, function(err, places){map.fire('apiResponse', {returnedType:'places', hash:'" + hash + "', response:places, error:err?err.status:''});});");
    }

    public void refresh() {
        this.executeJS("map.refresh()");
    }

    public void addRangedIBeacons(String uuid, Beacon[] beacons) {
        JSONArray beaconsArray = null;
        try {
            beaconsArray = new JSONArray();
            for (int i = 0; i < beacons.length; i++) {
                Beacon b = beacons[i];
                JSONObject o = new JSONObject();
                o.put("uuid", b.getId1());
                o.put("major", b.getId2());
                o.put("minor", b.getId3());
                o.put("rssi", b.getRssi());
                beaconsArray.put(o);
            }
            String beaconsString = beaconsArray.toString();
            this.executeJS("Mapwize.Location.addRangedIBeacons('" + uuid + "'," + beaconsString + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateMonitoredUuids(String[] uuids) {
        try {
            for (int i = 0; i < this.monitoredRegions.length; i++) {
                beaconManager.stopMonitoringBeaconsInRegion(this.monitoredRegions[i]);
            }
            Region[] rg = new Region[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                Region r = new Region(uuids[i], Identifier.parse(uuids[i]), null, null);
                beaconManager.startRangingBeaconsInRegion(r);
                rg[i] = r;
            }
            this.monitoredRegions = rg;
        } catch (RemoteException e) {
        }
    }

    private void updateMonitoredUuids(Region[] regions) {
        Region[] rg = new Region[regions.length];
        try {
            for (int i = 0; i < this.monitoredRegions.length; i++) {
                beaconManager.stopMonitoringBeaconsInRegion(this.monitoredRegions[i]);
            }
            for (int i = 0; i < regions.length; i++) {
                beaconManager.startRangingBeaconsInRegion(regions[i]);
                rg[i] = regions[i];
            }
            this.monitoredRegions = rg;
        } catch (RemoteException e) {
        }
    }

    public void startLocation(boolean useBeacon) {
        this.unlockUserPosition();
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        this.updateMonitoredUuids(new String[0]);
        this.options.setIsBeaconsEnabled(useBeacon);
        if (useBeacon) {
            this.updateMonitoredUuids(this.monitoredRegions);
        }
    }

    public void stopLocation() {
        this.locationManager.removeUpdates(this);
        this.mSensorManager.unregisterListener(this);
        this.setUserHeading(null);
        this.removeUserPosition();
        this.options.setIsBeaconsEnabled(false);
        this.monitoredRegions = new Region[0];
    }

    /**
     * Location
     * @param location
     */

    @Override
    public void onLocationChanged(Location location) {
        MWZMeasurement m = new MWZMeasurement(location.getLatitude(), location.getLongitude(), null, (int)(Math.floor(location.getAccuracy())),null,null, "gps");
        this.newUserPositionMeasurement(m);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBeaconServiceConnect() {
        final MWZWebView strongSelf = this;
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Beacon[] beaconsArray = new Beacon[beacons.size()];
                int i=0;
                for (Beacon b : beacons) {
                    beaconsArray[i++] = b;
                }
                strongSelf.addRangedIBeacons(region.getId1().toString(), beaconsArray);
            }
        });
    }

    @Override
    public Context getApplicationContext() {
        return this.act.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.act.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.act.bindService(intent, serviceConnection, i);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        currentDegree = degree;
        this.setUserHeading(new Double(currentDegree));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Add geolocation prompt to WebChromeClient
     */
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (listener != null) listener.onJavascriptConsoleCallback(consoleMessage);
            return super.onConsoleMessage(consoleMessage);
        }
    }
}
