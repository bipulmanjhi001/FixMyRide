package com.ust.fixmyride.map;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ust.fixmyride.R;
import com.ust.fixmyride.carservicepack.Car_Wash_Service_Pack;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.CustomProgressDialog;
import com.ust.fixmyride.model.ForAllDrawerFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {
    com.ust.fixmyride.model.FooterBarLayout mapcontinue;
    private GoogleMap map;
    CustomProgressDialog progressDialog;
    Location location;
    Double latitude,longitude;
    String address;
    com.ust.fixmyride.model.CircularImageView mapfab;
    ImageView getaddressonclick;
    String usersource;
    PlacesTask placesTask;
    ParserTask parserTask;
    public String latituded;
    public String longituded;
    com.ust.fixmyride.map.CUstomAutoCompleteText cUstomAutoCompleteText;
    private CoordinatorLayout coordinatorlayout;

    private Boolean isInternetPresent = false;

    private ConnectionDetector cd;
    boolean gpsEnabled;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.map_coordiaorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        mapcontinue = (com.ust.fixmyride.model.FooterBarLayout) findViewById(R.id.mapcontinue);
        mapcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, Car_Wash_Service_Pack.class);
                startActivity(intent);
                MapsActivity.this.finish();
            }
        });
        mapfab = (com.ust.fixmyride.model.CircularImageView) findViewById(R.id.mapfab);
        mapfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });

        getaddressonclick = (ImageView) findViewById(R.id.getaddressonclick);

        if(isInternetPresent && gpsEnabled) {

        //============== Adding Map=================//

        progressDialog = new CustomProgressDialog(MapsActivity.this,R.style.MyTheme);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
            // Getting Google Play availability status
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
            // Showing status
            if (status != ConnectionResult.SUCCESS) {
                // Google Play Services are not available
                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();
            } else {
                // Google Play Services are available
                // Getting reference to the SupportMapFragment of activity_main.xml
                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                // Enabling MyLocation Layer of Google Map
                try {
                    // Getting longitude of the current location
                    map.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setIndoorLevelPickerEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setRotateGesturesEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setScrollGesturesEnabled(true);
                map.setTrafficEnabled(true);
                map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        LatLng dragPosition = marker.getPosition();
                        double dragLat = dragPosition.latitude;
                        double dragLong = dragPosition.longitude;
                        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
                        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_SHORT).show();
                    }

                });

                map.setOnMyLocationChangeListener(myLocationChangeListener);
                // Getting LocationManager object from System Service LOCATION_SERVICE
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);
                // Getting Current Location
                try {
                    location = locationManager.getLastKnownLocation(provider);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }else{
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar.make(coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(14);

            snackbarActionTextView.setTextColor(Color.RED);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(1);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
        getaddressonclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUstomAutoCompleteText.setText("");
            }
        });

     /*******************************************Places Api***************************************************/
        cUstomAutoCompleteText = (com.ust.fixmyride.map.CUstomAutoCompleteText) findViewById(R.id.setaddressmap);
        cUstomAutoCompleteText.setThreshold(1);


        cUstomAutoCompleteText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSource(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (cUstomAutoCompleteText.isPerformingCompletion()) {
                    getValues();
                    setHideSoftKeyboard(cUstomAutoCompleteText);
                }
            }

        });
        }
    //----------------------------------------------------For get places names using google api----------------------------------------------------------
    private void getSource(CharSequence sd) {
        placesTask = new PlacesTask();
        placesTask.execute(sd.toString());
    }

    //--------------------------------------------------------For get source values-------------------------------------------------------------------
    private void getValues() {
        usersource = cUstomAutoCompleteText.getText().toString();
        getLocationFromAddress(usersource);
    }
    //----------------------------------------------------For hide Keyboard(not in used)---------------------------------------------------------
    private void setHideSoftKeyboard(CUstomAutoCompleteText editText) {
        if (cUstomAutoCompleteText.isPerformingCompletion()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

//-----------------------------------------------Fatching data from google location Api-----------------------------------------------------------------------------

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";
            String key = "key=AIzaSyBbYaKx_pRwVLuCnWXTg9mg7KeoRer5EXs";
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            // place type to be searched
            String types = "types=geocode";
            // Sensor enabled
            String sensor = "sensor=false";
            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJsonParse placeJsonParser = new PlaceJsonParse();
            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);


            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    view.setBackgroundColor(Color.WHITE);
                    return view;
                }
            };

            // Setting the adapter
            cUstomAutoCompleteText.setAdapter(adapter);
        }
    }

    /******************************************Update current location*********************************************************/
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            map.clear();

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
           Marker mMarker = map.addMarker(new MarkerOptions()
                   .position(loc)
                   .title(address)
                   .draggable(true)
                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin)));

            if (map != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
            try {

                Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.isEmpty()) {
                } else {
                    if (addresses.size() > 0) {
                        address = (addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    };

    //------------------------------------------------------------------Getting lat and longitude for data-------------------------------------------------------------------------
    public GeoPoint getLocationFromAddress(String usersource) {

        Geocoder coder = new Geocoder(MapsActivity.this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(usersource, 1);
            if (address == null) {
                return null;

            } else {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                        (int) (location.getLongitude() * 1E6));

                latituded = Location.convert(p1.getLatitude(), Location.FORMAT_DEGREES);

                longituded = Location.convert(p1.getLongitude(), Location.FORMAT_DEGREES);

                LatLng point = new LatLng(Double.parseDouble(latituded), Double.parseDouble(longituded));

                // Drawing the marker at the coordinates
                drawMarker(point);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }
    private void drawMarker(LatLng point){
        // Clears all the existing coordinates
         map.clear();

        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Setting title for the InfoWindow
        markerOptions.title("Position");

        // Setting InfoWindow contents
        markerOptions.snippet("Latitude:"+point.latitude+",Longitude"+point.longitude);

        // Adding marker on the Google Map
        map.addMarker(markerOptions);

        // Moving CameraPosition to the user input coordinates
        map.moveCamera(CameraUpdateFactory.newLatLng(point));

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
