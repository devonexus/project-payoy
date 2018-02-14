package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MapsActivity extends NavigationDrawerActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private String BIN_LOCATION_URL = "http://basurajuan.x10host.com/bin-location.php";
   // private double setLatitude = 10.262542, setLongitude = 123.952021;/*This is static longitude*/

    public static GlobalData globalData;

    int redActionButtonSize,leftMargin,rightMargin,topMargin,bottomMargin,redActionButtonContentSize,
            redActionButtonContentMargin,redActionMenuRadius,blueSubActionButtonSize,blueSubActionButtonContentMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_maps, null, false);
        globalData = (GlobalData) getApplicationContext();
        drawer.addView(contentView, 0);

        fab.setImageResource(R.drawable.floating_navigate_bin);
        fab.setVisibility(View.GONE);

        //getCoordinates();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up the large red button on the center right side
        // With custom button and content sizes and margins
        redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        leftMargin = getResources().getDimensionPixelOffset(R.dimen.red_action_button_margin);
        rightMargin = getResources().getDimensionPixelOffset(R.dimen.rightMargin);
        topMargin = getResources().getDimensionPixelOffset(R.dimen.topMargin);
        bottomMargin = getResources().getDimensionPixelOffset(R.dimen.red_action_button_margin);
        redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.bin_location_icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        FrameLayout.LayoutParams ContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        itemBuilder.setLayoutParams(ContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams conParams = new FrameLayout.LayoutParams(blueSubActionButtonSize,blueSubActionButtonSize);
        itemBuilder.setLayoutParams(conParams);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.floating_navigate_bin);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.deploy);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_action_register_bin);

        final SubActionButton sabNavigateBin = itemBuilder
                .setContentView(itemIcon1)
                .build();
        final SubActionButton sabDeployBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabRegisterBin = itemBuilder.setContentView(itemIcon3).build();

        //attach the sub buttons to the main button
        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabNavigateBin)
                .addSubActionView(sabDeployBin)
                .addSubActionView(sabRegisterBin)
                .attachTo(actionButton)
                .build();

        sabNavigateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceList.btSocket == null) {
                    startActivity(new Intent(getApplicationContext(), DeviceList.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), NavigateBin.class));
                }
            }
        });

        sabDeployBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
            }
        });
        sabRegisterBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterBin.class));
            }
        });
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionButton.setVisibility(View.INVISIBLE);
                sabDeployBin.setVisibility(View.INVISIBLE);
                sabNavigateBin.setVisibility(View.INVISIBLE);
                sabRegisterBin.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionButton.setVisibility(View.VISIBLE);
                sabDeployBin.setVisibility(View.VISIBLE);
                sabNavigateBin.setVisibility(View.VISIBLE);
                sabRegisterBin.setVisibility(View.VISIBLE);
        }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        ImageView fabIconStar = new ImageView(this);
       // fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.bin_location_icon));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.map_view)
                .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        //SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
       // lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.basurajuan_logo));

       /* FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);*/

        SubActionButton.Builder itemBuilder1 = new SubActionButton.Builder(this);
        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        itemBuilder1.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        itemBuilder1.setLayoutParams(blueParams);

        // repeat many times:
        ImageView itemHybrid = new ImageView(this);
        itemHybrid.setImageResource(R.drawable.satellite_view);

        ImageView itemNormal = new ImageView(this);
        itemNormal.setImageResource(R.drawable.icon_normal_view);

        final SubActionButton sabHybrid = itemBuilder1
                .setContentView(itemHybrid)
                .build();
        final SubActionButton sabNormal = itemBuilder1.setContentView(itemNormal).build();

        // Build another menu with custom options
        final FloatingActionMenu leftCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabHybrid)
                .addSubActionView(sabNormal)
                .setRadius(redActionMenuRadius)
                .setStartAngle(170)
                .setEndAngle(120)
                .attachTo(leftCenterButton)
                .build();

        sabHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHybridMap(v);
            }
        });
        sabNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNormalMap(v);
            }
        });
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                leftCenterButton.setVisibility(View.INVISIBLE);
                sabHybrid.setVisibility(View.INVISIBLE);
                sabNormal.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                leftCenterButton.setVisibility(View.VISIBLE);
                sabHybrid.setVisibility(View.INVISIBLE);
                sabNormal.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    public void onNormalMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    public void onHybridMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
        LatLng bin_location = new LatLng(Double.parseDouble(globalData.getLatitude()), Double.parseDouble(globalData.getLongitude()));
//        Toast.makeText(getApplicationContext(), "DATA "+globalData.getLatitude()+" "+globalData.getLongitude(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), jsonLatitude + " Sample" + jsonLongitude, Toast.LENGTH_SHORT).show();

        mMap.addMarker(new MarkerOptions().position(bin_location).title("Basura Juan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bin_location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bin_location, 15));
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
    }
    /*private void getCoordinates(){
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
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_BIN_LOCATION_REQUEST, "retrieve");
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);
    }*/
}
