# [deprecated] Mapwize Android SDK using Webview rendering

This is the Mapwize Android SDK, version 2.x.

It allows you to display and interact with Mapwize venue maps.

## New SDK available

**This SDK remains functional and fully supported by Mapwize. However, a new SDK built on Mapbox GL is now available and all developers are invited to use it instead. Details are available at [docs.mapwize.io](https://docs.mapwize.io).**

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

An example application is provided in this repository, inside the example directory.

The menu lists most of the possible actions that you can do with the map.

## Documentation

### Setup api key

In order to use the map and the api manager, you have to intialize the MWZAccountManager with your api key. It can be obtained from the Mapwize Studio interface. If you don't have any, contact us at support@mapwize.io.

```java
MWZAccountManager.start(this, "YOUR API KEY");
```

If you don't want to use the Api manager and only the map, you can pass the api key in the xml layout as shown in the next part.

### Display the map
To display the Mapwize map, add it in your layout

```xml
<io.mapwize.mapwize.MWZMapView
android:id="@+id/mwzview"
android:layout_width="match_parent"
android:layout_height="match_parent"
app:center_latitude="51.508653"
app:center_longitude="-0.124975"
app:zoom="15"
app:floor="1.0"
/> 
```

The following custom attributes can be use :

```xml
<!-- Access -->
<attr name="accesskey" format="string"/>

<!-- Language -->
<attr name="language" format="string"/>

<!-- Controller display -->
<attr name="showUserPositionControl" format="boolean"/>
<attr name="displayFloorControl" format="boolean"/>
<attr name="mainColor" format="string"/>

<!-- Location -->
<attr name="isLocationEnabled" format="boolean"/>
<attr name="isBeaconsEnabled" format="boolean"/>

<!-- Coordinates -->
<attr name="center_latitude" format="float"/>
<attr name="center_longitude" format="float"/>
<attr name="zoom" format="integer"/>
<attr name="floor" format="float"/>
<attr name="minZoom" format="integer"/>

<!-- Bounds -->
<attr name="maxBounds_latitudeMin" format="float"/>
<attr name="maxBounds_latitudeMax" format="float"/>
<attr name="maxBounds_longitudeMin" format="float"/>
<attr name="maxBounds_longitudeMax" format="float"/>
<attr name="bounds_latitudeMin" format="float"/>
<attr name="bounds_latitudeMax" format="float"/>
<attr name="bounds_longitudeMin" format="float"/>
<attr name="bounds_longitudeMax" format="float"/>

<!-- Custom marker -->
<attr name="iconUrl" format="string"/>
<attr name="x_iconSize" format="integer"/>
<attr name="y_iconSize" format="integer"/>
<attr name="x_iconAnchor" format="integer"/>
<attr name="y_iconAnchor" format="integer"/>
```

### Life cycle
To avoid any memory leak, and in particular if you are using the iBeacon positioning solution within the Mapwize SDK, 
you need to call onDestroy on the mapview when its activity is destroyed. 

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    mapview.onDestroy();
}
```

### Map options
Options are defined using the class MWZMapOptions. The following options are available:

- apiKey : [String] must be provided for the map to load, if it was not previously set in the MWZAccountManager. It can be obtained from the Mapwize Studio interface. If you don't have any, contact us at support@mapwize.io.
- bounds : [MWZLatLonBounds] region that will be display when map is loading
- maxBounds : [MWZLatLonBounds] region users are allowed to navigate in (default: entire world).
- center: [MWZLatLon] coordiantes of the center of the map at start-up (default: 0,0).
- zoom : [int] integer between 0 and 21 (default 0).
- minZoom: [int] optional minimum zoom allowed by the map, usefull to limit the visible area.
- floor : [double] double representing the desired floor of the building (default 0.0).
- isLocationEnabled : [boolean] boolean defining if the GPS should be started and the user position displayed (default: true).
- isBeaconsEnabled : [BOOL] boolean defining if the iBeacon scanner should be turned on (default: false).
- accessKey: [String] optional accessKey to be used during map load to be sure that access is granted to desired buildings at first map display.
- language: [String] optional preferred language for the map. Used to display all venues supporting that language.
- mainColor: [String] optional mainColor will be use to replace mapwize color in UI component as floor controller or direction.


### Moving the map
Once the map loaded, you can use the following functions on the map instance:

```java
public void fitBounds(MWZBounds bounds) 
public void centerOnCoordinates()
public void centerOnCoordinatesWithFloor()
public void setFloor(Double floor)
public void setZoom(Integer zoom)
public void centerOnVenue(MWZVenue venue)
public void centerOnVenue(String id)
public void centerOnPlace(MWZPlace place)
public void centerOnPlace(String id)
public void centerOnUser(Integer zoom)
```

### Getting map state

```java
public Integer getZoom()
public MWZCoordinate getCenter()
public Double getFloor()
public Double[] getFloors()
```

### User position

You can get/set the user position using the following methods. For a complete guide on the user position measurement principle, please refer to the [Mapwize.js documentation](https://github.com/Mapwize/mapwize.js-dist/blob/master/doc/doc.md).

```java
public MWZCoordinate getUserPosition()
public void newUserPositionMeasurement(MWZMeasurement measurement)
public void removeUserPosition()
public void setUserPosition(MWZUserPosition userPosition)
public void unlockUserPosition()
```
The followUserMode defines if the map should move when the user is moving.

```java
public boolean getFollowUserMode()
public void setFollowUserMode(boolean follow)
```

If you set isLocationEnabled=false in the map options, you can control the location using the functions
```java
public void startLocation(boolean useBeacon)
public void stopLocation()
```
### User heading

When a compass is available, it can be interesting to display the direction the user is looking at. To do so, the method setUserHeading can be used, giving it an angle in degree. To remove the display of the compass, simply set the angle to null.
```java
public void setUserHeading(Double heading)
```	

In the example app, you will find an example of code to listen to compass changes and display it on the map.

### Using Mapwize URLs

You can load Mapwize URLs using the following command. For a complete documentation please refer to the [Mapwize URL Scheme](https://github.com/Mapwize/mapwize-url-scheme).
```java
public void loadURL(String url)
```
### Adding markers

You can add markers on top of the map. A marker with floor null will be displayed on all floors

```java
public void addMarker(MWZCoordinate coordinate)
@Deprecated
public void addMarker(Double latitude, Double longitude, Double floor)
public void addMarker(String placeId)
public void removeMarkers()
```

You can change the default mapwize marker icon by passing options to the map. See map options.

### Controlling places display

You can promote places to increase their display priority.

```java
public void setPromotedPlaces(List<MWZPlace> places)
public void setPromotedPlacesWithIds(List<String> placeIds)
public void addPromotedPlace(MWZPlace place)
public void addPromotedPlaceWithId(String placeId)
public void addPromotedPlaces(List<MWZPlace> places)
public void addPromotedPlacesWithIds(List<String> placeIds)
public void removePromotedPlace(MWZPlace place)
public void removePromotedPlaceWithId(String placeId)
```

You can ignore place to make them not visible

```java
public void addIgnoredPlace(MWZPlace place)
public void addIgnoredPlaceWithId(String identifier)
public void removeIgnoredPlace(MWZPlace place)
public void removeIgnoredPlaceWithId(String identifier)
public void setIgnoredPlaces(List<MWZPlace> places)
public void setIgnoredPlacesWithIds(List<String> placeIds)
```

You can add places from your own data using this method. In order to make it works, you have to create a Mapwize formatted place object from your data and pass all the place as argument
```java
public void addExternalPlaces(List<MWZPlace> places)
```

The minimum attribute that should be specified to work propertly are the following :

MWZPlace.geometry
MWZPlace.venueId
MWZPlace.floor
MWZPlace.translations

In order to use promote and ignore methods, you have to specify an uniq id (MWZPlace.identifier)

### Universes

You can change the universe of a venue with the following methods.

```java
public void setUniverseForVenue(String universeId, MWZVenue venue)
public void setUniverseForVenue(MWZUniverse universe, MWZVenue venue)
```



### Showing directions

To start and stop directions you can you the following methods.

```java
public void startDirections(MWZDirection direction)
public void stopDirections()
```

The direction will be displayed and the event didStartDirections will be fired once the direction is loaded.
See the ApiManager part of this documentation to learn more about getting directions.

### Listening to events

You can listen for events emitted by the map using the MWZMapViewListener. To do so, set the listener using

```java
map.setListener(this);
```

then implement the following methods

```java
void onMapLoad();
void onMapLoaded();
void onReceivedError(String error);
void onZoomEnd(Integer zoom);
void onClick(MWZCoordinate latlon);
void onContextMenu(MWZCoordinate latlon);
void onFloorChange(Double floor);
void onFloorsChange(Double[] floors);
void onPlaceClick(MWZPlace place);
void onVenueClick(MWZVenue venue);
void onMarkerClick(MWZCoordinate position);
void onMoveEnd(MWZCoordinate latlon);
void onUserPositionChange(MWZUserPosition userPosition);
void onFollowUserModeChange(boolean followUserMode);
void onDirectionsStart(String info);
void onDirectionsStop(String info);
void onMonitoredUuidsChange(String[] uuids);
void onMissingPermission(String accessFineLocation);
void onJavascriptConsoleCallback(ConsoleMessage message);
```

onReveivedError will be triggered in case of unavilable internet connection or in case one of the interaction with the map failed.

### Editing place style

The display style of places can be changed dynamically within the SDK. To do so, you can use

```java
public void setStyle(String placeId,MWZStyle style)
```

### Setting top and bottom margins

It often happens that part of the map is hidden by banners or controls on the top or on the bottom. For example, if you display a banner to show the details of the place you just clicked on, it's better to display the banner on top of the map than having to resize the map.

However, you want to make sure that the Mapwize controls are always visible, like the followUserMode button and the floor selector. Also, that if you make a fitBounds, the area will be completely in the visible part of the map.

For this purpose, you can set a top and a bottom margin on the map. We garantee that nothing important will be displayed in those margin areas.

To set the margins in pixel:

```java
public void setBottomMargin(Integer margin)
public void setTopMargin(Integer margin)
```

### Refreshing the cache

To prevent too many network requests while browsing the map, the SDK keeps a cache of some data it already downloaded.
The Time To Live of the cache is 5 minutes.

If you want to force the map to refresh the cache and update itself, you can call the refresh method anytime. 
This method should be call each time a new access is granted by MWZApi.getAccess("mynewaccesskey")

```java
public void refresh()
```

## API Documentation

Mapwize objects and data can be retrieved without instantiating the map thanks to the MWZApi instance.

Before the ApiManager can be used, the api key need to be provided to the MWZAccountManager:
```java
MWZAccountManager.start(this, "YOUR API KEY");
```

### Api callback interface 

All api requests use the same callback interface. The type of the returned object depends on the request.
onFailure is called if an error occured
onSuccess is called in others cases

```java
public interface MWZCallback<T> {
void onSuccess(T object);
void onFailure(Throwable t);
}
```

### Accessing private venues

To access a private venue, enter the related accessKey using:
```java
public static void getAccess(String access, final MWZCallback<Map<String, Object>[]> callback)
```
Access keys are managed in the Mapwize Studio interface and are provided by venue managers.

Getting an access using api give the access in the map at the same time. If the map is already loaded when the call is done, you have to refresh the map.

### Getting venues

Venues have properties name and alias that allow to identify them in a more human-readable manner than the ID. Venue names and alias are unique throughout the Mapwize platform.

```java
public static void getVenues(Map<String,String> options, final MWZCallback<List<MWZVenue>> callback)
public static void getVenuesWithOrganizationId(@NonNull String organizationId, @NonNull final MWZCallback<List<MWZVenue>> callback)
public static void getVenueWithId(String id, final MWZCallback<MWZVenue> callback)
public static void getVenueWithName(String name, final MWZCallback<MWZVenue> callback)
public static void getVenueWithAlias(String alias, final MWZCallback<MWZVenue> callback)
```

### Getting places

Place have id, name and alias like venues. ID is unique throughout the mapwize platform but name and alias are unique throughout each venues

```java
public static void getPlaces(Map<String,String> options, final MWZCallback<List<MWZPlace>> callback)
public static void getPlaceWithId(String id, final MWZCallback<MWZPlace> callback)
public static void getPlaceWithName(String name, MWZVenue venue, final MWZCallback<MWZPlace> callback) 
public static void getPlaceWithAlias(String alias, MWZVenue venue, final MWZCallback<MWZPlace> callback)
public static void getPlacesForVenue(MWZVenue venue, final MWZCallback<List<MWZPlace>> callback)
```

### Getting placeLists

Placelist have the same behavior than places. You can use the following methods.

```java
public static void getPlacelists(Map<String,String> options, final MWZCallback<List<MWZPlaceList>> callback)
public static void getPlaceListWithId(String id, final MWZCallback<MWZPlaceList> callback)
public static void getPlaceListWithName(String name, MWZVenue venue, final MWZCallback<MWZPlaceList> callback)
public static void getPlaceListWithAlias(String alias, MWZVenue venue, final MWZCallback<MWZPlaceList> callback)
public static void getPlaceListsForVenue(MWZVenue venue, final MWZCallback<List<MWZPlaceList>> callback)
```
Placelist is a group of places so you may want to retrieve all the place object contained in th place list.

```java
public static void getPlacesForPlaceList(MWZPlaceList placeList, final MWZCallback<List<MWZPlace>> callback)
```

### Getting directions

To request a direction from one place to another, possibly with intermediate waypoints, the following method can be used:

```java
public static void getDirection(MWZDirectionPoint from, MWZDirectionPoint to, List<MWZDirectionPoint> waypoints, MWZDirectionOptions options, final MWZCallback<MWZDirection> callback)
public static void getDirection(MWZDirectionPoint from, List<MWZDirectionPoint> to, List<MWZDirectionPoint> waypoints, MWZDirectionOptions options, final MWZCallback<MWZDirection> callback)
```

- from (MWZPlace, MWZCoordinate,MWZCoordinateInVenue, MWZUserPosition) : The starting point of the direction
- to (MWZPlace, MWZPlaceList, MWZCoordinate,MWZCoordinateInVenue, MWZUserPosition) : Can be a MWZDirectionPoint or a List<MWZDirectionPoint>. In the first case, the to object will be the destination point. In the second case, the nearest point of the list will be use as destination point.
- waypoint (MWZPlace, MWZCoordinate,MWZCoordinateInVenue, MWZUserPosition) : A list of intermediate point.

Direction options :
- isAccessible (boolean) :  Boolean defining if the direction should avoid inaccessible way for someone with reduced mobility.
- waypointOptimize (boolean) : Boolean defining if the direction engine should optimize waypoint order to return the shortest way. 


### Search engine

MWZVenue, MWZPlace and MWZPlaceList are searchable object. They can be retrieve by the search method.

```java
public static void search(MWZSearchParams searchParams, final MWZCallback<List<MWZSearchableObject>> callback)
```

The search parameters are built as follow

```java
String query;           // The query string used for the search
String venueId;         // If specify, restrict the search to this venue
String organizationId;  // If specify, restrict the search to this organization
String universeId;      // If specify, restrict the search to this universe
```

## Proguard configuration

If you are using Proguard, add the following lines in yours proguard rules

-keepclassmembers class io.mapwize.mapwize.* {*;}
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**

## Contact

If you need any help, please contact us at contact@mapwize.io

## License

Mapwize Android SDK is available under the MIT license. See the LICENSE file for more info.
