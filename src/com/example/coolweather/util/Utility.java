package com.example.coolweather.util;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.bool;
import android.provider.DocumentsContract.Document;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.MyApplication;
import com.example.coolweather.model.Province;
public class Utility {
	/**
	 * �����ʹ������������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			 KXmlParser parser = new KXmlParser();  
			 try {
				 parser.setInput(new StringReader(response));
				 int eventType=parser.getEventType();
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
		                    break;
		                }
		                case XmlPullParser.END_TAG:{
		                	String tag=parser.getAttributeValue(null, "china");
							if("china".equals(tag)){
								Log.d("Utility","quNmae is "+quName);
								Log.d("Utility","pyNmae is "+pyName);
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
			}
//			try {
//				XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
//				XmlPullParser xmlPullParser=factory.newPullParser();
//				xmlPullParser.setInput(new StringReader(response));
//				int eventType=xmlPullParser.getEventType();
//				String quName="";
//				String pyName="";
//				while(eventType!=XmlPullParser.END_DOCUMENT){
//					String nodeName=xmlPullParser.getName();
//					switch (eventType) {
//					case XmlPullParser.START_TAG:{
//						if("quName".equals(nodeName)){
//							quName=xmlPullParser.nextText();
//						}else if("pyName".equals(nodeName)){
//							pyName=xmlPullParser.nextText();
//						}
//						break;
//					}
//					case XmlPullParser.END_TAG:{
//						if("china".equals(nodeName)){
//							Log.d("Utility","quNmae is "+quName);
//							Log.d("Utility","pyNmae is "+pyName);
//						}
//						break;
//					}
//					default:
//						break;
//					}
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
			return true;
		}
		return false;	
	}
	/**
	 * ����JSON����
	 * //			try {
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
	 * �����ʹ������������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//���������������ݴ洢��City��
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ������������ص��ؼ�����
	 */
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String c:allCounties){
					String[] array=c.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//���������������ݴ洢��City��
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

}