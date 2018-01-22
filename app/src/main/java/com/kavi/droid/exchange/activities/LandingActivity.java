package com.kavi.droid.exchange.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.fragments.HomeFragment;
import com.kavi.droid.exchange.fragments.MyRequestsHistoryFragment;
import com.kavi.droid.exchange.fragments.NotificationFragment;
import com.kavi.droid.exchange.fragments.ProfileFragment;
import com.kavi.droid.exchange.fragments.LanguageFragment;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
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
    private FloatingActionButton fab;

    private Context context = this;
    private ImageLoadingManager imageLoadingManager;
    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init local
        commonUtils.setLocal(this);
        setContentView(R.layout.activity_landing);

        setUpViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Init local
        commonUtils.setLocal(this);
    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(getResources().getColorStateList(R.color.bright_green));
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
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        setActionBarTitle("Different Test");

        setInitialView();
        slideMenuHeaderSetup();
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void slideMenuHeaderSetup() {
        imageLoadingManager = new ImageLoadingManager(context);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        profilePicImageView = (ImageView) hView.findViewById(R.id.profilePicImageView);
        userNameTextView = (TextView) hView.findViewById(R.id.userNameTextView);
        userEmailTextView = (TextView) hView.findViewById(R.id.userEmailTextView);

        if(SharedPreferenceManager.isUserLogIn(context)) {
            imageLoadingManager.loadRoundCornerImageToImageView(SharedPreferenceManager.getLoggedUserImageUrl(context),
                    profilePicImageView);
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
            navigationView.getMenu().getItem(0).setChecked(true);
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        commonUtils.setLocal(this);
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
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            fragmentTag = Constants.PROFILE_FRAGMENT_TAG;
        } else if (id == R.id.nav_notifications) {
            fragment = new NotificationFragment();
            fragmentTag = Constants.NOTIFICATION_FRAGMENT_TAG;
        } else if (id == R.id.nav_language) {
            fragment = new LanguageFragment();
            fragmentTag = Constants.SETTINGS_FRAGMENT_TAG;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragment instanceof HomeFragment)
                fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment, fragmentTag)
                    .commit();
            else
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment, fragmentTag)
                        //.addToBackStack(Constants.HOME_FRAGMENT_TAG)
                        .commit();

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

    public void showFloatingActionButton() {
        fab.show();
    };

    public void hideFloatingActionButton() {
        fab.hide();
    };
}
