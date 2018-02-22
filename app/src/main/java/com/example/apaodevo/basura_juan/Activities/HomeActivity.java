package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.LocationModel;
import com.example.apaodevo.basura_juan.Models.UserModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends NavigationDrawerActivity{
    private Button btn_bin_location, btn_deploy_bin, btn_register_bin, btn_navigate_bin;
    private ProgressDialog pDialog;
    GlobalData globalData;
    public static UserModel userModel;
    public static LocationModel locationModel;
    String jsonLatitude, jsonLongitude;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_navigate_bin);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);

        globalData = (GlobalData) getApplicationContext();
        fab.setImageResource(R.drawable.floating_navigate_bin);
        fab.setVisibility(View.GONE);
        locationModel = LocationModel.getInstance();
        getCoordinates();

        //        //Go to bin location interface
        //        btn_bin_location = (Button) findViewById(R.id.btn_de);
        //        btn_bin_location.setOnClickListener(
        //                new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View v) {
        //                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        //                    }
        //                }
        //        );
        //Go to bin location interface


        btn_bin_location = (Button) findViewById(R.id.btn_de);
        btn_bin_location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    }
                }
        );

        //Go to deploy bin interface
        btn_deploy_bin = (Button) findViewById(R.id.btn_deploy_bin);
        btn_deploy_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
            }
        });
        //Go to register bin interface

        btn_register_bin    = (Button) findViewById(R.id.btn_register_bin);
        btn_register_bin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), RegisterBin.class));
                    }
                }
        );
        btn_navigate_bin    = (Button) findViewById(R.id.btn_navigate_bin);
        btn_navigate_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceList.btSocket == null) {
                    globalData.intentAddress = "NAVIGATE";
                    startActivity(new Intent(getApplicationContext(), DeviceList.class));
                } else
                {
                    startActivity(new Intent(getApplicationContext(), NavigateBin.class));
                }
            }
        });

        initializeProgressDialogState();


    }
        private void getCoordinates(){
        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.BIN_LOCATION_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonLatitude = response.getString(Keys.TAG_BIN_LATITUDE);
                            jsonLongitude = response.getString(Keys.TAG_BIN_LONGITUDE);
                            final GlobalData globalData = (GlobalData) getApplicationContext();
                            /*globalData.setLatitude(jsonLatitude);
                            globalData.setLongitude(jsonLongitude);*/
                            locationModel.setLatitude(Double.valueOf(jsonLatitude));
                            locationModel.setLongitude(Double.valueOf(jsonLongitude));

                            // lati = Double.parseDouble(jsonLatitude);
                            // longitude = Double.parseDouble(jsonLongitude);
                            //   globalData = (GlobalData) getApplicationContext();
                            //   globalData.setLatitude(jsonLatitude);
                            //   globalData.setLongitude(jsonLongitude);
                            //Toast.makeText(getApplicationContext(), jsonLatitude+" Longitude "+jsonLongitude, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Toast.makeText(getApplicationContext(), "Data: "+error.toString(), Toast.LENGTH_SHORT).show();
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
    }
    private void initializeProgressDialogState(){
        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Signing out, Please wait...");
        pDialog.setCancelable(false);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        signOut();
    }
    private void signOut(){
        showMessage("Logout","This is the last activity, going back will log you out of the application.");
    }
    public void showMessage(String title,String Message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_exit_to_app_white_48dp);
        builder.setMessage(Message);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                showpDialog();
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
                                Disconnect();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.putExtra("LOGIN_STATUS", false);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
                                startActivity(intent);
                                finish();
                                hidepDialog();
                            }
                        });

                    }

                };
                thread.start();
            }
        });
        builder.show();
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }//Display progress dialog

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }//Dismiss progressDialog

    private void Disconnect()
    {
        if (DeviceList.btSocket != null)
        {
            try
            {
                DeviceList.btSocket.getOutputStream().write("0".toString().getBytes());
                DeviceList.btSocket.close(); //close connection
                globalData.address="";
                DeviceList.btSocket = null;
            }
            catch (IOException e)
            { globalData.msg("Error");}
        }
        finish(); //return to the first layout
    }
}