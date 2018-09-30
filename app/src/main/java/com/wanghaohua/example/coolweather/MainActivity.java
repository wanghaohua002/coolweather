package com.wanghaohua.example.coolweather;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

  private ChooseAreaFragment fragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragment = new ChooseAreaFragment();
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
