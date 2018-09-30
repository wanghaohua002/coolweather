package com.wanghaohua.example.coolweather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wanghaohua.example.coolweather.database.City;
import com.wanghaohua.example.coolweather.database.County;
import com.wanghaohua.example.coolweather.database.Province;
import com.wanghaohua.example.coolweather.util.ConstantUtil;
import com.wanghaohua.example.coolweather.util.GsonUtil;
import com.wanghaohua.example.coolweather.util.HttpCallBack;
import com.wanghaohua.example.coolweather.util.HttpUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import org.litepal.crud.DataSupport;

/**
 * 创建时间: 2018/09/29 17:05
 * 作者: wanghaohua
 * 描述:
 */
public class ChooseAreaFragment extends Fragment {
  public static final int LEVEL_PROVINCE = 0;
  public static final int LEVEL_CITY = 1;
  public static final int LEVEL_COUNTY = 2;

  private MyProgressBar mProgressBar;

  private TextView mTvTitle;
  private Button mBtnBack;
  private ListView mLvContent;
  private ArrayAdapter<String> mAdapter;
  private List<String> mDataList = new ArrayList<>();

  private List<Province> mProvinceList;
  private List<City> mCityList;
  private List<County> mCountyList;

  private Province mSelectedProvince;
  private City mSelectedCity;
  private int mCurrentLevel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
    mTvTitle = view.findViewById(R.id.tv_title);
    mBtnBack = view.findViewById(R.id.btn_back);
    mLvContent = view.findViewById(R.id.lv_content);
    createProgressBar();

    mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mDataList);
    mLvContent.setAdapter(mAdapter);
    return view;
  }

  private void createProgressBar() {
    mProgressBar = new MyProgressBar(getActivity());
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCurrentLevel == LEVEL_PROVINCE) {
          mSelectedProvince = mProvinceList.get(position);
          queryCities();
        } else if (mCurrentLevel == LEVEL_CITY) {
          mSelectedCity = mCityList.get(position);
          queryCounties();
        }
      }
    });
    mBtnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    queryProvinces();
  }

  public void onBackPressed() {
    if (mCurrentLevel == LEVEL_COUNTY) {
      queryCities();
    } else if (mCurrentLevel == LEVEL_CITY) {
      queryProvinces();
    }
  }

  private void queryProvinces() {
    mTvTitle.setText("中国");
    mBtnBack.setVisibility(View.GONE);
    mProvinceList = DataSupport.findAll(Province.class);
    if (mProvinceList != null && mProvinceList.size() > 0) {
      mDataList.clear();
      for (Province province : mProvinceList) {
        mDataList.add(province.getProvinceName());
      }
      mAdapter.notifyDataSetChanged();
      mLvContent.setSelection(0);
      mCurrentLevel = LEVEL_PROVINCE;
    } else {
      mProgressBar.show();
      HttpUtil.sendOkHttpRequest(ConstantUtil.URL_BASEURL, new HttpCallBack() {
        @Override
        public void onFail(Call call, IOException e) {
          mProgressBar.dismiss();
        }

        @Override
        public void onResult(Call call, Response response) throws IOException {
          boolean result = GsonUtil.handleProvinceRespone(response.body().string());
          mProgressBar.dismiss();
          if (result) {
            queryProvinces();
          }
        }
      });
    }
  }

  public void queryCities() {
    if (mSelectedProvince == null) {
      return;
    }
    mTvTitle.setText(mSelectedProvince.getProvinceName());
    mBtnBack.setVisibility(View.VISIBLE);
    mCityList = DataSupport.where("provinceId = ?", String.valueOf(mSelectedProvince.getId())).find(City.class);
    if (mCityList != null && mCityList.size() > 0) {
      mDataList.clear();
      for (City city : mCityList) {
        mDataList.add(city.getCityName());
      }
      mAdapter.notifyDataSetChanged();
      mLvContent.setSelection(0);
      mCurrentLevel = LEVEL_CITY;
    } else {
      HttpUtil.sendOkHttpRequest(
          ConstantUtil.URL_BASEURL + "/" + mSelectedProvince.getProvinceCode(),
          new HttpCallBack() {
            @Override
            public void onFail(Call call, IOException e) {
              mProgressBar.dismiss();
            }

            @Override
            public void onResult(Call call, Response response) throws IOException {
              boolean result = GsonUtil.handleCityResponse(response.body().string(), mSelectedProvince.getId());
              mProgressBar.dismiss();
              if (result) {
                queryCities();
              }
            }
          });
    }
  }

  public void queryCounties() {
    if (mSelectedCity == null) {
      return;
    }
    mTvTitle.setText(mSelectedCity.getCityName());
    mBtnBack.setVisibility(View.VISIBLE);
    mCountyList = DataSupport.where("cityId = ?", String.valueOf(mSelectedCity.getId())).find(County.class);
    if (mCountyList != null && mCountyList.size() > 0) {
      mDataList.clear();
      for (County county : mCountyList) {
        mDataList.add(county.getCountyName());
      }
      mAdapter.notifyDataSetChanged();
      mLvContent.setSelection(0);
      mCurrentLevel = LEVEL_COUNTY;
    } else {
      HttpUtil.sendOkHttpRequest(
          ConstantUtil.URL_BASEURL + "/" + mSelectedProvince.getProvinceCode() + "/" + mSelectedCity
              .getCityCode(), new HttpCallBack() {
            @Override
            public void onFail(Call call, IOException e) {
              mProgressBar.dismiss();
            }

            @Override
            public void onResult(Call call, Response response) throws IOException {
              boolean result = GsonUtil.handleCountyResponse(response.body().string(), mSelectedCity.getId());
              mProgressBar.dismiss();
              if (result) {
                queryCounties();
              }
            }
          });
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
