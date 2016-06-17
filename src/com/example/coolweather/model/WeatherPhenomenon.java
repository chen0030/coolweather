package com.example.coolweather.model;

public class WeatherPhenomenon {
	private String sunny="晴";
	private String cloudy="多云";
	private String overcast="阴";
	private String shower="、阵雨";
	private String thunderShower="雷阵雨";
	private String thunderShower_with_hail="雷阵雨伴有冰雹";
	private String sleet="雨夹雪";
	private String lightRain="小雨";
	private String moderateRain="中雨";
	private String heavyRain="大雨";
	private String storm="暴雨";
	private String heavyStorm="大暴雨";
	private String severeStorm="特大暴雨";
	private String snowFlurry="阵雪";
	private String lightSnow="小雪";
	private String moderateSnow="中雪";
	private String heavySnow="大雪";
	private String snowstorm="暴雪";
	private String foggy="雾";
	private String iceRain="冻雨";
	private String duststorm="沙尘暴";
	private String lightToModerateRain="小到中雨";
	private String moderateToHeavyRain="中到大雨";
	private String heavyRainToStorm="大到暴雨";
	private String stormToHeavyStorm="暴雨到大暴雨";
	private String heavyToSevereStorm="大暴雨到特大暴雨";
	private String lightToModerateSnow="小到中雪";
	private String moderateToHeavySnow="中到大雪";
	private String heavySnowToSnowstorm="大到暴雪";
	private String dust="浮沉";
	private String sand="扬沙";
	private String sandstorm="强沙尘暴";
	private String haze="霾";
	private String unkonw="无";
	private String densefog="浓雾";
	private String strongFog="强浓雾";
	private String moderateHaze="中度雾";
	private String severeHaze="严重霾";
	private String denseFog="大雾";
	private String extraHeavyFog="特强浓雾";
	private String rain="雨";
	private String snow="雪";
	
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
