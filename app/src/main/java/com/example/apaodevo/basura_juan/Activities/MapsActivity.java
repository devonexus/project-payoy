package com.example.apaodevo.basura_juan.Activities;

import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private String BIN_LOCATION_URL = "http://basurajuan.x10host.com/bin-location.php";
   // private double setLatitude = 10.262542, setLongitude = 123.952021;/*This is static longitude*/
    String jsonLatitude, jsonLongitude;
    double realLat, realLong;
    double lati, longitude;
    public static GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps) ;

        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, BIN_LOCATION_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonLatitude = response.getString(Keys.TAG_BIN_LATITUDE);
                            jsonLongitude = response.getString(Keys.TAG_BIN_LONGITUDE);
                            // lati = Double.parseDouble(jsonLatitude);
                            // longitude = Double.parseDouble(jsonLongitude);
                          //   globalData = (GlobalData) getApplicationContext();
                          //   globalData.setLatitude(jsonLatitude);
                          //   globalData.setLongitude(jsonLongitude);
                            Toast.makeText(getApplicationContext(), jsonLatitude+" Longitude "+jsonLongitude, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //getCoordinates(setLatitude, setLongitude);
        LatLng bin_location = new LatLng(10.262542, 123.952021);
//        Toast.makeText(getApplicationContext(), "DATA "+globalData.getLatitude()+" "+globalData.getLongitude(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), jsonLatitude + " Sample" + jsonLongitude, Toast.LENGTH_SHORT).show();

        mMap.addMarker(new MarkerOptions().position(bin_location).title("BasuraJuan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bin_location));
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
        //error
    }
}
