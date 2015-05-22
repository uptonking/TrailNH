package com.kingoo.nhtrail.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kingoo.nhtrail.R;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Description 自定义类：天气预报的显示
 * @author KINGOO
 * @date 2015/4/23
 */
public class WeatherActivity extends Activity {

    EditText etCityName;

    TextView tvCurrentTemperature,tvCurrentType,tvCurrentWind,tvCurrentDate;

    TextView tvDate2;
    ImageView ivIcon2;
    TextView tvType2;
    TextView tvTemperature2;

    TextView tvDate3;
    ImageView ivIcon3;
    TextView tvType3;
    TextView tvTemperature3;

    TextView tvDate4;
    ImageView ivIcon4;
    TextView tvType4;
    TextView tvTemperature4;

    ProgressDialog progressDialog;

    String strCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        findViewByIdWActivity();


        etCityName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (etCityName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入城市名",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog = ProgressDialog.show(WeatherActivity.this, null, "加载数据中，请稍等");
                    progressDialog.setCancelable(true);
                    //在输入完成后，自动隐藏键盘
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCityName.getWindowToken(), 0);
                    getData();
                }
                return false;
            }

        });

    }

    /**
     * Description 方法：调用异步线程获取天气数据
     */
    private void getData() {
        new AsyncWeatherTask().execute();
    }

    /**
     * Description 方法：获取当前日期、星期
     * @return string 当前日期
     */
    private String getCurrentDate() {
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E");
        return sdf.format(todayDate);
    }

    /**
     * Description 方法：实例化WeatherActivity布局里面的组件
     */
    private void findViewByIdWActivity() {
        etCityName = (EditText) findViewById(R.id.et_city_name);

        tvCurrentTemperature = (TextView) findViewById(R.id.tv_current_temperature);
        tvCurrentType = (TextView) findViewById(R.id.tv_current_type);
        tvCurrentWind = (TextView) findViewById(R.id.tv_current_wind);
        tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);

        tvDate2 = (TextView) findViewById(R.id.tv_date_2);
        ivIcon2 = (ImageView) findViewById(R.id.iv_icon_2);
        tvType2 = (TextView) findViewById(R.id.tv_type_2);
        tvTemperature2 = (TextView) findViewById(R.id.tv_temperature_2);

        tvDate3 = (TextView) findViewById(R.id.tv_date_3);
        ivIcon3 = (ImageView) findViewById(R.id.iv_icon_3);
        tvType3 = (TextView) findViewById(R.id.tv_type_3);
        tvTemperature3 = (TextView) findViewById(R.id.tv_temperature_3);

        tvDate4 = (TextView) findViewById(R.id.tv_date_4);
        ivIcon4 = (ImageView) findViewById(R.id.iv_icon_4);
        tvType4 = (TextView) findViewById(R.id.tv_type_4);
        tvTemperature4 = (TextView) findViewById(R.id.tv_temperature_4);
    }

    /**
     * Description 自定义内部类：异步从网络接口获取天气数据
     */
    class AsyncWeatherTask extends AsyncTask<String, Integer, String>{

        @Override
        protected void onPreExecute() {
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String strPath = "";
            try {
                strCityName = etCityName.getText().toString();
                //citys_strlength=city_str.length();
                strCityName = java.net.URLEncoder.encode(strCityName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strPath = "http://api.map.baidu.com/telematics/v3/weather?location="
                    + strCityName + "&output=json&ak=GuZriL3rkm1MUnyTyfsNGvTC";
            // http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=GuZriL3rkm1MUnyTyfsNGvTC

            String jsonString = HttpUtils.getJsonContent(strPath);//调用自定义工具类HttpUtils来获取返回的json数据

            return jsonString;
        }


        @Override
        protected void onPostExecute(String results) {
           data2view(results);
        }
    }

    private void data2view(String data) {

        WeatherInfo weatherInfo = JsonTools.getWeatherInfo(data);

        tvCurrentTemperature.setText(weatherInfo.getCurrentTemperature());
        tvCurrentType.setText(weatherInfo.getCurrentType());
        tvCurrentWind.setText(weatherInfo.getCurrentWind());
        tvCurrentDate.setText(getCurrentDate());


        tvDate2.setText(weatherInfo.getDate2());
        tvType2.setText(weatherInfo.getType2());
        tvTemperature2.setText(weatherInfo.getTemperature2());

        tvDate3.setText(weatherInfo.getDate3());
        tvType3.setText(weatherInfo.getType3());
        tvTemperature3.setText(weatherInfo.getTemperature3());

        tvDate4.setText(weatherInfo.getDate4());
        tvType4.setText(weatherInfo.getType4());
        tvTemperature4.setText(weatherInfo.getTemperature4());

    }
}
