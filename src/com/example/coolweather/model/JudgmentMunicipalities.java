package com.example.coolweather.model;

public class JudgmentMunicipalities {

	public boolean isMunicipalities(String cityName){
		if(cityName.equals("chongqing")){
			return true;
		}else if(cityName.equals("shanghai")){
			return true;
		}else if(cityName.equals("tianjin")){
			return true;
		}else if(cityName.equals("beijing")){
			return true;
		}else if(cityName.equals("xianggang")){
			return true;
		}else if(cityName.equals("aomen")){
			return true;
		}else if(cityName.equals("taiwan")){
			return true;
		}else if(cityName.equals("xisha")){
			return true;
		}else if(cityName.equals("nanshadao")){
			return true;
		}else if(cityName.equals("diaoyudao")){
			return true;
		}
		return false;
	}

}
