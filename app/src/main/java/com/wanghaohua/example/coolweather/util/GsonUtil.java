package com.wanghaohua.example.coolweather.util;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.wanghaohua.example.coolweather.database.City;
import com.wanghaohua.example.coolweather.database.County;
import com.wanghaohua.example.coolweather.database.Province;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建时间: 2018/09/29 16:20
 * 作者: wanghaohua
 * 描述:
 */
public class GsonUtil {

  private static Gson sInstance;

  public static Gson getInstance() {
    if (sInstance == null) {
      synchronized (GsonUtil.class) {
        if (sInstance == null) {
          sInstance = new Gson();
        }
      }
    }
    return sInstance;
  }

  public static <T> T getModel(String json, Class<T> cls) {
    if (TextUtils.isEmpty(json)) {
      return null;
    }
    return getInstance().fromJson(json, cls);
  }

  public static String toJson(Object obj) {
    return getInstance().toJson(obj);
  }

  public static boolean handleProvinceRespone(String response) {
    if (!TextUtils.isEmpty(response)) {
      try {
        JSONArray allProvinces = new JSONArray(response);
        for (int i = 0; i < allProvinces.length(); i++) {
          JSONObject provinceObject = allProvinces.getJSONObject(i);
          Province province = new Province();
          province.setProvinceName(provinceObject.getString("name"));
          province.setProvinceCode(provinceObject.getInt("id"));
          province.save();
        }
        return true;
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static boolean handleCityResponse(String response, int provinceId) {
    if (!TextUtils.isEmpty(response)) {
      try {
        JSONArray allCities = new JSONArray(response);
        for (int i = 0; i < allCities.length(); i++) {
          JSONObject cityObject = allCities.getJSONObject(i);
          City city = new City();
          city.setCityName(cityObject.getString("name"));
          city.setCityCode(cityObject.getInt("id"));
          city.setProvinceId(provinceId);
          city.save();
        }
        return true;
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static boolean handleCountyResponse(String response, int cityId) {
    if (!TextUtils.isEmpty(response)) {
      try {
        JSONArray allCounties = new JSONArray(response);
        for (int i = 0; i < allCounties.length(); i++) {
          JSONObject countyObject = allCounties.getJSONObject(i);
          County county = new County();
          county.setCountyName(countyObject.getString("name"));
          county.setWeatherId(countyObject.getString("weather_id"));
          county.setCityId(cityId);
          county.save();
        }
        return true;
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return false;
  }
}
