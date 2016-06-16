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
import com.example.coolweather.model.Province;
public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
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
		                    //startDocument(parser);//��������ִ�в���������ȥ��  
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
	 * 			����JSON����
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
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
				/*
				 * ����ʡ�ݲ�ѯ������õĳ�����Ϣ
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
		                    //startDocument(parser);//��������ִ�в���������ȥ��  
		                    break; 
		                case KXmlParser.START_TAG:{    
		                    pyName=parser.getAttributeValue(null, "pyName");
		                    cityName=parser.getAttributeValue(null, "cityname");
		                    if(pyName!=null&cityName!=null){
			                    City city=new City();
			                    city.setCityName(cityName);
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
	 * �����ʹ�����������ص��ؼ�����
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
		                    //startDocument(parser);//��������ִ�в���������ȥ��  
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
	 * �������������ص�JSON���ݣ������������������ݴ洢�ڱ���
	 */
	public static void handleWeatherResponse(Context context,String response,String cityName,String weatherCode){
		try {
							JSONObject jsonObject=new JSONObject(response).getJSONObject("forecast");
							JSONObject jsonObject1=jsonObject.getJSONObject("24h");
							JSONObject jsonObject2=jsonObject1.getJSONObject(weatherCode);
							JSONArray jsonArray=jsonObject2.getJSONArray("1001001");
							String temp1="";
							String temp2="";
							String weatherDesp="";
							for(int i=0;i<jsonArray.length();i++){
								JSONObject jsonObjectResponse=(JSONObject)jsonArray.opt(i);
								temp1 =jsonObjectResponse.getString("003");
								temp2 =jsonObjectResponse.getString("004");
								weatherDesp=jsonObjectResponse.getString("001");
								Log.d("Utility","�����¶�  "+temp2);
								Log.d("Utility","ҹ���¶� "+temp2);
								Log.d("Utility","����������� "+weatherDesp);	
							}
							String pTime=jsonObject2.getString("000");
							saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,pTime);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	}
	public static void saveWeatherInfo(Context context,String cityName,String weaherCode,String temp1,String temp2,String weatherDesp,String pTime){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weaherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", pTime);
		editor.putString("current_date",sdf.format(new Date()) );
		editor.commit();
	}

}
