package com.example.apaodevo.basura_juan.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.example.apaodevo.basura_juan.Utils.LocationHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.location.SimpleLocation;


public class DeployBinActivity extends NavigationDrawerActivity {
    private Button             btnDeploy;
    private Spinner           dropdown;
    private ArrayList<String> binNames = new ArrayList<>();
    private EditText          etActualLocation;
    ArrayList<BinModel>       world;
    GlobalData                globalData;
    private LocationHelper    locationHelper;
    private String            strAddress;
    private ProgressDialog    pDialog;
    Intent                    devicelist,naviagtenin;
    public static String      deploy = "", server_response = "";
    private SimpleLocation    simpleLocation;
    private double            latitude, longitude;
    private static String     selectedBinId = "";
    private String             binId;
    private String             cancelIdRequests = "ID";
    private String             deployRequests = "deploy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_deploy_bin, null, false);
        drawer.addView(contentView, 0);

        globalData       = (GlobalData) getApplicationContext();
        locationHelper   = new LocationHelper(DeployBinActivity.this);
        //get the spinner from the xml.
        dropdown         = (Spinner) findViewById(R.id.spinner1);
        btnDeploy        = (Button) findViewById(R.id.btn_deploy);
        etActualLocation = (EditText) findViewById(R.id.etActualLocation);
        fab.setImageResource(R.drawable.deploy);
        fab.setVisibility(View.GONE);
        loadBinNames();
        pDialog          = new ProgressDialog(this);
        pDialog.setMessage("Deploying bin, Please wait...");
        pDialog.setCancelable(false);

        // construct a new instance of SimpleLocation
        simpleLocation   = new SimpleLocation(this);
        if(DeviceList.btSocket != null)
        {
            globalData.msg("Please disconnect connected bluetooth!!!");
            naviagtenin = new Intent(DeployBinActivity.this, NavigateBin.class);
            startActivity(naviagtenin);
        }
        if(shouldAskPermissions()){
            askPermissions();
        }
        if (!simpleLocation.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        if (simpleLocation.hasLocationEnabled()) {
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedBinName = parent.getItemAtPosition(position).toString();
               // Toast.makeText(getApplicationContext(), ""+selectedItem, Toast.LENGTH_SHORT).show();
                getBinId(selectedBinName);
                latitude  = simpleLocation.getLatitude();
                longitude = simpleLocation.getLongitude();
                Toast.makeText(getApplicationContext(), "Location: "+latitude+" Longitude: "+longitude, Toast.LENGTH_SHORT).show();
                Thread thread = new Thread() {

                    @Override
                    public void run() {
                        // Block this thread for 2 seconds.al
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getLocationName(latitude, longitude);
                            }
                        });
                    }
                };
                thread.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "No registered bins yet!!!", Toast.LENGTH_SHORT).show();
            }
        });
        }
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)

                .setBackgroundDrawable(R.drawable.deploy)
                .build();
        int blueSubActionButtonSize;
        blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
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
        itemIcon2.setImageResource(R.drawable.bin_location_icon);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_action_register_bin);

        final SubActionButton sabNavigateBin = itemBuilder.setContentView(itemIcon1).build();
        final SubActionButton sabLocateBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabRegisterBin = itemBuilder.setContentView(itemIcon3).build();

        //attach the sub buttons to the main button
        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabNavigateBin)
                .addSubActionView(sabLocateBin)
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

        sabLocateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
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
                sabLocateBin.setVisibility(View.INVISIBLE);
                sabNavigateBin.setVisibility(View.INVISIBLE);
                sabRegisterBin.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                actionButton.setVisibility(View.VISIBLE);
                sabLocateBin.setVisibility(View.VISIBLE);
                sabNavigateBin.setVisibility(View.VISIBLE);
                sabRegisterBin.setVisibility(View.VISIBLE);
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        btnDeploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!validateDeploymentFields()){
                   return;
               }
                //Toast.makeText(getApplicationContext(), ""+globalData.getBinId(), Toast.LENGTH_SHORT).show();
                deployBin(globalData.getUserid(), globalData.getBinId(), etActualLocation.getText().toString());
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        // Block this thread for 4 seconds.al
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deploy = "deploy";
                                devicelist = new Intent(DeployBinActivity.this, DeviceList.class);
                                finish();
                                startActivity(devicelist);
                            }
                        });
                    }
                };
                thread.start();
            }
        });
    }

    private void getLocationName(double lat, double longi){
        Geocoder gc = new Geocoder(getApplicationContext());
        if(gc.isPresent()){
            List<Address> list = null;
            try {
                list = gc.getFromLocation(latitude, longitude,1);
                Address address = list.get(0);

              /*  str.append("Name: " + address.getLocality() + "\n");
                str.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
                str.append("Admin Area: " + address.getAdminArea() + "\n");
                str.append("Admin Area: " + address.getAddressLine(0) + "\n");
                str.append("Admin Area: " + address + "\n");
                str.append("Country: " + address.getCountryName() + "\n");str.append("Country Code: " + address.getCountryCode() + "\n");
                str.append("Country Code: " + address.getCountryCode() + "\n");*/
                strAddress = address.getAddressLine(0);

                etActualLocation.setText(strAddress);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean validateFields() {
        String binNames = (String) dropdown.getSelectedItem();
        String location = etActualLocation.getText().toString();
        if (binNames.isEmpty() && location.isEmpty()) {
            return false;
        }
        return true;
    }

    @TargetApi(25)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    private void deployBin(final String uid, final String bid, final String location) {
        showpDialog();
        final CustomJSONRequest deployBinRequest  = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.DEPLOYMENT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                // Block this thread for 4 seconds.al
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Bin succesfully deployed.", Toast.LENGTH_LONG).show();
                                        hidepDialog();
                                    }
                                });
                            }
                        };
                        thread.start();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                hidepDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USER_ID, uid);
                params.put(Keys.TAG_BIN_ID, bid);
                params.put(Keys.TAG_DEPLOYMENT_LOCATION, location);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deployBinRequest);
    }

    private void getBinId(final String binName){
        CustomJSONRequest binIdRequest  = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.DEPLOYMENT_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            selectedBinId = response.getString(Keys.TAG_BIN_ID);
                            globalData.setBinId(selectedBinId);
                            VolleySingleton.getInstance(getApplicationContext()).cancelPendingRequests(cancelIdRequests);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                VolleySingleton.getInstance(getApplicationContext()).cancelPendingRequests(cancelIdRequests);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_BIN_NAME, binName);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(binIdRequest);
    }
    private void loadBinNames() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceUrl.BIN_REG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                              for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //binNames.add(jsonObject.getString(Keys.TAG_BIN_NAME));
                                BinModel binModel = new BinModel();
                                binModel.setBinName(jsonObject.optString(Keys.TAG_BIN_NAME));
                                binNames.add(jsonObject.getString(Keys.TAG_BIN_NAME));
                                Log.d("XXX", jsonObject.getString(Keys.TAG_BIN_NAME));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeployBinActivity.this, android.R.layout.simple_spinner_dropdown_item, binNames);
                        dropdown.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USER_ID, globalData.getUserid());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
       /* final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, BIN_REG_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String json = "";

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                //binNames.add(jsonObject.getString(Keys.TAG_BIN_NAME));
                                BinModel binModel = new BinModel();
                                binModel.setBinName(jsonObject.optString(Keys.TAG_BIN_NAME));
                                //world.add(binModel);
                                //json = jsonObject.getString(Keys.TAG_BIN_NAME);
                                binNames.add(jsonObject.getString(Keys.TAG_BIN_NAME));
                                Log.d("XXX", jsonObject.getString(Keys.TAG_BIN_NAME));
                                //binNames.add(jsonObject.optString(Keys.TAG_BIN_NAME));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeployBinActivity.this, android.R.layout.simple_spinner_dropdown_item, binNames);
                        dropdown.setAdapter(adapter);
                        //dropdown.setAdapter(new ArrayAdapter<String>(DeployBinActivity.this, android.R.layout.simple_spinner_dropdown_item, binNames));


                        Log.d("Recycler View Contents", response.toString());
                        List<BinModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<BinModel>>() {

                        }.getType());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Could not get data from server.", Toast.LENGTH_SHORT).show();
                Log.d(Keys.TAG_ERRORS, error.getMessage());
                error.printStackTrace();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
*/
    }/*This will load bin names to the drop down*/

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private boolean validateDeploymentFields() {
        String locationName = etActualLocation.getText().toString();

        if (locationName.isEmpty()) {
            etActualLocation.setError(getString(R.string.actual_location));
            return false;
        } else {
            etActualLocation.setError(null);
        }

        return true;
    }//Validate lastname
}
