package com.wanghaohua.example.coolweather.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 创建时间: 2018/09/30 12:23
 * 作者: wanghaohua
 * 描述:
 */
public class WeatherVo {
  public String status;
  public Basic basic;
  public Aqi aqi;
  public Now now;
  public Suggestion suggestion;
  @SerializedName("daily_forecast") public List<DailyForecast> dailyForecasts;

  public class DailyForecast {
    public String date;
    public Condition cond;
    public Tmp tmp;
    public class Condition {
      @SerializedName("txt_d") public String txt;
    }
    public class Tmp {
      public String max;
      public String min;
    }
  }
}
