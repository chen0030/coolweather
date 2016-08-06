package com.example.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.JudgmentMunicipalities;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY=2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList=new ArrayList<String>();
	/**
	 * 省列表
	 */
	private List<Province> provinceList;
	/**
	 * 市列表
	 */
	private List<City> cityList;
	/**
	 * 县列表
	 */
	private List<County> countyList;
	/**
	 * 选中的省份
	 */
	private Province selectedProvince;
	/**
	 * 选中的城市
	 */
	private City selectedCity;
	/**
	 * 当前选中的级别
	 */
	private int currentLevel;
	/**
	 * 是否从WeatherActivity中跳转过来
	 */
	private  boolean isFromWeatherActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		//已经选择了城市切不是从WeatherActivity中跳转过来，才会直接跳转到WeatherActivity
		if(prefs.getBoolean("city_selected", false)&&!isFromWeatherActivity){
			Intent intent=new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
		titleText=(TextView)findViewById(R.id.title_text);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int index, long arg3) {
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=provinceList.get(index);
					if(selectedProvince.getProvinceCode().equals("diaoyudao")||selectedProvince.getProvinceCode().equals("nanshadao")||selectedProvince.getProvinceCode().equals("xisha")){
						Toast.makeText(ChooseAreaActivity.this, "暂不支持钓鱼岛、南沙、西沙地区选择", Toast.LENGTH_SHORT).show();
					}else{
						queryCities();
					}
				}else if(currentLevel==LEVEL_CITY) {
					selectedCity=cityList.get(index);
					JudgmentMunicipalities municipalities=new JudgmentMunicipalities();
					boolean isMunicipalities=municipalities.isMunicipalities(selectedProvince.getProvinceCode());
					if(isMunicipalities){
						Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
						intent.putExtra("county_code", selectedCity.getCityCode());
						intent.putExtra("city_name", selectedCity.getCityName());
						startActivity(intent);
						finish();
					}else{
						queryCounties();
					}
				}else if(currentLevel==LEVEL_COUNTY){
					County selectCounty=countyList.get(index);
					Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("county_code", selectCounty.getCountyCode());
					intent.putExtra("city_name", selectCounty.getCountyName());
					startActivity(intent);
					finish();
				}
				
			}
		});
		queryProvinces();
	}
	/**
	 * 查询全国所有的省份，优先从数据库中查询，如果没有再去服务器上查询
	 */
	private void queryProvinces(){
		provinceList=coolWeatherDB.loadProvinces();
		if(provinceList.size()>2){
			dataList.clear();
			for(Province province:provinceList){
				if(province.getProvinceName()!=null){
					dataList.add(province.getProvinceName());
				}
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setTag("中国");
			currentLevel=LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	/**
	 * 查询选中省内所有的城市，优先从数据库中查询，如果没有再去服务器上查询
	 */  
	private void queryCities(){
		cityList=coolWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				if(city.getCityName()!=null){
					dataList.add(city.getCityName());
				}
				
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setTag(selectedProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	/**
	 * 查询市内所有的县，优先从数据库中查询，如果没有再去服务器上查询
	 */
	private void queryCounties(){
		countyList=coolWeatherDB.loadCounties(selectedCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County county:countyList){
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setTag(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTY;
			
		}else{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据。
	 */
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://flash.weather.com.cn/wmaps/xml/"+code+".xml";
		}else{
			address="http://flash.weather.com.cn/wmaps/xml/china.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvincesResponse(coolWeatherDB, response);
				}else if("city".equals(type)){
					result=Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId(),code);
				}else if("county".equals(type)){
					result=Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if(result){
					//通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	/**
	 * 显示进度条
	 */
	private void showProgressDialog(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载。。。");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**
	 * 关闭进度对话框
	 */
	 private void closeProgressDialog(){
		 if(progressDialog!=null){
			 progressDialog.dismiss();
		 }
	 }
	 /**
	  * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表，省列表、还是直接退出。
	  */
	 @Override
	 public void onBackPressed(){
		 if(currentLevel==LEVEL_COUNTY){
			 queryCities();
		 }else if(currentLevel==LEVEL_CITY){
			 queryProvinces();
		 }else{
			 if(isFromWeatherActivity){
				 Intent intent=new Intent(this,WeatherActivity.class);
				 startActivity(intent);
			 }
			 finish();
		 }
	 }
}
