package com.kavi.droid.exchange.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.fragments.HomeFragment;
import com.kavi.droid.exchange.fragments.MyRequestsHistoryFragment;
import com.kavi.droid.exchange.fragments.NotificationFragment;
import com.kavi.droid.exchange.fragments.SettingsFragment;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.NavigationUtil;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private ImageView profilePicImageView;
    private TextView userNameTextView;
    private TextView userEmailTextView;

    private Context context = this;
    private ImageLoadingManager imageLoadingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        setUpViews();
    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                new NavigationUtil(LandingActivity.this)
                        .to(AddRequestActivity.class)
                        .go();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Remove app title from the action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setInitialView();
        slideMenuHeaderSetup();
    }

    private void slideMenuHeaderSetup() {
        imageLoadingManager = new ImageLoadingManager(context);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        profilePicImageView = (ImageView) hView.findViewById(R.id.profilePicImageView);
        userNameTextView = (TextView) hView.findViewById(R.id.userNameTextView);
        userEmailTextView = (TextView) hView.findViewById(R.id.userEmailTextView);

        if(SharedPreferenceManager.isUserLogIn(context)) {
            imageLoadingManager.loadImageToImageView(SharedPreferenceManager.getLoggedUserImageUrl(context),
                    profilePicImageView, true);
            if (SharedPreferenceManager.getLoggedUserName(context) != null)
                userNameTextView.setText(SharedPreferenceManager.getLoggedUserName(context));
            if (SharedPreferenceManager.getLoggedUserEmail(context) != null)
                userEmailTextView.setText(SharedPreferenceManager.getLoggedUserEmail(context));
        }
    }

    private void setInitialView() {

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        String fragmentTag = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            fragmentTag = Constants.HOME_FRAGMENT_TAG;
        } else if (id == R.id.nav_history) {
            fragment = new MyRequestsHistoryFragment();
            fragmentTag = Constants.HISTORY_FRAGMENT_TAG;
        } else if (id == R.id.nav_notifications) {
            fragment = new NotificationFragment();
            fragmentTag = Constants.NOTIFICATION_FRAGMENT_TAG;
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            fragmentTag = Constants.SETTINGS_FRAGMENT_TAG;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment, fragmentTag).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // error in creating fragment
            Log.e(Constants.LOG_TAG,"LandingActivity:onNavigationItemSelected / On Fragment form error: Fail to create Fragment");
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_icon_menu, menu);
        return true;
    }
}
