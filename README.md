# Mapwize

This is the Mapwize Android SDK, version 1.x.

It allows you to display and interact with Mapwize venue maps.

## Requirements

Android API Level 15 (Android 4.0.3) or higher.

## Installation

Add in build.gradle

	repositories {
		maven {
			url "https://jitpack.io"
		}
	}

and 

	dependencies {
		compile 'com.github.Mapwize:mapwize-android-sdk:X.X.X'
	}

substitute x.x.x with ultimate version released

## Example application

An example application is provided in this repository, inside de example directory.

The menu lists most of the possible actions that you can do with the map.

## Documentation

### Display the map
To display the Mapwize app, add it in your layout

	<io.mapwize.mapwize.MWZMapView
    	    android:id="@+id/mwzview"
        	android:layout_width="match_parent"
	        android:layout_height="match_parent"
    	    custom:center_latitude="51.508653"
        	custom:center_longitude="-0.124975"
	        custom:zoom="15"
    	    custom:floor="1"
        	custom:apikey="YOUR API KEY"
	        /> 
	        
### Map options
Options are defined using the class MWZMapOptions. The following options are available:

- apiKey : [String] must be provided for the map to load. It can be obtained from the Mapwize administration interface. If you don't have any, contact us.
- maxBounds : [MWZLatLonBounds] region users are allowed to navigate in (default: entire world).
- center: [MWZLatLon] coordiantes of the center of the map at start-up (default: 0,0).
- zoom : [int] integer between 0 and 21 (default 0).
- minZoom: [int] optional minimum zoom allowed by the map, usefull to limit the visible area.
- floor : [int] integer representing the desired floor of the building (default 0).
- isLocationEnabled : [boolean] boolean defining if the GPS should be started and the user position displayed (default: true).
- isBeaconsEnabled : [BOOL] boolean defining if the iBeacon scanner should be turned on (default: false).
- accessKey: [String] optional accessKey to be used during map load to be sure that access is granted to desired buildings at first map display.
- language: [String] optional preferred language for the map. Used to display all venues supporting that language.

 
### Moving the map
Once the map loaded, you can use the following functions on the map instance:

	public void fitBounds(MWZLatLonBounds bounds)
	public void centerOnCoordinates(Double lat, Double lon, Integer floor, Integer zoom)
	public void setZoom(Integer zoom)
	public void setFloor(Integer floor)
	public void centerOnVenue(MWZVenue venue)
	public void centerOnVenue(String id)
	public void centerOnPlace(MWZPlace place)
	public void centerOnPlace(String id)
	public void centerOnUser(Integer zoom)

### Getting map state

	public Integer getFloor() - returns the floor currently displayed.
	public Integer[] getFloors() - returns the list of floors available at the current viewing position.
	public Integer getZoom() - returns the current zoom level.

### User position

The followUserMode defines if the map should move when the user is moving.

	public boolean getFollowUserMode()
	public void setFollowUserMode(boolean follow)

You can get/set the user position using the following methods. For a complete guide on the user position measurement principle, please refer to the [Mapwize.js documentation](https://github.com/Mapwize/mapwize.js-dist/blob/master/doc/doc.md).

	public MWZMeasurement getUserPosition()
	public void setUserPosition(Double latitude, Double longitude, Integer floor)
	public void setUserPosition(Double latitude, Double longitude, Integer floor, Integer accuracy)
	public void newUserPositionMeasurement(MWZMeasurement measurement) 
	public void unlockUserPosition()

If you set isLocationEnabled=false in the map options, you can control the location using the functions

	public void startLocation(boolean useBeacon)
	public void stopLocation()

### User heading

When a compass is available, it can be interesting to display the direction the user is looking. To do so, the method setUserHeading can be used, giving it an angle in degree. Example if the user is looking south:

	public void setUserHeading(Double heading)
	
To remove the display of the compass, simply set the angle to null.

In the example app, you will find an example of code to listen to compass changes and display them on the map.

### Using Mapwize URLs

You can load Mapwize URLs using the following command. For a complete documentation please refer to the [Mapwize URL Scheme](https://github.com/Mapwize/mapwize-url-scheme).

	public void loadURL(String url)

### Adding markers

You can add markers on top of the map.

	public void addMarker(Double latitude, Double longitude, Integer floor)
	public void addMarker(String placeId)
	public void removeMarkers()

### Showing directions

To show directions, you can use 

	public void showDirections(MWZPosition from, MWZPosition to)

The direction will be automatically computed and displayed. The event onDirectionsStart will be fired once the direction is loaded.

To remove the directions from the map, use

	public void stopDirections()

### Accessing private venues

To access a private venue, enter the related accessKey using:

	public void access(String accesskey)

Access keys are managed in the Mapwize administration interface and are provided by venue managers.

### Listening to events

You can listen for events emitted by the map using the MWZMapViewListener. To do so, set the listener using

    map.setListener(this);

then implement the following methods

    void onMapLoad();
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
    
onReceivedError will be triggered in case of unavilable internet connection or in case one of the interaction with the map failed. 

### Editing place style

The display style of places can be changed dynamically within the SDK. To do so, you can use

	public void setStyle(String placeId,MWZPlaceStyle style)

### Setting top and bottom margins

It often happens that part of the map is hidden by banners or controls on the top or on the bottom. For example, if you display a banner to show the details of the place you just clicked on, it's better to display the banner on top of the map than having to resize the map.

However, you want to make sure that the Mapwize controls are always visible, like the followUserMode button and the floor selector. Also, that if you make a fitBounds, the area will be completely in the visible part of the map.

For this purpose, you can set a top and a bottom margin on the map. We garantee that nothing important will be displayed in those margin areas.

To set the margins in pixel:

	public void setBottomMargin(Integer margin)
	public void setTopMargin(Integer margin)

### Getting venues and places

Venues and places have properties name and alias that allow to identify them in a more human-readable manner than the ID.
Venue names and alias are unique throughout the Mapwize platform. Place names and alias are unique inside a venue.

To get a venue or place object from an id, a name or an alias, you can use the following methods. At each request, we will first try to get the object from the map cache. If the object is not available, a request to the server will be done.

The methods are asynchronous and uses a completionHandler block.

	public void getVenueWithId (String venueId, VenueCallbackInterface callback)
	public void getVenueWithName (String venueName, VenueCallbackInterface callback)
	public void getVenueWithAlias (String venueAlias, VenueCallbackInterface callback)

	public void getPlaceWithId (String placeId, PlaceCallbackInterface callback)
	public void getPlaceWithName (String placeName, String venueId, PlaceCallbackInterface callback)
	public void getPlaceWithAlias (String placeAlias, String venueId, PlaceCallbackInterface callback)

### Refreshing the cache

To prevent too many network requests while browsing the map, the SDK keeps a cache of some data it already downloaded.

The Time To Live of the cache is 5 minutes.

If you want to force the map to refresh the cache and update itself, you can call the refresh method anytime.

	public void refresh()

### Proguard configuration

If you are using Proguard, add the following lines in yours proguard rules

	-keepclassmembers class io.mapwize.mapwize.* {*;}
	-keepattributes *Annotation*,EnclosingMethod,Signature
 	-keepnames class com.fasterxml.jackson.** { *; }
  	-dontwarn com.fasterxml.jackson.databind.**

## Contact

If you need any help, please contact us at contact@mapwize.io

## License

Mapwize is available under the MIT license. See the LICENSE file for more info.
