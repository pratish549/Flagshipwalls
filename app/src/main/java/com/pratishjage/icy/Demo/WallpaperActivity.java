package com.pratishjage.icy.Demo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pratishjage.icy.Dialogs.BottomSheetFragment;
import com.pratishjage.icy.FragmentTag;
import com.pratishjage.icy.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WallpaperActivity extends AppCompatActivity implements IWallpaperActivity {


    private ArrayList<String> mfragmentTags = new ArrayList<>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<>();
    private int mExitCount = 0;

    private HomeFragment homeFragment;
    private OSFragment osFragment;
    private DevicesFragment devicesFragment;
    private QueryWallpaperFrgment queryWallpaperFrgment;
    private ImageView backArrow, logoImageview;
    TextView headerText;
    String header;
    String TAG = getClass().getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mfragmentTags.clear();
                    mfragmentTags = new ArrayList<>();
                    init();

                    return true;
                case R.id.navigation_dashboard:

                    if (osFragment == null) {
                        osFragment = OSFragment.newInstance();
                        fragmentTransaction.add(R.id.main_container, osFragment, getString(R.string.tag_fragment_os));
                        fragmentTransaction.commit();
                        mfragmentTags.add(getString(R.string.tag_fragment_os));
                        mFragments.add(new FragmentTag(osFragment, getString(R.string.tag_fragment_os)));
                    } else {

                        mfragmentTags.remove(getString(R.string.tag_fragment_os));
                        mfragmentTags.add(getString(R.string.tag_fragment_os));

                    }
                    setFragmentVisible(getString(R.string.tag_fragment_os));

                    return true;
                case R.id.navigation_notifications:

                    if (devicesFragment == null) {
                        devicesFragment = DevicesFragment.newInstance();
                        fragmentTransaction.add(R.id.main_container, devicesFragment, getString(R.string.tag_fragment_devices));
                        fragmentTransaction.commit();
                        mfragmentTags.add(getString(R.string.tag_fragment_devices));
                        mFragments.add(new FragmentTag(devicesFragment, getString(R.string.tag_fragment_devices)));
                    } else {

                        mfragmentTags.remove(getString(R.string.tag_fragment_devices));
                        mfragmentTags.add(getString(R.string.tag_fragment_devices));

                    }

                    setFragmentVisible(getString(R.string.tag_fragment_devices));

                    return true;
            }
            return false;
        }
    };
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();
        backArrow = view.findViewById(R.id.back_arrow);
        headerText = view.findViewById(R.id.header_txt);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        logoImageview = view.findViewById(R.id.logo_image);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        init();

    }

    private void init() {
        if (homeFragment == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            homeFragment = HomeFragment.newInstance();
            fragmentTransaction.add(R.id.main_container, homeFragment, getString(R.string.tag_fragment_home));
            fragmentTransaction.commit();
            mfragmentTags.add(getString(R.string.tag_fragment_home));
            mFragments.add(new FragmentTag(homeFragment, getString(R.string.tag_fragment_home)));
        } else {

            mfragmentTags.remove(getString(R.string.tag_fragment_home));
            mfragmentTags.add(getString(R.string.tag_fragment_home));

        }
        setFragmentVisible(getString(R.string.tag_fragment_home));
    }

    private void setFragmentVisible(String tagName) {
        if (tagName.equals(getString(R.string.tag_fragment_home))) {
            showBottomNavigation();
            showLogoInFragment();
        } else if (tagName.equals(getString(R.string.tag_fragment_devices))) {
            showBottomNavigation();
            showLogoInFragment();
        } else if (tagName.equals(getString(R.string.tag_fragment_os))) {
            showBottomNavigation();
            showLogoInFragment();
        }
        if (tagName.equals(getString(R.string.tag_fragment_query_wallpaper))) {
            hideBottomNavigation();
            showHeaderInFreagment();
        }

        for (int i = 0; i < mFragments.size(); i++) {
            if (tagName.equals(mFragments.get(i).getTag())) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(mFragments.get(i).getFragment()).commit();

            } else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mFragments.get(i).getFragment()).commit();

            }
        }
        setNavigationIcon(tagName);
    }

    @Override
    public void onBackPressed() {
        int backsatckCount = mfragmentTags.size();
        if (backsatckCount > 1) {
            String topfragmentTag = mfragmentTags.get(mfragmentTags.size() - 1);
            String newTopfragmentTag = mfragmentTags.get(mfragmentTags.size() - 2);
            setFragmentVisible(newTopfragmentTag);
            mfragmentTags.remove(topfragmentTag);
            mExitCount = 0;
        } else if (backsatckCount == 1) {
            mExitCount++;
            Toast.makeText(this, "One more time", Toast.LENGTH_SHORT).show();
        }

        if (mExitCount >= 2) {
            super.onBackPressed();
        }
    }


    private void setNavigationIcon(String tagName) {
        Menu menu = navigation.getMenu();
        MenuItem menuItem = null;

        if (tagName.equals(getString(R.string.tag_fragment_home))) {
            menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        } else if (tagName.equals(getString(R.string.tag_fragment_os))) {
            menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        } else if (tagName.equals(getString(R.string.tag_fragment_devices))) {
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
        }
    }

    @Override
    public void inflateQueryWallpaperFragment(String whereTag, String whereValue) {
        header = whereValue;
        if (queryWallpaperFrgment != null) {
            getSupportFragmentManager().beginTransaction().remove(queryWallpaperFrgment).commitAllowingStateLoss();
        }
        queryWallpaperFrgment = new QueryWallpaperFrgment();
        Bundle args = new Bundle();
        args.putString(AppConstants.INTENT_WHERE_TAG, whereTag);
        args.putString(AppConstants.INTENT_WHERE_VALUE, whereValue);
        queryWallpaperFrgment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, queryWallpaperFrgment, getString(R.string.tag_fragment_query_wallpaper));
        fragmentTransaction.commit();
        mfragmentTags.add(getString(R.string.tag_fragment_query_wallpaper));

        mFragments.add(new FragmentTag(queryWallpaperFrgment, getString(R.string.tag_fragment_query_wallpaper)));
        setFragmentVisible(getString(R.string.tag_fragment_query_wallpaper));
    }

    @Override
    public void showWallpaperDialog(String wallurl) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString(AppConstants.WALLURL,wallurl);
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(getSupportFragmentManager(),"bottom");


    }

    private void hideBottomNavigation() {
        if (navigation != null) {
            navigation.setVisibility(View.GONE);
        }
    }

    private void showBottomNavigation() {
        if (navigation != null) {
            navigation.setVisibility(View.VISIBLE);
        }
    }

    private void showHeaderInFreagment() {
        logoImageview.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        Log.d(TAG, "showHeaderInFreagment: " + header);
        headerText.setText(header);
        backArrow.setVisibility(View.VISIBLE);
    }

    private void showLogoInFragment() {
        logoImageview.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        header="Flagship Walls";
        headerText.setText(header);

      /*  logoImageview.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.GONE);
        headerText.setText(header);*/
        backArrow.setVisibility(View.GONE);
    }

}
