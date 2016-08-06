package com.example.coolweather.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.model.WeatherPhenomenon;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private RelativeLayout weatherInfoLayout;
	/**
	 * ������ʾ������
	 */
	private TextView cityNameText;
	/**
	 * ������ʾ����ʱ��
	 */
	private TextView releaseTime;
	/**
	 * ��ҳ��ʾ������������
	 */
	private TextView weatherDespText;
	/**
	 * ��ҳ��ʾ����ʵʱ����
	 */
	private TextView tempRealTime;
	/**
	 * ��ʾ��ǰ������
	 */
	private TextView currentPrecipition;
	/**
	 * ��ʾ��ǰ�����ȼ�
	 */
	private TextView currentWind;
	/**
	 * ������ʾ��������������Ϣ
	 */
//	private TextView weatherDespTodayText;
	/**
	 * ������ʾ�ڶ�������������Ϣ
	 */
//	private TextView weatherDespTomorrowText;
	/**
	 * ������ʾ����������������Ϣ
	 */
//	private TextView weatherDespAcquiredText;
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
	/**
	 * ��ʾ��������ͼ��
	 */
	private ImageView weatherTodayImage;
	/**
	 * ��ʾ�ڶ�������ͼ��
	 */
	private ImageView weatherTomorrowImage;
	/**
	 * ��ʾ����������ͼ��
	 */
	private ImageView weatherAcquiredImage;
	/**
	 * ��ʾʵʱ����ͼ��
	 */
	private ImageView weatherRealTimeImage;
	/**
	 * ��ʾ�������ڼ�
	 */
	private TextView weekToday;
	/**
	 * ��ʾ�ڶ������ڼ�
	 */
	private TextView weekTomorrow;
	/**
	 * ��ʾ���������ڼ�
	 */
	private TextView weekAcquired;
	
	private TextView tempDay;
	private TextView tempNight;
	private String countyCode;
	private String cityName;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout=(RelativeLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		releaseTime=(TextView)findViewById(R.id.release_time);
		tempRealTime=(TextView)findViewById(R.id.temp_real_time);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
//		weatherDespTodayText=(TextView)findViewById(R.id.weather_desp_today);
//		weatherDespTomorrowText=(TextView)findViewById(R.id.weather_desp_tomorrow);
//		weatherDespAcquiredText=(TextView)findViewById(R.id.weather_desp_acquired);
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
		currentPrecipition=(TextView)findViewById(R.id.current_precipition);
		currentWind=(TextView)findViewById(R.id.current_wind);
		tempDay=(TextView)findViewById(R.id.temp_day);
		tempNight=(TextView)findViewById(R.id.temp_night);
		weatherTodayImage=(ImageView)findViewById(R.id.weather_today_image);
		weatherTomorrowImage=(ImageView)findViewById(R.id.weather_tomorrow_image);
		weatherAcquiredImage=(ImageView)findViewById(R.id.weather_acquired_image);
		weatherRealTimeImage=(ImageView)findViewById(R.id.weather_real_time_image);
		weekToday=(TextView)findViewById(R.id.week_today);
		weekTomorrow=(TextView)findViewById(R.id.week_tomorrow);
		weekAcquired=(TextView)findViewById(R.id.week_acquired);
		if(!TextUtils.isEmpty(countyCode)){
			//���ؼ�����ʱ��ȥ��ѯ����
			releaseTime.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryFromServer(countyCode,cityName);
		}
		else{
				try {
					showWeather();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			String weatherCode=countyCode;
			if(!TextUtils.isEmpty(weatherCode)){
				releaseTime.setText("ͬ����...");
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
							try {
								showWeather();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						releaseTime.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ
	 * @throws ParseException 
	 */
	private void showWeather() throws ParseException {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		WeatherPhenomenon weatherPhenomenon=new WeatherPhenomenon();
		cityNameText.setText(prefs.getString("city_name", "")); 
		String publishTime=prefs.getString("publish_time", "");
		releaseTime.setText("����"+prefs.getString("release_time", "")+"����");
		currentDateText.setText("��������:"+publishTime.substring(0,4)+"��"+publishTime.substring(4,6)+"��"+publishTime.substring(6,8)+"��");
		String currentData=publishTime.substring(0,4)+"-"+publishTime.substring(4,6)+"-"+publishTime.substring(6,8);
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		tempRealTime.setText(prefs.getString("temp_real_time", ""));
		weatherDespText.setText(weatherPhenomenon.returnWeatherPhenomenon(prefs.getString("weather_code_real_time", "")));
		currentPrecipition.setText(prefs.getString("current_precipition", "")+"%");
		currentWind.setText(prefs.getString("current_wind", "")+"��/��");
		int j=0;
		weekToday.setText(getWeek(currentData,j));
		weekTomorrow.setText(getWeek(currentData,j+1));
		weekAcquired.setText(getWeek(currentData,j+2));
		weatherRealTimeImage.setImageLevel(Integer.parseInt(prefs.getString("weather_code_real_time", "")));
		
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
					if(strTempDay[i].equals("$")){
						tempDayTodayText.setText(prefs.getString("temp_real_time", ""));
						tempDay.setText(prefs.getString("temp_real_time", ""));
					}else{
						tempDayTodayText.setText(strTempDay[i]);
						tempDay.setText(strTempDay[i]);
					}
				}else if(i==1){
					tempDayTomorrowText.setText(strTempDay[i]);
				}else if(i==2){
					tempDayAcquiredText.setText(strTempDay[i]);
				}
			}
			for(int i=0;i<length;i++){
				if(i==0){
					if(strTempNight[i].equals("$")){
						tempNightTodayText.setText(prefs.getString("temp_real_time", ""));
						tempNight.setText(prefs.getString("temp_real_time", ""));
					}else{
						tempNightTodayText.setText(strTempNight[i]);
						tempNight.setText(strTempNight[i]);
					}
				}else if(i==1){
					tempNightTomorrowText.setText(strTempNight[i]);
				}else if(i==2){
					tempNightAcquiredText.setText(strTempNight[i]);
				}
			}
			for(int i=0;i<length;i++){
				if(i==0){
//					weatherDespTodayText.setText(weatherPhenomenon.returnWeatherPhenomenon(strWeatherCode[i]));
					weatherTodayImage.setImageLevel(Integer.parseInt(strWeatherCode[i]));
					
				}else if(i==1){
//					weatherDespTomorrowText.setText(weatherPhenomenon.returnWeatherPhenomenon(strWeatherCode[i]));
					weatherTomorrowImage.setImageLevel(Integer.parseInt(strWeatherCode[i]));
				}else if(i==2){
//					weatherDespAcquiredText.setText(weatherPhenomenon.returnWeatherPhenomenon(strWeatherCode[i]));
					weatherAcquiredImage.setImageLevel(Integer.parseInt(strWeatherCode[i]));
				}
			}
			Intent intent=new Intent(this,AutoUpdateService.class);
			startService(intent);
		}
	}
	/**
	 * �ж����ڼ�
	 * @throws ParseException 
	 */
	private String getWeek(String currentData,int i) throws ParseException{
		String week="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date date = sdf.parse(currentData);
		Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date);
	    int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
	    dayIndex=dayIndex+i;
	    switch (dayIndex) {
		case 1:
			week="������";
			break;
		case 2:
			week="����һ";
			break;
		case 3:
			week="���ڶ�";
			break;
		case 4:
			week="������";
			break;
		case 5:
			week="������";
			break;
		case 6:
			week="������";
			break;
		case 7:
			week="������";
			break;
		default:
			break;
		}
	    return week;
	}
}
