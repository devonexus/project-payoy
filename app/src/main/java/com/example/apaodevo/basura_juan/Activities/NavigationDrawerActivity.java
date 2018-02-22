package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.NotificationModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.joanzapata.iconify.widget.IconButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected FloatingActionButton fab;
    private NavigationView navigationView;
    private TextView tv_fullname, tv_email;
    private String fullName, imageUrl, emailAddress;    //Navigation Header data
    private ImageView img_login_user_image;
    private ProgressDialog pDialog;
    private AlphaAnimation buttonClick;
    private Toolbar toolbar;
    GlobalData globalData;
    private IconButton iconButton, homeButton;
    private ActionBarDrawerToggle toggle;
    private int mNotifCount = 0;
    public static NotificationModel notifModel;
    private Intent binListIntent, deploymentHistoryIntent, userProfileIntent, homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        castObjects(); //Call cast object function
        // /Set text in navigation drawer header
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        buttonClick = new AlphaAnimation(1F, 0.2F);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);
        //Toast.makeText(getApplicationContext(), "Revisited Counter", Toast.LENGTH_SHORT).show();


        img_login_user_image = (ImageView) header.findViewById(R.id.img_navigation_user_profile_image);
        tv_fullname = (TextView) header.findViewById(R.id.tvFullName);
        tv_email = (TextView) header.findViewById(R.id.tvEmail);

        loadNavHeader();
        //Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        notifModel = NotificationModel.getInstance();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //drawer.setDrawerListener(toggle);

        binListIntent = new Intent(getApplicationContext(), BinListActivity.class);
        userProfileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
        deploymentHistoryIntent = new Intent(getApplicationContext(), DeploymentHistory.class);
        homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                loadNavHeader();
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        initializeProgressDialogState();
        notificationCounter();
    }


    private void notificationCounter() {
        CustomJSONRequest notificationCountRequest = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.NOTIFICATION_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            notifModel.setNotificationCount(Integer.parseInt(response.getString(Keys.TAG_NOTIFICATION_COUNT)));
                            setNotifCount(Integer.parseInt(response.getString(Keys.TAG_NOTIFICATION_COUNT)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Could not get data from server.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                hidepDialog();
            }
        }
        );
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(notificationCountRequest);
    }

    private void loadNavHeader() {
        globalData = (GlobalData) getApplicationContext();
        fullName = globalData.getFullname()+".";
        imageUrl = globalData.getImageUrl().trim();
        emailAddress = globalData.getEmailAddress();
        tv_fullname.setText(fullName);
        tv_email.setText(emailAddress);

        Picasso.with(this)
                .load(imageUrl)
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.basurajuan_logo)     //This loads the image from the server
                .error(R.drawable.bin_list)
                .into(img_login_user_image);
    }

    private void castObjects() {
        tv_fullname     = (TextView) findViewById(R.id.tvFullName);
        fab             = (FloatingActionButton) findViewById(R.id.fab);

    }//Cast objects and point it to the object name
    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.menu_notification).getActionView();
        TextView counter = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        if(notifModel.getNotificationCount() == 0){
            counter.setVisibility(View.INVISIBLE);
        }else {
            counter.setText(String.valueOf(mNotifCount));
            counter.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(getApplicationContext(), "Notification Count: "+notifModel.getNotificationCount(), Toast.LENGTH_SHORT).show();
        counter = (TextView) badgeLayout.findViewById(R.id.badge_textView);

      /*  if(notificationCount == 0){
            counter.setVisibility(View.GONE);
        }*/
        counter.setText(""+notifModel.getNotificationCount());
        iconButton = (IconButton) badgeLayout.findViewById(R.id.badge_icon_button);
        homeButton = (IconButton) badgeLayout.findViewById(R.id.badge_home_button);
        homeButton.setText("{fa-home}");
        homeButton.setTextColor(getResources().getColor(R.color.colorWhite));
        iconButton.setText("{fa-bell}");
        iconButton.setTextColor(getResources().getColor(R.color.colorWhite));
        iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(homeIntent);
                overridePendingTransition(0, 0);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_account) {
            userProfileIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(userProfileIntent);
            overridePendingTransition(0, 0);
            drawer.closeDrawers();
            // Handle the camera action
        } else if (id == R.id.nav_bin_list) {
            binListIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(binListIntent);
            overridePendingTransition(0, 0);
            drawer.closeDrawers();
        } else if (id == R.id.nav_deployment_history) {
            startActivity(new Intent(getApplicationContext(), DeploymentHistory.class));
            deploymentHistoryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(deploymentHistoryIntent);
            overridePendingTransition(0, 0);
            drawer.closeDrawers();
        } else if (id == R.id.nav_logout) {
            signOut();
        }

        return true;
    }

    private void signOut() {
        showMessage("Logout", "Are you sure you want to logout?");
    }

    private void initializeProgressDialogState() {
        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Signing out, Please wait...");
        pDialog.setCancelable(false);
    }

    public void showMessage(String title, String Message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(title);
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
                dialog.cancel();
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



}
