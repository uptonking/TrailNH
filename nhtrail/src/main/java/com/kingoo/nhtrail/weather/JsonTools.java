package com.kingoo.nhtrail.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description 自定义类：解析json数据
 * @author KINGOO
 * @date 2015/4/28
 */
public class JsonTools {
    public static WeatherInfo getWeatherInfo(String jsonString){

        WeatherInfo weatherInfo = new WeatherInfo();

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonString);

            int returnResult = jsonObject.getInt("error");
            if(returnResult == 0) {

                JSONArray WeatherArray = jsonObject.getJSONArray("results");
                JSONObject jsonObjectTemp = (JSONObject) WeatherArray.get(0);
                weatherInfo.setCurrentCity(jsonObjectTemp.getString("currentCity"));


                JSONArray WeatherArray01 = jsonObjectTemp.getJSONArray("weather_data");

                //当天天气
                JSONObject jsonObject01 = (JSONObject) WeatherArray01.get(0);
                weatherInfo.setCurrentTemperature(jsonObject01.getString("temperature"));
                weatherInfo.setCurrentType(jsonObject01.getString("weather"));
                weatherInfo.setCurrentWind(jsonObject01.getString(""));

                //第2天天气
                JSONObject jsonObject02 = (JSONObject) WeatherArray01.get(1);
                weatherInfo.setDate2(jsonObject02.getString("date"));
                weatherInfo.setIcon2(jsonObject02.getString("dayPictureUrl"));
                weatherInfo.setType2(jsonObject02.getString("weather"));
                weatherInfo.setTemperature2(jsonObject02.getString("temperature"));

                //第3天天气
                JSONObject jsonObject03 = (JSONObject) WeatherArray01.get(2);
                weatherInfo.setDate3(jsonObject03.getString("date"));
                weatherInfo.setIcon3(jsonObject03.getString("dayPictureUrl"));
                weatherInfo.setType3(jsonObject03.getString("weather"));
                weatherInfo.setTemperature3(jsonObject03.getString("temperature"));

                //第4天天气
                JSONObject jsonObject04 = (JSONObject) WeatherArray01.get(3);
                weatherInfo.setDate4(jsonObject04.getString("date"));
                weatherInfo.setIcon4(jsonObject04.getString("dayPictureUrl"));
                weatherInfo.setType4(jsonObject04.getString("weather"));
                weatherInfo.setTemperature4(jsonObject04.getString("temperature"));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherInfo;
    }
}
