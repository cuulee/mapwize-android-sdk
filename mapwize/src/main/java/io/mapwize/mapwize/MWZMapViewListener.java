package io.mapwize.mapwize;

public interface MWZMapViewListener {

    void onReceivedError(String error);
    void onZoomEnd(Integer zoom);
    void onClick(MWZLatLon latlon);
    void onContextMenu(MWZLatLon latlon);
    void onFloorChange(Integer floor);
    void onFloorsChange(Integer[] floors);
    void onPlaceClick(MWZPlace place);
    void onVenueClick(MWZVenue venue);
    void onMarkerClick(MWZPosition position);
    void onMoveEnd(MWZLatLon latlon);
    void onUserPositionChange(MWZMeasurement measurement);
    void onFollowUserModeChange(boolean followUserMode);
    void onDirectionsStart(String info);
    void onDirectionsStop(String info);
    void onMonitoredUuidsChange(String[] uuids);

}
