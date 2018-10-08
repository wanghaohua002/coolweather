package com.wanghaohua.example.coolweather.model;

/**
 * 创建时间: 2018/09/30 14:32
 * 作者: wanghaohua
 * 描述:
 */
public class Basic {
  public String city;
  public String id;
  public Update update;

  public class Update {
    public String loc;
  }
}
