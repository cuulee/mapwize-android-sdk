package io.mapwize.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import io.mapwize.mapwize.*;

public class MainActivity extends AppCompatActivity implements MWZMapViewListener{

    MWZMapView mapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Requesting permission for location
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // We have permission already. Do nothing.

            } else {

                // Requesting permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        5);
            }
        }
        mapview = (MWZMapView) this.findViewById(R.id.mwzview);
        mapview.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_accessKey:
                this.accessKey();
                break;
            case R.id.action_setZoom:
                this.setZoom();
                break;
            case R.id.action_centerOnCoordinates:
                this.centerOnCoordinates();
                break;
            case R.id.action_centerOnCoordinatesWithFloor:
                this.centerOnCoordinatesWithFloor();
                break;
            case R.id.action_setFloor:
                this.setFloor();
                break;
            case R.id.action_centerOnVenue:
                this.centerOnVenue();
                break;
            case R.id.action_centerOnPlace:
                this.centerOnPlace();
                break;
            case R.id.action_centerOnUser:
                this.centerOnUser();
                break;
            case R.id.action_loadUrl:
                this.loadUrl();
                break;
            case R.id.action_addMarker:
                this.addMarker();
                break;
            case R.id.action_addMarkerOnPlace:
                this.addMarkerOnPLace();
                break;
            case R.id.action_removeMarkers:
                this.removeMarkers();
                break;
            case R.id.action_setFollowUserModeON:
                this.setFollowUserModeON();
                break;
            case R.id.action_setFollowUserModeOFF:
                this.setFollowUserModeOFF();
                break;
            case R.id.action_setUserPosition:
                this.setUserPosition();
                break;
            case R.id.action_setUserPositionWithFloor:
                this.setUserPositionWithFloor();
                break;
            case R.id.action_unlockUserPosition:
                this.unlockUserPosition();
                break;
            case R.id.action_showDirections:
                this.showDirections();
                break;
            case R.id.action_stopDirections:
                this.stopDirections();
                break;
            case R.id.action_setStyle:
                this.setStyle();
                break;
            case R.id.action_setBottomMargin:
                this.setBottomMargin();
                break;
            case R.id.action_setTopMargin:
                this.setTopMargin();
                break;
            case R.id.action_resetMargin:
                this.resetMargin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  Test methods
     */
    public void accessKey () {
        this.mapview.access("YOUR ACCESS KEY");
    }

    public void setZoom(){
        this.mapview.setZoom(12);
    }

    public void centerOnCoordinates() {
        this.mapview.centerOnCoordinates(49.74252973220731, 4.599119424819946, null, 18);
    }

    public void centerOnCoordinatesWithFloor() {
        this.mapview.centerOnCoordinates(49.74252973220731, 4.599119424819946, 2, 18);
    }

    public void setFloor() {
        this.mapview.setFloor(2);
    }

    public void centerOnVenue() {
        this.mapview.centerOnVenue("56c2ea3402275a0b00fb00ac");
    }

    public void centerOnPlace() {
        this.mapview.centerOnPlace("56c3426202275a0b00fb00b9");
    }

    public void centerOnUser() {
        this.mapview.centerOnUser(19);
    }

    public void loadUrl() {
        this.mapview.loadURL("http://mwz.io/aaa");
    }

    public void addMarker() {
        this.mapview.addMarker(49.74278626088478, 4.598293304443359, null);
    }

    public void addMarkerOnPLace() {
        this.mapview.addMarker("56c3426202275a0b00fb00b9");
    }

    public void removeMarkers() {
        this.mapview.removeMarkers();
    }

    public void setFollowUserModeON() {
        this.mapview.setFollowUserMode(true);
    }

    public void setFollowUserModeOFF() {
        this.mapview.setFollowUserMode(false);
    }

    public void setUserPosition() {
        this.mapview.setUserPosition(49.74278626088478, 4.5982933044, null);
    }

    public void setUserPositionWithFloor() {
        this.mapview.setUserPosition(49.74278626088478, 4.5982933044, 2, 15);
    }

    public void unlockUserPosition() {
        this.mapview.unlockUserPosition();
    }

    public void showDirections() {
        MWZPosition from, to;
        from = new MWZPosition();
        from.setPlaceId("56c3426202275a0b00fb00b9");
        to = new MWZPosition();
        to.setPlaceId("56c3504102275a0b00fb00fa");
        this.mapview.showDirections(from, to);
    }

    public void stopDirections() {
        this.mapview.stopDirections();
    }

    public void setStyle() {
        MWZPlaceStyle style = new MWZPlaceStyle();
        style.setFillColor(Color.RED);
        style.setLabelBackgroundColor(Color.BLUE);
        style.setStrokeColor(Color.GRAY);
        style.setMarkerUrl("https://cdn4.iconfinder.com/data/icons/medical-soft-1/512/map_marker_pin_pointer_navigation_location_point_position-128.png");
        this.mapview.setStyle("56c3426202275a0b00fb00b9", style);
    }

    public void setBottomMargin() {
        this.mapview.setBottomMargin(60);
    }

    public void setTopMargin() {
        this.mapview.setTopMargin(60);
    }

    public void resetMargin() {
        this.mapview.setBottomMargin(0);
        this.mapview.setTopMargin(0);
    }

    public void getFloor() {
        Log.i("Floor", "" + this.mapview.getFloor());
    }

    public void getZoom() {
        Log.i("Zoom", ""+this.mapview.getZoom());
    }

    public void getUserPosition() {
        Log.i("UserPosition", ""+this.mapview.getUserPosition());
    }

    public void getCenter() {
        Log.i("Center", ""+this.mapview.getCenter());
    }

    @Override
    public void onZoomEnd(Integer zoom) {
        Log.i("Zoomend", "" + zoom);
    }

    @Override
    public void onClick(MWZLatLon latlon) {
        Log.i("OnClick", "" + latlon);
    }

    @Override
    public void onContextMenu(MWZLatLon latlon) {
        Log.i("OnContextMenu", "" + latlon);
    }

    @Override
    public void onFloorChange(Integer floor) {
        Log.i("OnFloorChange", "" + floor);
    }

    @Override
    public void onFloorsChange(Integer[] floors) {
        Log.i("OnFloorsChange", "floors");
    }

    @Override
    public void onPlaceClick(MWZPlace place) {
        Log.i("OnPlaceClick", "" + place);
    }

    @Override
    public void onVenueClick(MWZVenue venue) {
        Log.i("OnVenueClick", "" + venue);
    }

    @Override
    public void onMarkerClick(MWZPosition position) {
        Log.i("OnMarkerClick", "" + position);
    }

    @Override
    public void onMoveEnd(MWZLatLon latlon) {
        Log.i("OnMoveEnd", "" + latlon);
    }

    @Override
    public void onUserPositionChange(MWZMeasurement measurement) {
        Log.i("OnUserPositionChange", "" + measurement);
    }

    @Override
    public void onFollowUserModeChange(boolean followUserMode) {
        Log.i("OnFollowUserModeChange", "" + followUserMode);
    }

    @Override
    public void onDirectionsStart(String info) {
        Log.i("OnDirectionsStart", "" + info);
    }

    @Override
    public void onDirectionsStop(String info) {
        Log.i("OnDirectionsStop", "" + info);
    }

    @Override
    public void onMonitoredUuidsChange(String[] uuids) {
        Log.i("OnMonitoredUuidsChange", "" + uuids);
    }
}