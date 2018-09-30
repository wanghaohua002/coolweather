package com.wanghaohua.example.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 创建时间: 2018/09/29 16:12
 * 作者: wanghaohua
 * 描述:
 */
public class HttpUtil {
  public static void sendOkHttpRequest(String url, HttpCallBack callback) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).build();
    client.newCall(request).enqueue(callback);
  }
}
