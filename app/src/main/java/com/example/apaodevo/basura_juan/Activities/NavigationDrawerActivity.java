package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.joanzapata.iconify.widget.IconButton;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int navItemIndex = 0;
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
        View header=navigationView.getHeaderView(0);
        img_login_user_image    = (ImageView) header.findViewById(R.id.img_navigation_user_profile_image);
        tv_fullname             = (TextView)header.findViewById(R.id.tvFullName);
        tv_email                = (TextView) header.findViewById(R.id.tvEmail);
        /*globalData = (GlobalData) getApplicationContext();
        fullName            = globalData.getSomeVariable();
        imageUrl            = globalData.getImageUrl().trim();
        emailAddress        = globalData.getEmailAddress();
        tv_fullname.setText(fullName);
        tv_email.setText(emailAddress);*/
        loadNavHeader();
        //Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        /*toolbar.setNavigationIcon(R.drawable.location_icon);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub");*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
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
    }

    private void loadNavHeader(){
        globalData = (GlobalData) getApplicationContext();
        fullName            = globalData.getFullname();
        imageUrl            = globalData.getImageUrl().trim();
        emailAddress        = globalData.getEmailAddress();
        tv_fullname.setText(fullName);
        tv_email.setText(emailAddress);

        Picasso.with(this)
                .load(imageUrl)
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.basurajuan_logo)     //This loads the image from the server
                .error(R.drawable.bin_list)
                .into(img_login_user_image);
    }

    private void castObjects(){
        tv_fullname             = (TextView) findViewById(R.id.tvFullName);
        fab                     = (FloatingActionButton) findViewById(R.id.fab);

    }//Cast objects and point it to the object name

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        RelativeLayout badgeLayout = (RelativeLayout)  menu.findItem(R.id.menu_notification).getActionView();
        TextView counter = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        counter.setText("10");
        iconButton = (IconButton) badgeLayout.findViewById(R.id.badge_icon_button);
        homeButton = (IconButton) badgeLayout.findViewById(R.id.badge_home_button);
        homeButton.setText("{fa-home}");
        homeButton.setTextColor(getResources().getColor(R.color.colorWhite));
        iconButton.setText("{fa-bell}");
        iconButton.setTextColor(getResources().getColor(R.color.colorWhite));
        //final View menu_notifications = menu.findItem(R.id.menu_notification).getActionView();
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
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_bin_list) {
            startActivity(new Intent(getApplicationContext(), BinListActivity.class));
        } else if (id == R.id.nav_deployment_history) {

            startActivity(new Intent(getApplicationContext(), DeploymentHistory.class));
        } else if (id == R.id.nav_logout) {
            signOut();
        }
            /*
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }*/
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void signOut(){
        showMessage("Logout","Are you sure you want to logout?");
    }

    private void initializeProgressDialogState(){
        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Signing out, Please wait...");
        pDialog.setCancelable(false);
    }
    public void showMessage(String title,String Message){
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


    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }//Selecting navigation drawer item

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }//Display progress dialog

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }//Dismiss progressDialog
}
//package com.example.apaodevo.basura_juan.Activities;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.Snackbar;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.apaodevo.basura_juan.R;
//import com.example.apaodevo.basura_juan.Services.GlobalData;
//import com.joanzapata.iconify.widget.IconButton;
//import com.squareup.picasso.Picasso;
//
//import jp.wasabeef.picasso.transformations.CropCircleTransformation;
//
//public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    private int navItemIndex = 0;
//    protected DrawerLayout drawer;
//    protected FloatingActionButton fab;
//    private NavigationView navigationView;
//    private TextView tv_fullname, tv_email;
//    private String fullName, imageUrl, emailAddress;    //Navigation Header data
//    private ImageView img_login_user_image;
//    private ProgressDialog pDialog;
//    private AlphaAnimation buttonClick;
//    private Toolbar toolbar;
//    private static final String TAG_HOME = "home";
//    public static String CURRENT_TAG = TAG_HOME;
//
//
//    GlobalData globalData;
//    private IconButton iconButton;
//    private ActionBarDrawerToggle toggle;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_navigation_drawer);
//
//        castObjects(); //Call cast object function
//
//        globalData = (GlobalData) getApplicationContext();
//        fullName            = globalData.getSomeVariable();
//        imageUrl            = globalData.getImageUrl().trim();
//        emailAddress        = globalData.getEmailAddress();
//
//
//        // /Set text in navigation drawer header
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        buttonClick = new AlphaAnimation(1F, 0.2F);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setItemIconTintList(null);
//        View header=navigationView.getHeaderView(0);
//        img_login_user_image    = (ImageView) header.findViewById(R.id.img_navigation_user_profile_image);
//        tv_fullname             = (TextView)header.findViewById(R.id.tvFullName);
//        tv_email                = (TextView) header.findViewById(R.id.tvEmail);
//        tv_fullname.setText(fullName);
//        tv_email.setText(emailAddress);
//
//
//        Picasso.with(this)
//                .load(imageUrl)
//                .transform(new CropCircleTransformation())
//                .placeholder(R.drawable.basurajuan_logo)     //This loads the image from the server
//                .error(R.drawable.bin_list)
//                .into(img_login_user_image);
//
//        //Create toolbar
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setLogo(R.mipmap.ic_logo);
//
//
//
//
//        /*toolbar.setNavigationIcon(R.drawable.location_icon);
//        toolbar.setTitle("Title");
//        toolbar.setSubtitle("Sub");*/
//        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        initializeProgressDialogState();
//    }
//    private void castObjects(){
//        tv_fullname             = (TextView) findViewById(R.id.tvFullName);
//        fab                     = (FloatingActionButton) findViewById(R.id.fab);
//
//    }//Cast objects and point it to the object name
//    @Override
//    public void onBackPressed() {
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
//        RelativeLayout badgeLayout = (RelativeLayout)  menu.findItem(R.id.menu_notification).getActionView();
//        TextView counter = (TextView) badgeLayout.findViewById(R.id.badge_textView);
//        counter.setText("10");
//        counter.setVisibility(View.GONE);
//        iconButton = (IconButton) badgeLayout.findViewById(R.id.badge_icon_button);
//        iconButton.setText("{fa-bell}");
//        iconButton.setTextColor(getResources().getColor(R.color.colorNotificationBell));
//       //final View menu_notifications = menu.findItem(R.id.menu_notification).getActionView();
//        iconButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.startAnimation(buttonClick);
//
//                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (id == R.id.nav_account) {
//            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//            // Handle the camera action
//        } else if (id == R.id.nav_bin_list) {
//            startActivity(new Intent(getApplicationContext(), BinListActivity.class));
//        } else if (id == R.id.nav_deployment_history) {
//            navItemIndex = 2;
//            startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
//        } else if (id == R.id.nav_logout) {
//            signOut();
//        }
//            /*
//        } else if (id == R.id.nav_share) {
//        } else if (id == R.id.nav_send) {
//        }*/
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//    private void signOut(){
//        showMessage("Logout","Are you sure you want to logout?");
//    }
//
//    private void initializeProgressDialogState(){
//        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Signing out, Please wait...");
//        pDialog.setCancelable(false);
//    }
//    public void showMessage(String title,String Message){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setIcon(R.drawable.ic_exit_to_app_white_48dp);
//        builder.setMessage(Message);
//        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                showpDialog();
//                Thread thread = new Thread() {
//
//                    @Override
//                    public void run() {
//
//                        // Block this thread for 4 seconds.al
//                        try {
//                            Thread.sleep(1500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                intent.putExtra("LOGIN_STATUS", false);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
//                                startActivity(intent);
//                                finish();
//                                hidepDialog();
//                            }
//                        });
//
//                    }
//
//                };
//                thread.start();
//            }
//
//        });
//
//        builder.show();
//    }
//    private void selectNavMenu(){
//        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
//    }//Selecting navigation drawer item
//
//    private void showpDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }//Display progress dialog
//    private void showpDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }//Display progress dialog
//    private void hidepDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }//Dismiss progressDialog
//}