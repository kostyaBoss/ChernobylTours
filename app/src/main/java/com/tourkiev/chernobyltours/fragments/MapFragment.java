package com.tourkiev.chernobyltours.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tourkiev.chernobyltours.ModelMarker;
import com.tourkiev.chernobyltours.R;
import com.tourkiev.chernobyltours.activities.DisplayPointActivity;
import com.tourkiev.chernobyltours.helpers.GPSTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import static com.tourkiev.chernobyltours.Constants.EXTRAS_HASH_MAP;
import static com.tourkiev.chernobyltours.Constants.EXTRAS_TITLE;
import static com.tourkiev.chernobyltours.Constants.PREFS_TRACK_GEO;
import static com.tourkiev.chernobyltours.ModelMarker.convertToBitmap;
import static com.tourkiev.chernobyltours.R.string.nearest_point;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    GoogleMap mMap;
    Location currentLocation;
    GPSTracker gpsTracker;
    ArrayList<ModelMarker> modelMarkerArrayList;  // arrayList of gmap markers
    ArrayList<MarkerOptions> googleMapArrayList; // arrayList of models
    TextView nearestMarkerTextView;
    MarkerOptions markerOptions;
    RelativeLayout bottomLayout;
    HashMap<String, ModelMarker> hashMap;
    Boolean trackGeo;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        gpsTracker = new GPSTracker(getContext());
        modelMarkerArrayList = new ArrayList<>();
        googleMapArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        // rel layout with whole elements
        bottomLayout = view.findViewById(R.id.nearest_point_layout);
        nearestMarkerTextView = view.findViewById(R.id.nearest_point);
        // animate camera by click to specific marker
        bottomLayout.setOnClickListener(this);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


        return view;
    }

    private ArrayList<ModelMarker> setGeoInMap(ArrayList<ModelMarker> modelMakerArrayList) {// method returns an array of models of markers


        // first points check point
        modelMarkerArrayList.add(new ModelMarker(50.45148595,
                30.52208215, (R.string.tour_starting_point),
                (R.string.tour_starting_point_description),
                (R.drawable.data),
                50,
                (R.drawable.pin_1),
                R.raw.tour_starting_point));// Tour starting point
        modelMarkerArrayList.add(new ModelMarker(50.45046123,
                30.5239436,
                (R.string.kreschatic_street),
                (R.string.kreshatik_street_description),
                (R.drawable.data1),
                75,
                (R.drawable.pin_2),
                R.raw.kres_street));// Kreschatik street
        modelMarkerArrayList.add(new ModelMarker(50.45846028,
                30.52656412,
                (R.string.podol),
                (R.string.podol_description),
                (R.drawable.data2),
                100,
                (R.drawable.pin_3),
                R.raw.podol));// Podol
        modelMarkerArrayList.add(new ModelMarker(50.60230732,
                30.45289993,
                (R.string.novi_petrivtsi), (R.string.novi_petrivtsi_description),
                (R.drawable.data3),
                100,
                (R.drawable.pin_4),
                R.raw.novi));// Novi Petrivtsy
        modelMarkerArrayList.add(new ModelMarker(50.77636424,
                30.3228879,
                (R.string.dymer),
                (R.string.dymer_description),
                (R.drawable.data4),
                400,
                (R.drawable.pin_5),
                R.raw.dymer));// Dymer
        modelMarkerArrayList.add(new ModelMarker(50.80640933, 30.15102267,
                (R.string.katyuzhanka),
                (R.string.katyachanka_description),
                (R.drawable.data5),
                400,
                (R.drawable.pin_6),
                R.raw.kat));// Katyuzhanka
        modelMarkerArrayList.add(new ModelMarker(50.91928863,
                29.90091741,
                (R.string.ivankiv),
                (R.string.ivankiv_description),
                (R.drawable.data6),
                400,
                (R.drawable.pin_7),
                R.raw.ivankiv));// Ivankiv
        modelMarkerArrayList.add(new ModelMarker(51.11254837,
                30.12176514,
                (R.string.dityatki_check_point),
                (R.string.dityatki_check_point_description),
                (R.drawable.data7),
                500,
                (R.drawable.pin_8),
                R.raw.dit));// Dityatki Check Point
        modelMarkerArrayList.add(new ModelMarker(51.12256969,
                30.12187243, (R.string.thirty_k_zone),
                (R.string.thirty_k_zone_description),
                (R.drawable.data8),
                500,
                (R.drawable.pin_9),
                R.raw.thirty_k_zone));// 30k zone
        modelMarkerArrayList.add(new ModelMarker(51.253804,
                30.184443,
                (R.string.zalesye_village),
                (R.string.zalesye_village_description),
                (R.drawable.data9),
                350,
                (R.drawable.pin_10),
                R.raw.zal));// Zalesye Village
        // ten point check point
        modelMarkerArrayList.add(new ModelMarker(51.26497619,
                30.20884037,
                (R.string.chornobyl),
                (R.string.chornobyl_description),
                (R.drawable.data10),
                500,
                (R.drawable.pin_11),
                R.raw.chornobyl));// Chornobyl
        modelMarkerArrayList.add(new ModelMarker(51.27234674,
                30.22422016,
                (R.string.trumpeting_angel_of_chornobyl),
                (R.string.trumpeting_angel_of_chornobyl_description),
                (R.drawable.data11),
                300,
                (R.drawable.pin_12),
                R.raw.trumpeting));// Trumpeting Angel of Chernobyl
        modelMarkerArrayList.add(new ModelMarker(51.28024628,
                30.20818055,
                (R.string.monuments_to_liquidator),
                (R.string.monuments_to_liquidator_description),
                (R.drawable.data12),
                300,
                (R.drawable.pin_13),
                R.raw.monument));// Monument to the liquidators
        modelMarkerArrayList.add(new ModelMarker(51.28688467,
                30.20294622,
                (R.string.robots),
                (R.string.robots_description),
                (R.drawable.data13),
                250,
                (R.drawable.pin_14),
                R.raw.robots));// Robots
        modelMarkerArrayList.add(new ModelMarker(51.27269577,
                30.23734152,
                (R.string.elijah_church),
                (R.string.elijah_church_description),
                (R.drawable.data14),
                250,
                (R.drawable.pin_15),
                R.raw.st_elijah));// Elijah church
        modelMarkerArrayList.add(new ModelMarker(51.304720,
                30.064399,
                (R.string.radar_duga),
                (R.string.radar_duga_description),
                (R.drawable.data15),
                500,
                (R.drawable.pin_16),
                R.raw.radar_duga));// Radar duga
        modelMarkerArrayList.add(new ModelMarker(51.35342519,
                30.12482285,
                (R.string.kopachi_village),
                (R.string.kopachi_village_description),
                (R.drawable.data16),
                600,
                (R.drawable.pin_17),
                R.raw.kopachi));// Kopachi Village
        modelMarkerArrayList.add(new ModelMarker(51.37854448,
                30.11360049,
                (R.string.first_part_of_reactor),
                (R.string.first_part_of_reactor_description),
                (R.drawable.data17),
                300,
                (R.drawable.pin_18),
                R.raw.first_part_nuclear));// 1st Part nuclear Power Plant
        modelMarkerArrayList.add(new ModelMarker(51.39031566,
                30.0938648,
                (R.string.chernobyl_new_safe_confinement),
                (R.string.chernobyl_new_safe_confinement_description),
                (R.drawable.data18),
                300,
                (R.drawable.pin_19),
                R.raw.chernobyl_new_safe));// 19 Chernobyl New Safe Confinement
        modelMarkerArrayList.add(new ModelMarker(51.39129981,
                30.10875911,
                (R.string.second_part_power_part),
                (R.string.second_part_power_plant),
                (R.drawable.data19),
                250,
                (R.drawable.pin_20),
                R.raw.second_part_power_plant));// 19 Chernobyl New Safe Confinement
        // twenty point check point
        modelMarkerArrayList.add(new ModelMarker(51.39486798,
                30.06919384,
                (R.string.pripyat_town),
                (R.string.pripyat_town_descriptuion),
                (R.drawable.data20),
                400,
                (R.drawable.pin_21),
                R.raw.pripyat_town));//Pripyat town
        modelMarkerArrayList.add(new ModelMarker(51.40798684,
                30.06644726,
                (R.string.pripyat_river_point),
                (R.string.pripyat_river_point_description),
                (R.drawable.data21),
                300,
                (R.drawable.pin_22),
                R.raw.pripyat_river));//Pripyat river point
        modelMarkerArrayList.add(new ModelMarker(51.40666174,
                30.05779445,
                (R.string.centre),
                (R.string.centre_description),
                (R.drawable.data22),
                100,
                (R.drawable.pin_23),
                R.raw.centre));//Center
        modelMarkerArrayList.add(new ModelMarker(51.40762545,
                30.05620122,
                (R.string.ferris_wheel),
                (R.string.ferris_wheel_description),
                (R.drawable.data23),
                100,
                (R.drawable.pin_24),
                R.raw.ferris_wheel));//Ferris wheel
        modelMarkerArrayList.add(new ModelMarker(51.41031571,
                30.05469918,
                (R.string.stadium_avangard),
                (R.string.stadium_avangard_description),
                (R.drawable.data24),
                200,
                (R.drawable.pin_25),
                R.raw.stadium_avangard));//Stadium avangard
        modelMarkerArrayList.add(new ModelMarker(51.40670189,
                30.04939377,
                (R.string.swimming_pool),
                (R.string.swimming_pool_description),
                (R.drawable.data25),
                200,
                (R.drawable.pin_26),
                R.raw.swimming_pool));//Swimming pool
        modelMarkerArrayList.add(new ModelMarker(51.40233816,
                30.0425756,
                (R.string.jupiter_factory),
                (R.string.jupiter_factory_description),
                (R.drawable.data26),
                200,
                (R.drawable.pin_27),
                R.raw.jupiter_factory));//Jupiter factory
        modelMarkerArrayList.add(new ModelMarker(51.40227123,
                30.05153954,
                (R.string.police_station),
                (R.string.police_station_description),
                (R.drawable.data27),
                200,
                (R.drawable.pin_28),
                R.raw.police));//Police station

        return modelMarkerArrayList;


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent intent = new Intent(getContext(), DisplayPointActivity.class);
        intent.putExtra(EXTRAS_TITLE, marker.getTitle());
        intent.putExtra(EXTRAS_HASH_MAP, hashMap);

        startActivity(intent);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        setUpMap(mMap);

    }

    private void setUpMap(GoogleMap googleMap) {
        // Enable MyLocation Layer of Google Map
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // adding markers
        for (ModelMarker modelMarker : setGeoInMap(modelMarkerArrayList)) {
            // create marker
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(modelMarker.getLatitude(), modelMarker.getLongitude()))
                    .title(getString(modelMarker.getTitle()))
                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap((convertToBitmap(getContext(), modelMarker.getBitmapMarkerId())), 90, 122, false)));

            // add a maker
            googleMapArrayList.add(marker);

            // draw a radius
            drawCircle(marker.getPosition(), googleMap, modelMarker.getRadius());

            hashMap.put(marker.getTitle(), modelMarker); // link each marker with it's model
            googleMap.addMarker(marker);

            modelMarkerArrayList = null;
            System.gc();
        }

        modelMarkerArrayList = null;
        System.gc();

        googleMap.setMyLocationEnabled(true);


        //set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (gpsTracker != null) {
            // default value
            currentLocation = gpsTracker.getLocation();

            // Get latitude of the current location
            double latitude = currentLocation.getLatitude();

            // Get longitude of the current location
            double longitude = currentLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // set text into bottom text view for the first time
            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);

            markerOptions = getNearestMarker(googleMapArrayList, temp);
            setTextBottomTextView(markerOptions);

            // Show the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }


        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMap != null) {

                    // checking for geolocation tracking from settings
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    trackGeo = preferences.getBoolean(PREFS_TRACK_GEO, true);

                    if (trackGeo) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
                    }

                    // returns nearest marker
                    markerOptions = getNearestMarker(googleMapArrayList, location);
                    setTextBottomTextView(markerOptions);

                    // set text to the textView


                }
            }
        });


        //marker click listener
        googleMap.setOnMarkerClickListener(this);
        System.gc();
    }


    // draw a circle radius
    private void drawCircle(LatLng point, GoogleMap googleMap, double radius) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);

    }

    private MarkerOptions getNearestMarker(ArrayList<MarkerOptions> gMarkers, Location currentLocation) {


        HashMap<MarkerOptions, Float> hm = new HashMap<>();
        ArrayList<Float> distancesByFloat = new ArrayList<>();


        for (MarkerOptions markerOptions : gMarkers) {

            // arrayList of latitudes
            // temporary variable
            Location temp = new Location(markerOptions.getTitle());
            temp.setLatitude(markerOptions.getPosition().latitude);
            temp.setLongitude(markerOptions.getPosition().longitude);


            // hm by title and current distance to marker
            hm.put(markerOptions, currentLocation.distanceTo(temp));
            // arrayList of distances
            distancesByFloat.add(currentLocation.distanceTo(temp));
            // Log.d("Dist" + " " + markerOptions.getTitle(), markerOptions.getTitle()/*String.valueOf(currentLocation.distanceTo(temp))*/);
        }

        // sorting arrayList to get a float
        Collections.sort(distancesByFloat);

        MarkerOptions markerOptions = null;
        for (HashMap.Entry<MarkerOptions, Float> hashMap : hm.entrySet()) {

            if (Objects.equals(hashMap.getValue(), distancesByFloat.get(0))) {

                markerOptions = hashMap.getKey();
            }
        }
        // returning the nearest title
        assert markerOptions != null;
        // setting text into textview
        // make title bold


        return markerOptions;
    }

    private void setTextBottomTextView(MarkerOptions markerOptions) {
        SpannableString boldMarkerTitle = new SpannableString(getString(nearest_point) + " " + markerOptions.getTitle());
        boldMarkerTitle
                .setSpan(new StyleSpan(Typeface.BOLD),
                        getString(nearest_point).length() + 1, // plus one, because we have " "
                        getString(nearest_point).length() + 1 + markerOptions.getTitle().length(), // sum two length of.....
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nearestMarkerTextView.setText(boldMarkerTitle);
    }


    @Override
    public void onClick(View view) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(hashMap.get(markerOptions.getTitle()).getLatitude(),
                hashMap.get(markerOptions.getTitle()).getLongitude())), 16));
    }
}