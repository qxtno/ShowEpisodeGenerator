package io.qxtno.showepisodegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProperTheme();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_fav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavouritesFragment()).commit();
                break;
            case R.id.nav_add_show:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewShowFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

   private void setProperTheme(){

       SharedPreferences prefs = getSharedPreferences("THEME", Context.MODE_PRIVATE);
       boolean r1 = prefs.getBoolean("theme_1", false);
       boolean r2 = prefs.getBoolean("theme_2", false);
       boolean r3 = prefs.getBoolean("theme_3", false);
       boolean r4 = prefs.getBoolean("theme_4", false);

       if (r1) {
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       } else if (r2) {
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
       } else if (r3) {
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
       } else if (r4) {
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
       }
    }
}
