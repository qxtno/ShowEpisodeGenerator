package io.qxtno.showepisodegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    private boolean r1;
    private boolean r2;
    private boolean r3;
    private boolean r4;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProperTheme();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        init();
    }

    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build();
                Navigation.findNavController(this, R.id.fragment_container).navigate(R.id.homeFragment, null, navOptions);
                break;
            case R.id.nav_list:
                if (isValidDestination(R.id.nav_list)) {
                    Navigation.findNavController(this, R.id.fragment_container).navigate(R.id.showListFragment);
                }
                break;
            case R.id.nav_fav:
                if (isValidDestination(R.id.nav_fav)) {
                    Navigation.findNavController(this, R.id.fragment_container).navigate(R.id.favouritesFragment);
                }

                break;
            case R.id.nav_add_show:
                if (isValidDestination(R.id.nav_add_show)) {
                    Navigation.findNavController(this, R.id.fragment_container).navigate(R.id.newShowFragment);
                }

                break;
            case R.id.nav_settings:
                if (isValidDestination(R.id.nav_settings)) {
                    Navigation.findNavController(this, R.id.fragment_container).navigate(R.id.settingsFragment);
                }

                break;
            case R.id.nav_about:
                if (isValidDestination( R.id.nav_about)) {
                    Navigation.findNavController(this, R.id.fragment_container).navigate(R.id.aboutFragment);
                }

                break;
        }
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isValidDestination(int designation) {
        return designation!= Objects.requireNonNull(Navigation.findNavController(this, R.id.fragment_container).getCurrentDestination()).getId();
    }

    private void setProperTheme() {

        SharedPreferences prefs = getSharedPreferences("THEME", Context.MODE_PRIVATE);

        if (r1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (r2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (r3) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        } else if (r4) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        r1 = prefs.getBoolean("theme_1", false);
        r2 = prefs.getBoolean("theme_2", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r3 = prefs.getBoolean("theme_3", false);
            r4 = prefs.getBoolean("theme_4", true);
            if (r1) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (r2) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (r3) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            } else if (r4) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        } else {
            r3 = prefs.getBoolean("theme_3", true);
            if (r1) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (r2) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (r3) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment_container), drawerLayout);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else {
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
