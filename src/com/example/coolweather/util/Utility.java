package com.example.coolweather.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kxml2.io.KXmlParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.JudgmentMunicipalities;
import com.example.coolweather.model.Province;
import com.example.coolweather.model.WeatherPhenomenon;
public class Utility {
	/**
	 * 判断是否为直辖市
	 */
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			 KXmlParser parser = new KXmlParser();  
			 try {
				 parser.setInput(new StringReader(response));
				 String quName="";
				 String pyName="";
				 boolean keepParsing = true;
				 while(keepParsing){  
		                int type = parser.next();  
		                switch(type){
		                case KXmlParser.START_DOCUMENT:  
		                    break; 
		                case KXmlParser.START_TAG:{
		                	pyName=parser.getAttributeValue(null, "pyName");
		                	quName=parser.getAttributeValue(null, "quName");
				            Province province=new Province();
				            province.setProvinceName(quName);
				            province.setProvinceCode(pyName);
				            coolWeatherDB.saveProvince(province);
			                 }
		                    break;
		                case KXmlParser.END_DOCUMENT:{
		                	keepParsing=false;
		                	}
		                }
				 	}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			return true;
		}
		return false;	
	}
	/**
	 * 			解析JSON数据
	 *  //			try {
		//				JSONObject jsonObject=new JSONObject(response);
		//				JSONArray jsonArray=jsonObject.getJSONArray("retData");
		//				for(int i=0;i<jsonArray.length();i++){
		//					JSONObject jsonObjectResponse=(JSONObject)jsonArray.opt(i);
		//					int id=jsonObjectResponse.getInt("area_id");
		//					String name=jsonObjectResponse.getString("name_cn");
		//					Log.d("Utility","area_id is "+id);
		//					Log.d("Utility","name_cn is "+name);
		//					
		//				}
		//			} catch (Exception e) {
		//				// TODO: handle exception
		//				e.printStackTrace();
		//			}
	 */
	
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId,String cityName){
		if(!TextUtils.isEmpty(response)){
				/*
				 * 根据省份查询解析获得的城市信息
				 */
			KXmlParser parser = new KXmlParser();  
			 try {
				 parser.setInput(new StringReader(response));
				 String pyName="";
				 String quName="";
				 boolean keepParsing = true;
				 boolean isMunicipalities=false;
				 City city=new City();
				 JudgmentMunicipalities municipalities=new JudgmentMunicipalities();
				 isMunicipalities=municipalities.isMunicipalities(cityName);
				 while(keepParsing){  
		                int type = parser.next();  
		                switch(type){
		                case KXmlParser.START_DOCUMENT:  
		                    break; 
		                case KXmlParser.START_TAG:{ 
		                	if(isMunicipalities){
		                		  if(parser.getAttributeValue(null, "url")!=null&parser.getAttributeValue(null, "cityname")!=null){
			                		 pyName=parser.getAttributeValue(null, "url");
					                 quName=parser.getAttributeValue(null, "cityname");
		                		  }
		                		  }else{
		                			  if(parser.getAttributeValue(null, "pyName")!=null&parser.getAttributeValue(null, "cityname")!=null){
		                				  pyName=parser.getAttributeValue(null, "pyName");
						                    quName=parser.getAttributeValue(null, "cityname");}
		                			  }
			                 if(pyName.length()>0&quName.length()>0){
			                     city.setCityName(quName);
				                 city.setCityCode(pyName);
				                 city.setProvinceId(provinceId);
				                 coolWeatherDB.saveCity(city);
			                 }
			                    break;
		                }
		                case KXmlParser.END_DOCUMENT:{
		                	keepParsing=false;
		                	}
		                }
				 	}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			return true;
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			KXmlParser parser = new KXmlParser();  
			 try {
				 parser.setInput(new StringReader(response));
				 String cityName="";
				 String pyName="";
				 boolean keepParsing = true;
				 while(keepParsing){  
		                int type = parser.next();  
		                switch(type){
		                case KXmlParser.START_DOCUMENT:  
		                    //startDocument(parser);//这里总是执行不到，可以去掉  
		                    break; 
		                case KXmlParser.START_TAG:{    
		                    pyName=parser.getAttributeValue(null, "url");
		                    cityName=parser.getAttributeValue(null, "cityname");
		                    if(pyName!=null&cityName!=null){
			                    County county=new County();
			                    county.setCountyName(cityName);
			                    county.setCountyCode(pyName);
			                    county.setCityId(cityId);
			                    coolWeatherDB.saveCounty(county);
		                    }
		                    break;
		                }
		                case KXmlParser.END_DOCUMENT:{
		                	keepParsing=false;
		                	}
		                }
				 	}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return true;
			}
		return false;
	}
	/**
	 * 解析服务器返回的JSON数据，并将解析出来的数据存储在本地
	 */
	public static void handleWeatherResponse(Context context,String response,String cityName,String weatherCode){
		try {
							JSONObject jsonObject=new JSONObject(response).getJSONObject("forecast").getJSONObject("24h").getJSONObject(weatherCode);
							JSONObject jsonObjectObserve=new JSONObject(response).getJSONObject("observe").getJSONObject(weatherCode).getJSONObject("1001002");
							JSONArray jsonArray=jsonObject.getJSONArray("1001001");
							int tempLength=jsonArray.length();
							String[] tempDay=new String[tempLength];
							String[] tempNight=new String[tempLength];
							String[] weatherPhenomenonDayCode=new String[tempLength];
							String[] weatherPhenomenonNightCode=new String[tempLength];
							WeatherPhenomenon weatherPhenomenon=new WeatherPhenomenon();
							for(int i=0;i<tempLength;i++){
								JSONObject jsonObjectResponse=(JSONObject)jsonArray.opt(i);
								if(jsonObjectResponse.getString("003").length()>0){
									tempDay[i] =jsonObjectResponse.getString("003");  //003白天温度
									tempNight[i] =jsonObjectResponse.getString("004");//004晚上温度
								}else{
									tempDay[i] ="No Temp";
									tempNight[i] =jsonObjectResponse.getString("004");
								}
								
								if(jsonObjectResponse.getString("001").length()>0){
									weatherPhenomenonDayCode[i]=weatherPhenomenon.returnWeatherPhenomenon(jsonObjectResponse.getString("001"));//001白天天气现象编码
								}else{
									weatherPhenomenonDayCode[i]="The day is over";
									weatherPhenomenonNightCode[i]=weatherPhenomenon.returnWeatherPhenomenon(jsonObjectResponse.getString("002"));
								}
								 //00晚上天气现象编码	
								Log.d("Utility","白天温度  "+tempDay[i]);
								Log.d("Utility","夜晚温度 "+tempNight[i]);
								Log.d("Utility","天气现象编码 "+weatherPhenomenonDayCode[i]);	
							}
							String tempRealTime=jsonObjectObserve.getString("002");
							String weatherCodeRealTime=weatherPhenomenon.returnWeatherPhenomenon(jsonObjectObserve.getString("001"));
							String pTime=jsonObject.getString("000");
							saveWeatherInfo(context,cityName,weatherCode,tempDay,tempNight,weatherPhenomenonDayCode,pTime,tempLength,tempRealTime,weatherCodeRealTime);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	}
	public static void saveWeatherInfo(Context context,String cityName,String weaherCode,String[] tempDay,String[] tempNight,String[] weatherPhenomenonCode,String pTime,int tempLength,String tempRealTime,String weatherCodeRealTime){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weaherCode);
		editor.putString("publish_time", pTime);
		editor.putString("current_date",sdf.format(new Date()) );
		editor.putInt("temp_length", tempLength);
		editor.putString("temp_real_time", tempRealTime);
		editor.putString("weather_code_real_time",weatherCodeRealTime);
		//存放三天的天气信息
		String regularEx = "#";
		String str = "";
		if(tempDay != null && tempDay.length > 0) {
			for (String value : tempDay) {
				str += value;
				str += regularEx;
			}
			editor.putString("temp_day", str);	
			str="";
		}
		if(tempNight != null && tempNight.length > 0) {
			for (String value : tempNight) {
				str += value;
				str += regularEx;
			}
			editor.putString("temp_night", str);	
			str="";
		}
		if(weatherPhenomenonCode != null && weatherPhenomenonCode.length > 0) {
			for (String value : weatherPhenomenonCode) {
				str += value;
				str += regularEx;
			}
			editor.putString("weather_phenomenon_code", str);	
			str="";
		}
		editor.commit();
	}
}
