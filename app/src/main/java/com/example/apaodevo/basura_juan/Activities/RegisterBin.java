package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apaodevo.basura_juan.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterBin extends NavigationDrawerActivity {
        public static String BIN_REG_URL = "http://132.223.41.121/bin-registration.php";
//    public static String BIN_REG_URL = "http://basurajuan.x10host.com/bin-registration.php";
    private TextView etIpAddress, etBinName;
    private Button btn_register_bin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_register_bin, null, false);
        drawer.addView(contentView, 0);
        fab.setImageResource(R.drawable.floating_action_register_bin);
        castObjects();


        btn_register_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBin();
            }
        });
    }

    public void castObjects(){
        etIpAddress = (TextView) findViewById(R.id.et_ip_address);
        etBinName   = (TextView) findViewById(R.id.et_bin_name);
        btn_register_bin    = (Button) findViewById(R.id.btn_reg_bin);
    }


    private void createBin() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BIN_REG_URL,

              new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("ip_address",etIpAddress.getText().toString());
                    map.put("bin_name", etBinName.getText().toString());

                    return map;
                }
            };
            requestQueue.add(stringRequest);
    }
}

 /*   HashMap<String,String> map = new HashMap<>();
                map.put("ip_address",etIpAddress.getText().toString());
                        map.put("bin_id",etBinId.getText().toString());
                        map.put("bin_name", etBinName.getText().toString());
                        return map;*/