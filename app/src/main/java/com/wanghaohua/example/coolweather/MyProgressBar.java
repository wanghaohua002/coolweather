package com.wanghaohua.example.coolweather;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

/**
 * 创建时间: 2018/09/29 19:46
 * 作者: wanghaohua
 * 描述:
 */
class MyProgressBar extends Dialog {

  private Activity mContext;

  private View view;

  private ProgressBar mProgressBar;

  public MyProgressBar(@NonNull Context context) {
    super(context);
    mContext = (Activity) context;
    initView();
  }

  private void initView() {
    view = LayoutInflater.from(mContext).inflate(R.layout.progress_bar, null);
    mProgressBar = view.findViewById(R.id.progressBar1);
  }

  public MyProgressBar(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(view);
    setCanceledOnTouchOutside(false);
  }

  public void show() {
    super.show();
    mProgressBar.setVisibility(View.VISIBLE);
  }

  public void dismiss() {
    super.dismiss();
  }
}
