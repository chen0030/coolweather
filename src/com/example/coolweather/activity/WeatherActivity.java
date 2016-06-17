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
	 * ������ʾ������
	 */
	private TextView cityNameText;
	/**
	 * ������ʾ����ʱ��
	 */
	private TextView publishText;
	/**
	 * ��ҳ��ʾ������������
	 */
	private TextView weatherDespText;
	/**
	 * ��ҳ��ʾ�����������
	 */
	private TextView temp1Text;
	/**
	 * ��ҳ��ʾ����ҹ������
	 */
	private TextView temp2Text;
	
	/**
	 * ������ʾ��������������Ϣ
	 */
	private TextView weatherDespTodayText;
	/**
	 * ������ʾ�ڶ�������������Ϣ
	 */
	private TextView weatherDespTomorrowText;
	/**
	 * ������ʾ����������������Ϣ
	 */
	private TextView weatherDespAcquiredText;
	/**
	 * ������ʾ�����������
	 */
	private TextView tempDayTodayText;
	/**
	 * ������ʾ����ҹ������
	 */
	private TextView tempNightTodayText;
	/**
	 * ������ʾ�ڶ����������
	 */
	private TextView tempDayTomorrowText;
	/**
	 * ������ʾ�ڶ���ҹ������
	 */
	private TextView tempNightTomorrowText;
	/**
	 * ������ʾ�������������
	 */
	private TextView tempDayAcquiredText;
	/**
	 * ������ʾ������ҹ������
	 */
	private TextView tempNightAcquiredText;
	/**
	 * ������ʾ��ǰ����
	 */
	private TextView currentDateText;
	/**
	 * �л����а�ť
	 */
	private Button switchCity;
	/**
	 * ����������ť
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
			//���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ����...");
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
			publishText.setText("ͬ����...");
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
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ������Ϣ
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
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ
	 */
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		String publishTime=prefs.getString("publish_time", "");
		publishText.setText("����"+publishTime.substring(8,10)+":"+publishTime.substring(10)+"����");
		currentDateText.setText(publishTime.substring(0,4)+"��"+publishTime.substring(4,6)+"��"+publishTime.substring(6,8)+"��");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);

		//��ȡ�����������Ϣ
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
