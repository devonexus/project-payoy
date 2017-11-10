package com.example.apaodevo.basura_juan.Activities;

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
import android.widget.TextView;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;

public class NavigationDrawerActivity extends ActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GlobalData globalData = new GlobalData();
    protected DrawerLayout drawer;
    protected FloatingActionButton fab;
    private TextView tv1;
    private String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        castObjects(); //Call cast object function

        fullname = globalData.getSomeVariable();

        //Set text in navigation drawer header
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        tv1 = (TextView)header.findViewById(R.id.tvFullName);
        tv1.setText(fullname);


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
        tv1 = (TextView) findViewById(R.id.tvFullName);
        fab = (FloatingActionButton) findViewById(R.id.fab);
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
            startActivity(new Intent(getApplicationContext(), RegisterUserActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_bin_list) {

        } else if (id == R.id.nav_deployment_history) {

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
            /*
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
