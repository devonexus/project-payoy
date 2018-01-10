package com.example.apaodevo.basura_juan.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterBin extends NavigationDrawerActivity {

    //public static String BIN_REG_URL = "http://132.223.41.121/bin-registration.php";
    public static String BIN_REG_URL = "http://basurajuan.x10host.com/bin-registration.php";
    private TextView etIpAddress, etBinName;
    private Button btn_register_bin;
    private ProgressDialog pDialog;
    private String ip_address, bin_name;
    private Button registerBin;
    /*private String ip_address, bin_name;*/ /* Variables  to store post data*/
    GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_register_bin, null, false);
        drawer.addView(contentView, 0);
        fab.setImageResource(R.drawable.floating_action_register_bin);
        fab.setVisibility(View.GONE);
        castObjects();
        registerBin     = (Button) findViewById(R.id.btn_reg_bin);
        globalData = (GlobalData) getApplicationContext();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Creating bin...");
        pDialog.setCancelable(false);

        btn_register_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateIpAddress()){
                    return;
                }
                if(!validateBinName()){
                    return;
                }
                createBin();
            }
        });

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)

                .setBackgroundDrawable(R.drawable.floating_action_register_bin)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.bin_location_icon);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.deploy);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_navigate_bin);

        final SubActionButton sabLocateBin = itemBuilder
                .setContentView(itemIcon1)
                .build();
        final SubActionButton sabDeployBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabNavigateBin = itemBuilder.setContentView(itemIcon3).build();

        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabLocateBin)
                .addSubActionView(sabDeployBin)
                .addSubActionView(sabNavigateBin)
                .attachTo(actionButton)
                .build();


        sabNavigateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( globalData.address == "") {
                    startActivity(new Intent(getApplicationContext(), DeviceList.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), NavigateBin.class));
                }
                startActivity(new Intent(getApplicationContext(), DeviceList.class));
            }
        });

        sabLocateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
        sabDeployBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionButton.setVisibility(View.INVISIBLE);
                sabLocateBin.setVisibility(View.INVISIBLE);
                sabNavigateBin.setVisibility(View.INVISIBLE);
                sabDeployBin.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionButton.setVisibility(View.VISIBLE);
                sabLocateBin.setVisibility(View.VISIBLE);
                sabNavigateBin.setVisibility(View.VISIBLE);
                sabDeployBin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public void castObjects(){
        etIpAddress = (TextView) findViewById(R.id.et_ip_address);
        etBinName   = (TextView) findViewById(R.id.et_bin_name);
        btn_register_bin    = (Button) findViewById(R.id.btn_reg_bin);
    }


    private void createBin() {
        showpDialog();
        CustomJSONRequest request = new CustomJSONRequest(Request.Method.POST, BIN_REG_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String server_response = response.getString(Keys.TAG_SUCCESS);
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
                                        if(server_response.equals("1")){
                                            Toast.makeText(getApplicationContext(), "Bin successfully registered", Toast.LENGTH_SHORT).show();
                                            etIpAddress.setText("");
                                            etBinName.setText("");
                                            hidepDialog();
                                        }
                                    }
                                });
                            }
                        };
                        thread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_IP_ADDRESS, etIpAddress.getText().toString());
                params.put(Keys.TAG_BIN_NAME, etBinName.getText().toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private boolean validateIpAddress() {
        ip_address = etIpAddress.getText().toString().trim();

        if (ip_address.isEmpty()) {
            etIpAddress.setError(getString(R.string.err_msg_ip_address));
            requestFocus(etIpAddress);
            return false;
        } else {
            etIpAddress.setError(null);
        }

        return true;
    }//Validate ip_address

    private boolean validateBinName() {
        bin_name = etBinName.getText().toString().trim();

        if (bin_name.isEmpty()) {
            etBinName.setError(getString(R.string.err_msg_bin_name));
            requestFocus(etBinName);
            return false;
        } else {
            etBinName.setError(null);
        }

        return true;
    }//Validate ip_address
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }  //Focus
}
