package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.squareup.picasso.Picasso;

public class NavigationDrawerActivity extends ActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    protected DrawerLayout drawer;
    protected FloatingActionButton fab;
    private TextView tv_fullname, tv_email;
    private String fullName, imageUrl, emailAddress;    //Navigation Header data
    private ImageView img_login_user_image;
    GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        castObjects(); //Call cast object function

        globalData = (GlobalData) getApplicationContext();
        fullName            = globalData.getSomeVariable();
        imageUrl            = globalData.getImageUrl().trim();
        emailAddress        = globalData.getEmailAddress();


        // /Set text in navigation drawer header
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        img_login_user_image    = (ImageView) header.findViewById(R.id.img_navigation_user_profile_image);
        tv_fullname             = (TextView)header.findViewById(R.id.tvFullName);
        tv_email                = (TextView) header.findViewById(R.id.tvEmail);
        tv_fullname.setText(fullName);
        tv_email.setText(emailAddress);


        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.basurajuan_logo)    /* This loads the image from the server */
                .error(R.drawable.bin_list)
                .into(img_login_user_image);

        //Create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();





    }
    private void castObjects(){
        tv_fullname                     = (TextView) findViewById(R.id.tvFullName);
        fab                     = (FloatingActionButton) findViewById(R.id.fab);

    }//Cast objects and point it to the object name
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

        } else if (id == R.id.nav_deployment_history) {
            startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
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
        showMessage("Message","Are you sure you want to logout?");
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                intent.putExtra("LOGIN_STATUS", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}