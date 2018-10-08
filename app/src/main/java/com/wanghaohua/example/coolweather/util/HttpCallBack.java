package com.wanghaohua.example.coolweather.util;

import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 创建时间: 2018/09/30 10:39
 * 作者: wanghaohua
 * 描述:
 */
public abstract class HttpCallBack implements Callback {

  private Handler mHandler = new Handler(Looper.getMainLooper());

  private void runOnUiThread(Runnable action) {
    if (Looper.getMainLooper() != Looper.myLooper()) {
      mHandler.post(action);
    } else {
      action.run();
    }
  }

  @Override
  public void onFailure(final Call call, final IOException e) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        onFail(call, e);
      }
    });
  }

  @Override
  public void onResponse(final Call call, final Response response) throws IOException {
    final String responseString = response.body().string();
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        try {
          onResult(call, responseString);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public abstract void onFail(Call call, IOException e);

  public abstract void onResult(Call call, String response) throws IOException;
}
