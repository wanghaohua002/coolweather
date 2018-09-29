package com.wanghaohua.example.coolweather.database;

import org.litepal.crud.DataSupport;

/**
 * 创建时间: 2018/09/29 16:03
 * 作者: wanghaohua
 * 描述:
 */
public class City extends DataSupport {
  private int id;
  private String cityName;
  private int cityCode;
  private int provinceId;

  public int getCityCode() {
    return cityCode;
  }

  public void setCityCode(int cityCode) {
    this.cityCode = cityCode;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public int getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(int provinceId) {
    this.provinceId = provinceId;
  }
}
