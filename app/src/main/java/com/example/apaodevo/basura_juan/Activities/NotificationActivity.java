package com.example.apaodevo.basura_juan.Activities;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.example.apaodevo.basura_juan.Utils.FragmentText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import br.com.goncalves.pugnotification.notification.PugNotification;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class NotificationActivity extends NavigationDrawerActivity implements MaterialTabListener{
    private ArrayList<String> notification  = new ArrayList<>();
    private static String NOTIFICATION_URL = "http://basurajuan.x10host.com/notification-list.php";
    private String category = "";
    MaterialTabHost tabHost;
    ViewPager pager;
    ViewPagerAdapter adapter;
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Put navigation drawer on this layout
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);
        tabHost = (MaterialTabHost) findViewById(R.id.tabHost);

        pager = (ViewPager) findViewById(R.id.pager );

        // init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

       /* ImageView img = (ImageView) findViewById(R.id.imageId);
        img.setImageDrawable(getResources().getDrawable(R.drawable.battery_notif, null));*/
        /* pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
               tabHost.setSelectedNavigationItem(position);
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });*/
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }
        //loadUnreadNotifications();

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
        if(tab.getPosition() == 0){
            Toast.makeText(getApplicationContext(), "Selection 1", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Selection 2", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        //Toast.makeText(getApplicationContext(), "Reselection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return new FragmentText();
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Battery Status";
                case 1: return "Bin Capacity";
                default: return null;
            }
        }

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


    /*private Drawable getIcon(int position) {
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
             drawable = getResources().getDrawable(R.drawable.ic_battery_alert_black_24dp,null);

        }
        return drawable;


    }*/

}
