package com.example.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private LinearLayout weatherInfoLayout;
	/**
	 * 用于显示城市名
	 */
	private TextView cityNameText;
	/**
	 * 用于显示发布时间
	 */
	private TextView publishText;
	/**
	 * 主页显示当天天气描述
	 */
	private TextView weatherDespText;
	/**
	 * 主页显示当天白天气温
	 */
	private TextView temp1Text;
	/**
	 * 主页显示当天夜晚气温
	 */
	private TextView temp2Text;
	
	/**
	 * 用于显示当天天气描述信息
	 */
	private TextView weatherDespTodayText;
	/**
	 * 用于显示第二天天气描述信息
	 */
	private TextView weatherDespTomorrowText;
	/**
	 * 用于显示第三天天气描述信息
	 */
	private TextView weatherDespAcquiredText;
	/**
	 * 用于显示当天白天气温
	 */
	private TextView tempDayTodayText;
	/**
	 * 用于显示当天夜晚气温
	 */
	private TextView tempNightTodayText;
	/**
	 * 用于显示第二天白天气温
	 */
	private TextView tempDayTomorrowText;
	/**
	 * 用于显示第二天夜晚气温
	 */
	private TextView tempNightTomorrowText;
	/**
	 * 用于显示第三天白天气温
	 */
	private TextView tempDayAcquiredText;
	/**
	 * 用于显示第三天夜晚气温
	 */
	private TextView tempNightAcquiredText;
	/**
	 * 用于显示当前日期
	 */
	private TextView currentDateText;
	/**
	 * 切换城市按钮
	 */
	private Button switchCity;
	/**
	 * 更新天气按钮
	 */
	private Button refreshWeather;
	private String countyCode;
	private String cityName;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publishe_text);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		weatherDespTodayText=(TextView)findViewById(R.id.weather_desp_today);
		weatherDespTomorrowText=(TextView)findViewById(R.id.weather_desp_tomorrow);
		weatherDespAcquiredText=(TextView)findViewById(R.id.weather_desp_acquired);
		tempDayTodayText=(TextView)findViewById(R.id.temp_day_today);
		tempNightTodayText=(TextView)findViewById(R.id.temp_night_today);
		tempDayTomorrowText=(TextView)findViewById(R.id.temp_day_tomorrow);
		tempNightTomorrowText=(TextView)findViewById(R.id.temp_night_tomorrow);
		tempDayAcquiredText=(TextView)findViewById(R.id.temp_day_acquired);
		tempNightAcquiredText=(TextView)findViewById(R.id.temp_night_acquired);
		currentDateText=(TextView)findViewById(R.id.current_date);
		switchCity=(Button)findViewById(R.id.switch_city);
		refreshWeather=(Button)findViewById(R.id.refresh_weather);
		countyCode=getIntent().getStringExtra("county_code");
		cityName=getIntent().getStringExtra("city_name");
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号时就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryFromServer(countyCode,cityName);
		}
		else{
			showWeather();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent=new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			String weatherCode=countyCode;
			if(!TextUtils.isEmpty(weatherCode)){
				queryFromServer(weatherCode, cityName);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 根据传入的地址和类型去向服务器查询天气信息
	 */
	private void queryFromServer(final String weatherCode,final String cityName){
		String address="http://apis.baidu.com/tianyiweather/basicforecast/weatherapi"+"?area="+weatherCode;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(WeatherActivity.this,response,cityName, weatherCode);
				runOnUiThread(new Runnable() {
					public void run() {
						showWeather();
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示
	 */
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		String publishTime=prefs.getString("publish_time", "");
		publishText.setText("今天"+publishTime.substring(8,10)+":"+publishTime.substring(10)+"发布");
		currentDateText.setText(publishTime.substring(0,4)+"年"+publishTime.substring(4,6)+"月"+publishTime.substring(6,8)+"日");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);

		//读取三天的天气信息
		String regularEx = "#";
		String[] strTempDay = null;
		String[] strTempNight=null;
		String[] strWeatherCode=null;
		String tempDayValues = prefs.getString("temp_day", "");
		String tempNightValues=prefs.getString("temp_night", "");
		String weatherCodeValues=prefs.getString("weather_phenomenon_code", "");
		strTempDay = tempDayValues.split(regularEx);
		strTempNight=tempNightValues.split(regularEx);
		strWeatherCode=weatherCodeValues.split(regularEx);
		int length=prefs.getInt("temp_length", 0);
		if(prefs.getInt("temp_lenght", 1)>0){
			for(int i=0;i<length;i++){
				if(i==0){
					tempDayTodayText.setText(strTempDay[i]);
					temp1Text.setText(strTempDay[i]);
				}else if(i==1){
					tempDayTomorrowText.setText(strTempDay[i]);
				}else if(i==2){
					tempDayAcquiredText.setText(strTempDay[i]);
				}
			}
			for(int i=0;i<length;i++){
				if(i==0){
					tempNightTodayText.setText(strTempNight[i]);
					temp2Text.setText(strTempNight[i]);
				}else if(i==1){
					tempNightTomorrowText.setText(strTempNight[i]);
				}else if(i==2){
					tempNightAcquiredText.setText(strTempNight[i]);
				}
			}
			for(int i=0;i<length;i++){
				if(i==0){
					weatherDespTodayText.setText(strWeatherCode[i]);
					weatherDespText.setText(strWeatherCode[i]);
				}else if(i==1){
					weatherDespTomorrowText.setText(strWeatherCode[i]);
				}else if(i==2){
					weatherDespAcquiredText.setText(strWeatherCode[i]);
				}
			}
		}
	}
}
