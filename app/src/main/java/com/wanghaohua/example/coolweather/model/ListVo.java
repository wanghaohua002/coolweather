package com.wanghaohua.example.coolweather.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 创建时间: 2018/10/08 10:32
 * 作者: wanghaohua
 * 描述:
 */
public class ListVo<T> {
  @SerializedName("HeWeather") public List<T> list;
}
