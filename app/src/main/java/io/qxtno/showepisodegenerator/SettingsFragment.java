package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private SharedPreferences prefs;
    private SharedPreferences.Editor settingsEditor;

    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RadioGroup themeGroup = view.findViewById(R.id.radio_group);
        RadioButton radio1 = view.findViewById(R.id.radio_light);
        RadioButton radio2 = view.findViewById(R.id.radio_dark);
        RadioButton radio3 = view.findViewById(R.id.radio_auto_battery_save);
        RadioButton radio4 = view.findViewById(R.id.radio_system_theme);

        prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("THEME", Context.MODE_PRIVATE);

        boolean r1 = prefs.getBoolean("theme_1", false);
        boolean r2 = prefs.getBoolean("theme_2", false);
        boolean r3;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            radio4.setVisibility(View.VISIBLE);
            r3 = prefs.getBoolean("theme_3", false);
            boolean r4 = prefs.getBoolean("theme_4", true);
            if (r1) {
                radio1.setChecked(prefs.getBoolean("theme_1", false));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (r2) {
                radio2.setChecked(prefs.getBoolean("theme_2", false));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (r3) {
                radio3.setChecked(prefs.getBoolean("theme_3", false));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            } else if (r4) {
                radio4.setChecked(prefs.getBoolean("theme_4", true));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }else {
            radio4.setVisibility(View.GONE);
            r3 = prefs.getBoolean("theme_3", true);
            if (r1) {
                radio1.setChecked(prefs.getBoolean("theme_1", false));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (r2) {
                radio2.setChecked(prefs.getBoolean("theme_2", false));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (r3) {
                radio3.setChecked(prefs.getBoolean("theme_3", true));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }

        themeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                prefs = Objects.requireNonNull(getContext()).getSharedPreferences("THEME", Context.MODE_PRIVATE);
                settingsEditor = prefs.edit();
                switch (checkedId) {
                    case R.id.radio_light:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        settingsEditor.putBoolean("theme_1", true);
                        settingsEditor.putBoolean("theme_2", false);
                        settingsEditor.putBoolean("theme_3", false);
                        settingsEditor.putBoolean("theme_4", false);
                        settingsEditor.apply();
                        break;
                    case R.id.radio_dark:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        settingsEditor.putBoolean("theme_1", false);
                        settingsEditor.putBoolean("theme_2", true);
                        settingsEditor.putBoolean("theme_3", false);
                        settingsEditor.putBoolean("theme_4", false);
                        settingsEditor.apply();
                        break;
                    case R.id.radio_auto_battery_save:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                        settingsEditor.putBoolean("theme_1", false);
                        settingsEditor.putBoolean("theme_2", false);
                        settingsEditor.putBoolean("theme_3", true);
                        settingsEditor.putBoolean("theme_4", false);
                        break;
                    case R.id.radio_system_theme:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        settingsEditor.putBoolean("theme_1", false);
                        settingsEditor.putBoolean("theme_2", false);
                        settingsEditor.putBoolean("theme_3", false);
                        settingsEditor.putBoolean("theme_4", true);
                        break;
                }
                settingsEditor.apply();
            }
        });

        return view;
    }
}
