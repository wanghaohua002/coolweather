package com.wanghaohua.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import com.wanghaohua.example.coolweather.util.ConstantUtil;

public class MainActivity extends AppCompatActivity {

  private ChooseAreaFragment fragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    if (!TextUtils.isEmpty(preferences.getString(ConstantUtil.PREF_WEATHER, ""))) {
      Intent intent = new Intent(this, WeatherActivity.class);
      startActivity(intent);
      finish();
    }
    fragment = new ChooseAreaFragment();
    fragment.setListener(new ChooseAreaFragment.OnBackPressedListener() {
      @Override
      public void onBackPressed() {
        MainActivity.super.onBackPressed();
      }
    });
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, fragment)
        .commit();
  }

  @Override
  public void onBackPressed() {
    if (fragment != null) {
      fragment.onBackPressed();
    }
  }
}
