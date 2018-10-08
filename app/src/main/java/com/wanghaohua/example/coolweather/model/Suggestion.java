package com.wanghaohua.example.coolweather.model;

/**
 * 创建时间: 2018/09/30 14:35
 * 作者: wanghaohua
 * 描述:
 */
public class Suggestion {
  public Comfort comf;
  public Cw cw;
  public Sport sport;

  public class Comfort {
    public String txt;
  }

  public class Cw {
    public String txt;
  }

  public class Sport {
    public String txt;
  }
}
