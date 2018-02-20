package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apaodevo.basura_juan.Fragment.DeployedBinFragment;
import com.example.apaodevo.basura_juan.Fragment.UndeployedBinFragment;
import com.example.apaodevo.basura_juan.R;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apaodevo on 12/5/2017.
 */

public class BinListActivity extends NavigationDrawerActivity{

    /*Declare variables here*/
    //protected DrawerLayout drawerLayout;

    private ViewPagerAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private EditText binSearch;
    private AlphaAnimation buttonClick;
    private String binId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private IconButton iconButton, homeButton;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bin_list);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_bin_list, null, false);

        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE); // Hide floating action
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        buttonClick = new AlphaAnimation(1F, 0.2F);
        //Cast objects here
        Toast.makeText(getApplicationContext(), "Goes within bin list activity", Toast.LENGTH_SHORT).show();
        /*recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        binSearch = (EditText) findViewById(R.id.search_bin);

        //Change as per key press
        binSearch.addTextChangedListener(new SearchBinTextWatcher(binSearch));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //binList = new ArrayList<>();

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);*/

        //Initialize bin list adapter
        //initializeAdapter();

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DeployedBinFragment(),   "Deployed Bins");
        adapter.addFragment(new UndeployedBinFragment(), "Undeployed Bins");

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
        public int getItemPosition(Object object) {

            return POSITION_NONE;
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
            return mFragmentTitleList.get(position);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void signOut() {
        showMessage("Logout", "Are you sure you want to logout?");
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }//Display progress dialog

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }//Dismiss progressDialog


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
   /* @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_account) {
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_bin_list) {
            startActivity(new Intent(this, BinListActivity.class));
        } else if (id == R.id.nav_deployment_history) {
            startActivity(new Intent(getApplicationContext(), DeploymentHistory.class));
        } else if (id == R.id.nav_logout) {
            signOut();
        }

        return true;
    }*/
}
