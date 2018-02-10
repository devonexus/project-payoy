package com.example.apaodevo.basura_juan.Activities;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Fragment.BatteryFragment;
import com.example.apaodevo.basura_juan.Fragment.BinCapacityFragment;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.goncalves.pugnotification.notification.PugNotification;

public class NotificationActivity extends NavigationDrawerActivity{
    private ArrayList<String> notification  = new ArrayList<>();
    //private static String NOTIFICATION_URL = "http://basurajuan.x10host.com/notification-list.php";
    private static String NOTIFICATION_URL = "http://132.223.41.121/notification-list.php";

    private String category = "";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.battery_notif,
            R.drawable.delete_notif
    };

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Put navigation drawer on this layout
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        loadUnreadNotifications();

    }

    public static Drawable changeDrawableColor(Context context,int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }


    private void setupTabIcons() {


        tabLayout.getTabAt(0).setIcon(changeDrawableColor(getApplicationContext(), tabIcons[0], Color.BLUE));
        tabLayout.getTabAt(1).setIcon(changeDrawableColor(getApplicationContext(), tabIcons[1], Color.BLUE));
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new BatteryFragment(),  "Battery Status");
        adapter.addFragment(new BinCapacityFragment(), "Bin Capacity");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
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
                                category = jsonObject.getString(Keys.TAG_NOTIFICATION);

                                //if(jsonArray.length() > 0) {

                                if(jsonArray.length() > 0) {

                                    if(category.toString().equals("Battery Status")) {
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
                                                .largeIcon(R.drawable.bin_capacity)
                                                .smallIcon(R.mipmap.ic_launcher)
                                                .flags(Notification.DEFAULT_ALL)
                                                .autoCancel(true)
                                                .simple()
                                                .build();
                                    }

                                }




                                Toast.makeText(getApplicationContext(), ""+category, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_NOTIFICATION_CATEGORY, "All");
                return params;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }




}
