package com.wanghaohua.example.coolweather.database;

import org.litepal.crud.DataSupport;

/**
 * 创建时间: 2018/09/29 15:47
 * 作者: wanghaohua
 * 描述:
 */
public class Province extends DataSupport {
  private int id;
  private String provinceName;
  private int provinceCode;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getProvinceName() {
    return provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  public int getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(int provinceCode) {
    this.provinceCode = provinceCode;
  }
}
