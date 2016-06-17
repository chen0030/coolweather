package com.example.coolweather.model;

public class WeatherPhenomenon {
	private String sunny="��";
	private String cloudy="����";
	private String overcast="��";
	private String shower="������";
	private String thunderShower="������";
	private String thunderShower_with_hail="��������б���";
	private String sleet="���ѩ";
	private String lightRain="С��";
	private String moderateRain="����";
	private String heavyRain="����";
	private String storm="����";
	private String heavyStorm="����";
	private String severeStorm="�ش���";
	private String snowFlurry="��ѩ";
	private String lightSnow="Сѩ";
	private String moderateSnow="��ѩ";
	private String heavySnow="��ѩ";
	private String snowstorm="��ѩ";
	private String foggy="��";
	private String iceRain="����";
	private String duststorm="ɳ����";
	private String lightToModerateRain="С������";
	private String moderateToHeavyRain="�е�����";
	private String heavyRainToStorm="�󵽱���";
	private String stormToHeavyStorm="���굽����";
	private String heavyToSevereStorm="���굽�ش���";
	private String lightToModerateSnow="С����ѩ";
	private String moderateToHeavySnow="�е���ѩ";
	private String heavySnowToSnowstorm="�󵽱�ѩ";
	private String dust="����";
	private String sand="��ɳ";
	private String sandstorm="ǿɳ����";
	private String haze="��";
	private String unkonw="��";
	private String densefog="Ũ��";
	private String strongFog="ǿŨ��";
	private String moderateHaze="�ж���";
	private String severeHaze="������";
	private String denseFog="����";
	private String extraHeavyFog="��ǿŨ��";
	private String rain="��";
	private String snow="ѩ";
	
	public String returnWeatherPhenomenon(String weatherPhenomenonCode){
		
		switch (Integer.parseInt(weatherPhenomenonCode)) {
		case 0:return sunny;
		case 1:return cloudy;
		case 2:return overcast;
		case 3:return shower;
		case 4:return thunderShower;
		case 5:return thunderShower_with_hail;
		case 6:return sleet;
		case 7:return lightRain;
		case 8:return moderateRain;
		case 9:return heavyRain;
		case 10:return storm;
		case 11:return heavyStorm;
		case 12:return severeStorm;
		case 13:return snowFlurry;
		case 14:return lightSnow;
		case 15:return moderateSnow;
		case 16:return heavySnow;
		case 17:return snowstorm;
		case 18:return foggy;
		case 19:return iceRain;
		case 20:return duststorm;
		case 21:return lightToModerateRain;
		case 22:return moderateToHeavyRain;
		case 23:return heavyRainToStorm;
		case 24:return stormToHeavyStorm;
		case 25:return heavyToSevereStorm;
		case 26:return lightToModerateSnow;
		case 27:return moderateToHeavySnow;
		case 28:return heavySnowToSnowstorm;
		case 29:return dust;
		case 30:return sand;
		case 31:return sandstorm;
		case 53:return haze;
		case 99:return unkonw;
		case 32:return densefog;
		case 49:return strongFog;
		case 54:return moderateHaze;
		case 55:return severeHaze;
		case 56:return severeHaze;
		case 57:return denseFog;
		case 58:return extraHeavyFog;
		case 301:return rain;
		case 302:return snow;		
		default:
			break;
		}
		return weatherPhenomenonCode;
	}
	
	
}
