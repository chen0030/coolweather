package com.example.coolweather.model;

public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;
	private boolean isMunicipalities;
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getProvinceName(){
		return provinceName;
	}
	public void setProvinceName(String provinceName){
		this.provinceName=provinceName;
	}
	public String getProvinceCode(){
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}
	public boolean getIsMunicipalities(){
		return isMunicipalities;
	}
	public void setIsMunicipalities(boolean jugment){
		this.isMunicipalities=jugment;
	}
}
