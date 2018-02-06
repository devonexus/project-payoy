package com.example.apaodevo.basura_juan.Activities;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import br.com.goncalves.pugnotification.notification.PugNotification;

public class NotificationActivity extends NavigationDrawerActivity{
    private ArrayList<String> notification  = new ArrayList<>();
    private static String NOTIFICATION_URL = "http://basurajuan.x10host.com/notification-list.php";
    private String category = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Put navigation drawer on this layout
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);


       loadUnreadNotifications();
    }
    private void loadUnreadNotifications(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTIFICATION_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Random r = new Random();
                                int countNotif = r.nextInt(9999 - 1000) + 9999;
                                category = jsonObject.getString(Keys.TAG_NOTIFICATION_CATEGORY);
                                if(jsonArray.length() > 0) {
                                    if(category.toString().equals("Battery")) {
                                        //Toast.makeText(getApplicationContext(), "Number: " + countNotif, Toast.LENGTH_SHORT).show();
                                        PugNotification.with(getApplicationContext())
                                                .load()
                                                .title("Battery Status")
                                                .identifier(countNotif)
                                                .message(jsonObject.getString(Keys.TAG_NOTIFICATION_MESSAGE))
                                                .largeIcon(R.drawable.low_battery)
                                                .smallIcon(R.mipmap.ic_launcher)
                                                .flags(Notification.DEFAULT_ALL)
                                                .autoCancel(true)
                                                .simple()
                                                .build();
                                    } else if (category.toString().equals("Bin Capacity")){
                                        PugNotification.with(getApplicationContext())
                                                .load()
                                                .title("Bin Capacity")
                                                .identifier(countNotif)
                                                .message(jsonObject.getString(Keys.TAG_NOTIFICATION_MESSAGE))
                                                .largeIcon(R.drawable.bin_full)
                                                .smallIcon(R.mipmap.ic_launcher)
                                                .flags(Notification.DEFAULT_ALL)
                                                .autoCancel(true)
                                                .simple()
                                                .build();
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
