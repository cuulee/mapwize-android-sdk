package io.mapwize.mapwize;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MWZMapView extends LinearLayout {

    private MWZWebView webView;
    public MWZMapOptions options;

    public MWZMapView(Context context) {
        super(context);
        initializeViews(context);
    }

    public MWZMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.options = new MWZMapOptions();
        this.setupAttributes(context, attrs);
        initializeViews(context);
    }

    public MWZMapView(Context context,
                      AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
        this.options = new MWZMapOptions();
        this.setupAttributes(context,attrs);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mapwize_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setupMap(this.options);
    }

    private void setupAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MWZMapView,
                0, 0);
        try {
            String apiKey = a.getString(R.styleable.MWZMapView_apikey);
            options.setApiKey(apiKey);
            /*float NWLat = a.getFloat(R.styleable.MapwizeView_bounds_northwest_latitude,Float.MAX_VALUE);
            float NWLon = a.getFloat(R.styleable.MapwizeView_bounds_northwest_longitude,Float.MAX_VALUE);
            float SELat = a.getFloat(R.styleable.MapwizeView_bounds_southeast_latitude,Float.MAX_VALUE);
            float SELon = a.getFloat(R.styleable.MapwizeView_bounds_southeast_longitude,Float.MAX_VALUE);
            if (NWLat != Float.MAX_VALUE && NWLon != Float.MAX_VALUE && SELat != Float.MAX_VALUE && SELon != Float.MAX_VALUE){
                options.setMaxBounds(new MWZLatLonBounds(new MWZLatLon(new Double(NWLat), new Double(NWLon)),(new MWZLatLon(new Double(SELat), new Double(SELon)))));
            }*/
            float centerLat = a.getFloat(R.styleable.MWZMapView_center_latitude, Float.MAX_VALUE);
            float centerLon = a.getFloat(R.styleable.MWZMapView_center_longitude, Float.MAX_VALUE);
            if (centerLat != Float.MAX_VALUE && centerLon != Float.MAX_VALUE) {
                options.setCenter(new MWZLatLon(new Double(centerLat), new Double(centerLon)));
            }
            int floor = a.getInteger(R.styleable.MWZMapView_floor, Integer.MAX_VALUE);
            if (floor != Integer.MAX_VALUE) {
                options.setFloor(floor);
            }
            int zoom = a.getInteger(R.styleable.MWZMapView_zoom, Integer.MAX_VALUE);
            if (zoom != Integer.MAX_VALUE) {
                options.setZoom(zoom);
            }
            options.setZoomControl(false);
        } finally {
            a.recycle();
        }
    }

    /**
     *  SDK Public methods
     */
    public void setupMap(MWZMapOptions options) {
        this.webView = (MWZWebView)this.findViewById(R.id.webview);
        this.webView.setupMap(options);
    }

    public void setupMap(MWZMapOptions options, MWZMapViewListener listener) {
        this.webView = (MWZWebView)this.findViewById(R.id.webview);
        this.webView.setupMap(options);
        this.webView.setListener(listener);
    }

    public void setListener(MWZMapViewListener listener) {
        this.webView.setListener(listener);
    }

    public void fitBounds(MWZLatLonBounds bounds) {
        this.webView.fitBounds(bounds);
    }

    public void centerOnCoordinates(Double lat, Double lon, Integer floor, Integer zoom) {
        this.webView.centerOnCoordinates(lat, lon, floor, zoom);
    }

    public void setFloor(Integer floor) {
        this.webView.setFloor(floor);
    }

    public Integer getFloor() {
        return this.webView.getFloor();
    }

    public Integer[] getFloors() {
        return this.webView.getFloors();
    }

    public void setZoom(Integer zoom) {
        this.webView.setZoom(zoom);
    }

    public Integer getZoom() {
        return this.webView.getZoom();
    }

    public MWZLatLon getCenter() {
        return this.webView.getCenter();
    }

    public void centerOnVenue(MWZVenue venue) {
        this.webView.centerOnVenue(venue);
    }

    public void centerOnVenue(String id) {
        this.webView.centerOnVenue(id);
    }

    public void centerOnPlace(MWZPlace place) {
        this.webView.centerOnPlace(place);
    }

    public void centerOnPlace(String id) {
        this.webView.centerOnPlace(id);
    }

    public boolean getFollowUserMode() {
        return this.webView.isFollowUserMode();
    }

    public void setFollowUserMode(boolean follow) {
        this.webView.setFollowUserMode(follow);
    }

    public void centerOnUser(Integer zoom) {
        this.webView.centerOnUser(zoom);
    }

    public MWZMeasurement getUserPosition() {
        return this.webView.getUserPosition();
    }

    public void setUserPosition(Double latitude, Double longitude, Integer floor) {
        this.webView.setUserPosition(latitude, longitude, floor);
    }

    public void setUserPosition(Double latitude, Double longitude, Integer floor, Integer accuracy) {
        this.webView.setUserPosition(latitude, longitude, floor, accuracy);
    }

    public void newUserPositionMeasurement(MWZMeasurement measurement) {
        this.webView.newUserPositionMeasurement(measurement);
    }

    public void setUserHeading(Double heading) {
        this.webView.setUserHeading(heading);
    }

    public void unlockUserPosition() {
        this.webView.unlockUserPosition();
    }

    public void loadURL(String url) {
        this.webView.loadURL(url);
    }

    public void addMarker(Double latitude, Double longitude, Integer floor) {
        this.webView.addMarker(latitude, longitude, floor);
    }

    public void addMarker(String placeId) {
        this.webView.addMarker(placeId);
    }

    public void removeMarkers() {
        this.webView.removeMarkers();
    }

    public void showDirections(MWZPosition from, MWZPosition to) {
        this.webView.showDirections(from, to);
    }

    public void stopDirections() {
        this.webView.stopDirections();
    }

    public void access(String accesskey) {
        this.webView.access(accesskey);
    }

    public void setStyle(String placeId,MWZPlaceStyle style) {
        this.webView.setStyle(placeId, style);
    }

    public void getPlaceWithId (String placeId, PlaceCallbackInterface callback) {
        this.webView.getPlaceWithId(placeId, callback);
    }

    public void getPlaceWithName (String placeName, String venueId, PlaceCallbackInterface callback) {
        this.webView.getPlaceWithName(placeName, venueId, callback);
    }

    public void getPlaceWithAlias (String placeAlias, String venueId, PlaceCallbackInterface callback) {
        this.webView.getPlaceWithAlias(placeAlias, venueId, callback);
    }

    public void getVenueWithId (String venueId, VenueCallbackInterface callback) {
        this.webView.getVenueWithId(venueId, callback);
    }

    public void getVenueWithName (String venueName, VenueCallbackInterface callback) {
        this.webView.getVenueWithName(venueName, callback);
    }

    public void getVenueWithAlias (String venueAlias, VenueCallbackInterface callback) {
        this.webView.getVenueWithAlias(venueAlias, callback);
    }

    public void refresh() {
        this.webView.refresh();
    }

    public void setBottomMargin(Integer margin) {
        this.webView.setBottomMargin(margin);
    }
    public void setTopMargin(Integer margin) {
        this.webView.setTopMargin(margin);
    }

}