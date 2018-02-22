package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.example.apaodevo.basura_juan.Fragment.DeployedBinFragment;
import com.example.apaodevo.basura_juan.Fragment.UndeployedBinFragment;
import com.example.apaodevo.basura_juan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apaodevo on 12/5/2017.
 */

public class BinListActivity extends NavigationDrawerActivity{

    /*Declare variables here*/
    //protected DrawerLayout drawerLayout;

    private ViewPagerAdapter adapter;
    private AlphaAnimation buttonClick;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int    position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
