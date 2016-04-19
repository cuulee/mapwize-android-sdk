package io.mapwize.mapwize;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class MWZWebView extends WebView {

    final private String SERVER_URL = "https://www.mapwize.io";
    final private String SDK_VERSION = "1.4.x";
    final private String ANDROID_SDK_VERSION = "1.4.2";
    final private String ANDROID_SDK_NAME = "ANDROID SDK";
    private static String CLIENT_APP_NAME;
    private MWZMapViewListener listener;
    private Integer floor;
    private Integer[] floors;
    private Integer zoom;
    private boolean followUserMode;
    private MWZLatLon center;
    private MWZMeasurement userPosition;

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
        this.getSettings().setGeolocationEnabled(true);
        this.setWebChromeClient(new MWZWebView.GeoWebChromeClient());
        this.loadUrl(SERVER_URL + "/sdk/mapwize-android-sdk/" + SDK_VERSION + "/map.html");
        final MWZWebView self = this;

        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                self.initMap(options);
            }
        });



    }

    public void initMap(MWZMapOptions options) {
        this.executeJS("Mapwize.config.SERVER = '" + SERVER_URL + "';");
        this.executeJS("Mapwize.config.SDK_NAME = '" + ANDROID_SDK_VERSION + "';");
        this.executeJS("Mapwize.config.SDK_VERSION = '" + ANDROID_SDK_NAME + "';");
        this.executeJS("Mapwize.config.CLIENT_APP_NAME = '" + CLIENT_APP_NAME + "';");
        this.executeJS("var map = Mapwize.map('map'," + options.toJSONString() + ");");
        this.addListeners();
    }

    private void addListeners() {
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
        this.executeJS("map.on('monitoredUuidsChange', function(e){android.onMonitoredUuidsChange((function(){return JSON.stringify(e.uuids)})())});");
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
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor);
        Log.i("SetUserPosition", latLonFloor.toJSONString());
        this.executeJS("map.setUserPosition("+latLonFloor.toJSONString()+")");
    }
    public void setUserPosition(Double latitude, Double longitude, Integer floor, Integer accuracy) {
        MWZLatLonFloor latLonFloor = new MWZLatLonFloor(latitude, longitude, floor, accuracy);
        Log.i("SetUserPosition", latLonFloor.toJSONString());
        this.executeJS("map.setUserPosition("+latLonFloor.toJSONString()+")");
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
    }

}
