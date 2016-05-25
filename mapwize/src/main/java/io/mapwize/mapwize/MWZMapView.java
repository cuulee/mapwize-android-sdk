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
            options.setIsLocationEnabled(a.getBoolean(R.styleable.MWZMapView_isLocationEnabled, Boolean.TRUE));
            options.setIsBeaconsEnabled(a.getBoolean(R.styleable.MWZMapView_isBeaconsEnabled, Boolean.TRUE));
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

    public void destroy() {
        if(this.webView != null) webView.destroy();
    }

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

    public void removeUserPosition() {
        this.webView.removeUserPosition();
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

    public void showDirections(MWZPosition from, MWZPosition to, DirectionsCallbackInterface callback) {
        this.webView.showDirections(from, to, callback);
    }

    public void stopDirections() {
        this.webView.stopDirections();
    }

    public void access(String accesskey, AccessCallbackInterface callback) {
        this.webView.access(accesskey, callback);
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

    public void getPlaceListWithId (String placeListId, PlaceListCallbackInterface callback) {
        this.webView.getPlaceListWithId(placeListId, callback);
    }

    public void getPlaceListWithName (String placeListName, String venueId, PlaceListCallbackInterface callback) {
        this.webView.getPlaceListWithName(placeListName, venueId, callback);
    }

    public void getPlaceListWithAlias (String placeListAlias, String venueId, PlaceListCallbackInterface callback) {
        this.webView.getPlaceListWithAlias(placeListAlias, venueId, callback);
    }

    public void getPlaceListsForVenueId (String venueId, PlaceListsCallbackInterface callback) {
        this.webView.getPlaceListsForVenueId(venueId, callback);
    }

    public void getPlacesWithPlaceListId (String placeListId, PlacesCallbackInterface callback) {
        this.webView.getPlacesWithPlaceListId(placeListId, callback);
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