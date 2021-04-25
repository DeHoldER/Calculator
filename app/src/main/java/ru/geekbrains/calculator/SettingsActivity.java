package ru.geekbrains.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private Button mButtonBack;
    private SwitchMaterial mNightModeSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("Theme", MODE_PRIVATE);

        mButtonBack = findViewById(R.id.button_back);

        initView();

        mButtonBack.setOnClickListener(v -> {
//            Intent back = new Intent(this, MainActivity.class);
//            startActivity(back);
            this.finish();
        });

        mNightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveNightModeState(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveNightModeState(false);
            }
        });
    }

    private void initView() {
        mNightModeSwitch = findViewById(R.id.night_mode_switch);

        if (MainActivity.NIGHT_MODE_STATE) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mNightModeSwitch.setChecked(true);
        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mNightModeSwitch.setChecked(false);
        }
    }

    private void saveNightModeState(boolean value) {
        editor = sharedPreferences.edit();
        editor.putBoolean("mode", value).apply();
    }
}