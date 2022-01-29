package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TouristInfoResActivity extends AppCompatActivity {

    private boolean getWeatherInfo = false;
    private String tem1;
    private String tem2;
    private String wind;
    private String date;
    private String weather;
    private TextView weatherText1;
    private TextView weatherText2;
    private Button mBtnBack;
    private String province="";
    private String city="";
    private String sightPath="http://api.tianapi.com/txapi/scenic/index";
    private String responseData;
    private Response response;
    private String responseData2;
    private Response response2;
    private String[] sightInfo = new String[5];
    private boolean gotSightInfo = false;
    private TextView[] sightTextView = new TextView[5];
    private String weatherpath = "https://v0.yiketianqi.com/api?unescape=1&version=v61&appid=62722718&appsecret=zmkz2fQ2&city=";
    //private ListView sightListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_info_res);
        Intent thisIntent=getIntent();
        province=thisIntent.getStringExtra("province");
        city=thisIntent.getStringExtra("city");
        if(province.equals("上海市")||province.equals("北京市")||
                province.equals("重庆市")||province.equals("天津市")){
            city = province;
        }
        searchSightInfo(sightPath);
        searchWeather(weatherpath);
        try{
            Thread.sleep(800);
        } catch (Exception e){
            e.printStackTrace();
        }
        mBtnBack = (Button) findViewById(R.id.btn_tourist_info_res_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        while(!gotSightInfo){

        }
        sightTextView[0]=(TextView) findViewById(R.id.tv_tourist_info_res_sight_1);
        sightTextView[1]=(TextView) findViewById(R.id.tv_tourist_info_res_sight_2);
        sightTextView[2]=(TextView) findViewById(R.id.tv_tourist_info_res_sight_3);
        sightTextView[3]=(TextView) findViewById(R.id.tv_tourist_info_res_sight_4);
        sightTextView[4]=(TextView) findViewById(R.id.tv_tourist_info_res_sight_5);
        for(int i=0;i<5;i++){
            sightTextView[i].setText(sightInfo[i]);
            sightTextView[i].setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        while(!getWeatherInfo){

        }
        weatherText1 = (TextView) findViewById(R.id.tv_weather_1);
        weatherText2 = (TextView) findViewById(R.id.tv_weather_2);
        weatherText1.setText(city+"   "+date);
        weatherText2.setText(tem2+"°C~"+tem1+"°C"+"   "+wind);
/*        sightListView = (ListView) findViewById(R.id.lv_tourist_info_res_sight);
        sightListView.setMinimumHeight(50);
        ArrayAdapter<String> sightAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,sightInfo);
        sightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sightListView.setAdapter(sightAdapter);
        System.out.println("设置成功");*/
    }
    public void searchSightInfo(final String path){
       new Thread(new Runnable() {
           @Override
           public void run() {
               try{

                   OkHttpClient client = new OkHttpClient();
                   int j=0;
                   while(j!=city.length()&&!(city.charAt(j)=='市'&&j==city.length()-1)){
                       j++;
                   }
                   String tempCity = city.substring(0,j);
                   RequestBody body = new FormBody.Builder()
                           .add("key", "c0ca516a1ee7f0dc972cd114c3d85e2e")
                           .add("city", tempCity)
                           .add("num","5")
                           .build();
                   Request request = new Request.Builder()
                           .url(path)
                           .post(body)
                           .build();
                   // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                   response = client.newCall(request).execute();
                   responseData = response.body().string();
                   System.out.println(responseData);
                   try{
                       JSONObject jsonObj = new JSONObject(responseData);
                       if(jsonObj.getString("msg").equals("数据返回为空")){
                           for (int i = 0;i < 5;i++){
                               sightInfo[i] = "未查找到该城市景点信息！";
                           }
                           gotSightInfo = true;
                           return;
                       }
                       String tempJSONObj = jsonObj.getString("newslist");
                       JSONArray jsonArray = new JSONArray(tempJSONObj);
                       for (int i = 0;i < jsonArray.length();i++){
                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                           sightInfo[i] = jsonObject.getString("name");
                       }
                       gotSightInfo = true;
                   } catch (Exception e2){
                       e2.printStackTrace();
                   }
               } catch (Exception e){
                   e.printStackTrace();
               }
           }
       }).start();
    }
    public void searchWeather(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    int j = 0;
                    while(!(city.charAt(j)=='市'&&j==city.length()-1)&&!(j==city.length())){
                        j++;
                    }
                    String tempCity = city.substring(0, j);
                    Request request = new Request.Builder()
                            .url(path + tempCity)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response2 = client.newCall(request).execute();
                    responseData2 = response2.body().string();
                    System.out.println(responseData2);
                    try{
                        JSONObject jsonObj = new JSONObject(responseData2);
                        tem1 = jsonObj.getString("tem1");
                        tem2 = jsonObj.getString("tem2");
                        weather = jsonObj.getString("wea");
                        date = jsonObj.getString("date");
                        wind = jsonObj.getString("win")+' '+jsonObj.getString("win_speed");
                        getWeatherInfo = true;
                    } catch (Exception e2){
                        e2.printStackTrace();
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}