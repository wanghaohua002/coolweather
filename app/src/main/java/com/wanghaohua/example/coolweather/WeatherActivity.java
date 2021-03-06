package com.wanghaohua.example.coolweather;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wanghaohua.example.coolweather.model.WeatherVo;
import com.wanghaohua.example.coolweather.util.ConstantUtil;
import com.wanghaohua.example.coolweather.util.GsonUtil;
import com.wanghaohua.example.coolweather.util.HttpCallBack;
import com.wanghaohua.example.coolweather.util.HttpUtil;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建时间: 2018/09/30 14:41
 * 作者: wanghaohua
 * 描述:
 */
public class WeatherActivity extends AppCompatActivity {

  public DrawerLayout mDrawLayout;
  private Button mBtnNav;
  public SwipeRefreshLayout mSwipeRefreshLayout;
  private ImageView mIvBingPic;
  private TextView mTvTitle;
  private TextView mTvUpdateTime;
  private TextView mTvDegree;
  private TextView mTvWeatherInfo;
  private LinearLayout mLlForecast;
  private TextView mTvAqi;
  private TextView mTvPm25;
  private TextView mTvComfort;
  private TextView mTvCarWash;
  private TextView mTvSport;

  private WeatherVo mWeather;

  private String mWeatherId;

  private ChooseAreaFragment mFragment;

  public static void start(Activity activity, String weatherId) {
    Intent intent = new Intent(activity, WeatherActivity.class);
    intent.putExtra(ConstantUtil.EXTRA_WEATHER_ID, weatherId);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    setContentView(R.layout.activity_weather);
    initView();
    mFragment = new ChooseAreaFragment();
    mFragment.setListener(new ChooseAreaFragment.OnBackPressedListener() {
      @Override
      public void onBackPressed() {
        mDrawLayout.closeDrawers();
      }
    });
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fl_container, mFragment)
        .commitAllowingStateLoss();
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    String bingPic = preferences.getString(ConstantUtil.BACKGROUND_PIC, "");
    String weatherString = preferences.getString(ConstantUtil.PREF_WEATHER, "");
    if (!TextUtils.isEmpty(weatherString)) {
      mWeather = GsonUtil.handleWeatherResponse(weatherString);
      mWeatherId = mWeather.basic.id;
      showWeather();
    } else {
      mWeatherId = getIntent().getStringExtra(ConstantUtil.EXTRA_WEATHER_ID);
      requestWeather();
    }
    if (!TextUtils.isEmpty(bingPic)) {
      Glide.with(this).load(bingPic).into(mIvBingPic);
    } else {
      loadBingPic();
    }
  }

  @Override
  public void onBackPressed() {
    if (mDrawLayout.isDrawerOpen(findViewById(R.id.fl_container)) && mFragment != null) {
      mFragment.onBackPressed();
    } else {
      super.onBackPressed();
    }
  }

  private void initView() {
    mDrawLayout = findViewById(R.id.drawer_layout);
    mBtnNav = findViewById(R.id.btn_nav);
    mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mIvBingPic = findViewById(R.id.iv_bing_pic);
    mTvTitle = findViewById(R.id.tv_title);
    mTvUpdateTime = findViewById(R.id.tv_update_time);
    mTvDegree = findViewById(R.id.tv_degree);
    mTvWeatherInfo = findViewById(R.id.tv_weather_info);
    mLlForecast = findViewById(R.id.ll_forecast);
    mTvAqi = findViewById(R.id.tv_aqi);
    mTvPm25 = findViewById(R.id.tv_pm25);
    mTvComfort = findViewById(R.id.tv_comfort);
    mTvCarWash = findViewById(R.id.tv_car_wash);
    mTvSport = findViewById(R.id.tv_sport);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        requestWeather();
      }
    });
    mBtnNav.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mDrawLayout.openDrawer(Gravity.LEFT);
      }
    });
  }

  private void showWeather() {
    if (mWeather == null) {
      return;
    }
    mTvTitle.setText(mWeather.basic.city);
    mTvUpdateTime.setText(mWeather.basic.update.loc.split(" ")[1]);
    mTvDegree.setText(mWeather.now.tmp + "°C");
    mTvWeatherInfo.setText(mWeather.now.cond.txt);
    mLlForecast.removeAllViews();
    for (WeatherVo.DailyForecast forecast : mWeather.dailyForecasts) {
      View view = LayoutInflater.from(this).inflate(R.layout.item_forecast, mLlForecast, false);
      TextView tvDate = view.findViewById(R.id.tv_date);
      TextView tvInfo = view.findViewById(R.id.tv_info);
      TextView tvMax = view.findViewById(R.id.tv_max);
      TextView tvMin = view.findViewById(R.id.tv_min);
      tvDate.setText(forecast.date);
      tvInfo.setText(forecast.cond.txt);
      tvMax.setText(forecast.tmp.max);
      tvMin.setText(forecast.tmp.min);
      mLlForecast.addView(view);
    }
    if (mWeather.aqi != null) {
      mTvAqi.setText(mWeather.aqi.city.aqi);
      mTvPm25.setText(mWeather.aqi.city.pm25);
    }
    mTvComfort.setText("舒适度：" + mWeather.suggestion.comf.txt);
    mTvCarWash.setText("洗车指数：" + mWeather.suggestion.cw.txt);
    mTvSport.setText("运动建议：" + mWeather.suggestion.sport.txt);
  }

  private void requestWeather() {
    requestWeather(mWeatherId);
  }

  public void requestWeather(String weatherId) {
    if (TextUtils.isEmpty(weatherId)) {
      return;
    }
    String url = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=" + ConstantUtil.WEATHER_KEY;
    HttpUtil.sendOkHttpRequest(url, new HttpCallBack() {
      @Override
      public void onFail(Call call, IOException e) {
        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_LONG).show();
        mSwipeRefreshLayout.setRefreshing(false);
      }

      @Override
      public void onResult(Call call, String response) throws IOException {
        mWeather = GsonUtil.handleWeatherResponse(response);
        if (mWeather != null && ConstantUtil.OK.equals(mWeather.status)) {
          SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
          SharedPreferences.Editor editor = preferences.edit();
          editor.putString(ConstantUtil.PREF_WEATHER, response);
          editor.apply();
          mWeatherId = mWeather.basic.id;
          showWeather();
        } else {
          Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_LONG).show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
      }
    });
    loadBingPic();
  }

  private void loadBingPic() {
    HttpUtil.sendOkHttpRequest(ConstantUtil.URL_BING_PIC, new HttpCallBack() {
      @Override
      public void onFail(Call call, IOException e) {

      }

      @Override
      public void onResult(Call call, String response) throws IOException {
        PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
            .edit()
            .putString(ConstantUtil.BACKGROUND_PIC, response)
            .apply();
        Glide.with(WeatherActivity.this).load(response).into(mIvBingPic);
      }
    });
  }
}
