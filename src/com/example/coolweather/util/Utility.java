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
		                    //startDocument(parser);//这里总是执行不到，可以去掉  
		                    break; 
		                case KXmlParser.START_TAG:{
		                    pyName=parser.getAttributeValue(null, "pyName");
		                    quName=parser.getAttributeValue(null, "quName");
		                    if(pyName!=null&quName!=null){
			                    Province province=new Province();
			                    province.setProvinceName(quName);
			                    province.setProvinceCode(pyName);
			                    coolWeatherDB.saveProvince(province);
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
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
				/*
				 * 根据省份查询解析获得的城市信息
				 */
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
		                	JudgmentMunicipalities municipalities=new JudgmentMunicipalities();
		                	if(municipalities.isMunicipalities(parser.getAttributeValue(null, "pyName"))){
		                		 Province province=new Province();
		                		 province.setIsMunicipalities(true);
		                		 pyName=parser.getAttributeValue(null, "url");
				                 cityName=parser.getAttributeValue(null, "cityname");
				                 if(pyName!=null&cityName!=null){
					                 province.setProvinceName(cityName);
					                 province.setProvinceCode(pyName);
					                 coolWeatherDB.saveProvince(province);
				                 }
		                	}else{
		                		Province province=new Province();
		                		province.setIsMunicipalities(false);
			                    pyName=parser.getAttributeValue(null, "pyName");
			                    cityName=parser.getAttributeValue(null, "cityname");
			                    if(pyName!=null&cityName!=null){
				                    City city=new City();
				                    city.setCityName(cityName);
				                    city.setCityCode(pyName);
				                    city.setProvinceId(provinceId);
				                    coolWeatherDB.saveCity(city);
			                    }
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
						//	JSONObject jsonObject1=jsonObject.getJSONObject("24h");
						//	JSONObject jsonObject2=jsonObject1.getJSONObject(weatherCode);
							JSONArray jsonArray=jsonObject.getJSONArray("1001001");
							int tempLength=jsonArray.length();
							String[] tempDay=new String[tempLength];
							String[] tempNight=new String[tempLength];
							String[] weatherPhenomenonCode=new String[tempLength];
							for(int i=0;i<tempLength;i++){
								JSONObject jsonObjectResponse=(JSONObject)jsonArray.opt(i);
								tempDay[i] =jsonObjectResponse.getString("003");
								tempNight[i] =jsonObjectResponse.getString("004");
								WeatherPhenomenon weatherPhenomenon=new WeatherPhenomenon();
								weatherPhenomenonCode[i]=weatherPhenomenon.returnWeatherPhenomenon(jsonObjectResponse.getString("001"));
								Log.d("Utility","白天温度  "+tempDay[i]);
								Log.d("Utility","夜晚温度 "+tempNight[i]);
								Log.d("Utility","天气现象编码 "+weatherPhenomenonCode[i]);	
							}
							String pTime=jsonObject.getString("000");
							saveWeatherInfo(context,cityName,weatherCode,tempDay,tempNight,weatherPhenomenonCode,pTime,tempLength);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	}
	public static void saveWeatherInfo(Context context,String cityName,String weaherCode,String[] tempDay,String[] tempNight,String[] weatherPhenomenonCode,String pTime,int tempLength){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weaherCode);
		editor.putString("publish_time", pTime);
		editor.putString("current_date",sdf.format(new Date()) );
		editor.putInt("temp_length", tempLength);
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
