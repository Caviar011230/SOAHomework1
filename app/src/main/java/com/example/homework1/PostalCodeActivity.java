package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.ArrayRow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostalCodeActivity extends AppCompatActivity {
    private Button mBtnBack;
    private Handler handler = new Handler();
    private EditText editText1;
    private ArrayList<String> codeList = new ArrayList<>();
    private String Text1;
    private Button mBtnSearch1;
    private Button mBtnSearch2;
    private String responseData;
    private Response response;
    private String responseData2;
    private Response response2;
    private String responseData3;
    private Response response3;
    private String codeRes;//地名查询邮编的结果
    private String httpUrl1="http://v.juhe.cn/postcode/query";
    private String httpUrl2="http://v.juhe.cn/postcode/search";
    private String location;//存储省市
    private TextView textView1;
    private TextView textView2;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private ArrayList<String> provinceList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private String provinceSelected;
    private String citySelected;
    public String provincePath="https://restapi.amap.com/v3/config/district?substrict=1&key=efc3347eb9e730e347f3874cc6da92a1&output=xml";
    public String cityPath="https://restapi.amap.com/v3/config/district?key=efc3347eb9e730e347f3874cc6da92a1&output=xml&keywords=";
    private boolean end=false;
    private boolean secondSearchEnd = false;

    private void parseXMLWithPullProvince(String xmlData) {
        try {
            //xmlData = xmlData.substring(39);
            //获取一个XmlPullParserFactory实例对象
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //通过XmlPullParserFactory实例对象获得一个XmlPullParser对象
            XmlPullParser xmlPullParser = factory.newPullParser();
            //将XML数据设置进去
            xmlPullParser.setInput(new StringReader(xmlData));
            //通过getEventType()得到当前的解析事件
            int eventType = xmlPullParser.getEventType();
            provinceList.add("请选择省份");
            //开始解析，如果当前的解析事件不等于XmlPullParser.END_DOCUMENT，则表示解析还没完成
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("name".equals(nodeName)) {
                            String tempProvince=xmlPullParser.nextText();
                            if(!tempProvince.equals("中华人民共和国")){
                                provinceList.add(tempProvince);
                            }
                        }

                        break;
                    }
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity",provinceList.get(0));

                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();

            }
            try{

            } catch (Exception e2){
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getProvince(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(path)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response = client.newCall(request).execute();
                    responseData = response.body().string();
                    System.out.println(responseData);
                    parseXMLWithPullProvince(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseXMLWithPullCity(String xmlData) {
        try {
            //xmlData = xmlData.substring(39);
            //获取一个XmlPullParserFactory实例对象
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //通过XmlPullParserFactory实例对象获得一个XmlPullParser对象
            XmlPullParser xmlPullParser = factory.newPullParser();
            //将XML数据设置进去
            xmlPullParser.setInput(new StringReader(xmlData));
            //通过getEventType()得到当前的解析事件
            int eventType = xmlPullParser.getEventType();

            //开始解析，如果当前的解析事件不等于XmlPullParser.END_DOCUMENT，则表示解析还没完成
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("name".equals(nodeName)) {
                            String tempCity=xmlPullParser.nextText();
                            if(!tempCity.equals(provinceSelected)){
                                cityList.add(tempCity);
                            }
                        }

                        break;
                    }
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity",cityList.get(0));

                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();

            }
            end=true;
            try{

            } catch (Exception e2){
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getCity(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(path+provinceSelected)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response2 = client.newCall(request).execute();
                    responseData2 = response2.body().string();
                    System.out.println(responseData2);
                    parseXMLWithPullCity(responseData2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postal_code);
        if(provinceList.isEmpty())
            getProvince(provincePath);
        try{
            Thread.sleep(1500);
        } catch(Exception e){

        }
        while(provinceList.isEmpty()){

        }

        textView1 = (TextView) findViewById(R.id.tv_postal_code_res_1);
        textView2 = (TextView) findViewById(R.id.tv_postal_code_res_2);
        provinceSpinner = (Spinner) findViewById(R.id.sp_postal_code_1);
        citySpinner = (Spinner) findViewById(R.id.sp_postal_code_2);
        ArrayAdapter<String> pro_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,provinceList);
        pro_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(pro_adapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                end=false;
                provinceSelected = provinceList.get(position);
                if(!provinceSelected.equals("请选择省份")) {
                    cityList.clear();
                    getCity(cityPath);
                    try{
                        Thread.sleep(1200);
                    } catch(Exception e){

                    }
                    while (!end) {

                    }
                    ArrayAdapter ci_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                    ci_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(ci_adapter);
                    ci_adapter.notifyDataSetChanged();
                    //cityList.clear();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySelected = cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBtnBack = (Button) findViewById(R.id.btn_postal_code_back);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnSearch2 = (Button) findViewById(R.id.btn_postal_code_2);
        mBtnSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTwo(httpUrl2);
                textView1.setText("查询类型：地名查询邮编");
                secondSearchEnd=false;
                try{
                    Thread.sleep(800);
                } catch (Exception e){
                    e.printStackTrace();
                }
                while(!secondSearchEnd){

                }
                textView2.setText("查询结果："+codeRes);
            }
        });
        editText1 = (EditText) findViewById(R.id.et_postal_code_1);
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(delayRun!=null){
//
//                    //每次editText有变化的时候，则移除上次发出的延迟线程
//
//                    handler.removeCallbacks(delayRun);
//
//                }
                Text1 = s.toString();
                //handler.postDelayed(delayRun, 800);
            }
//            private Runnable delayRun = new Runnable() {
//
//                @Override
//
//                public void run() {
//
//                    //在这里调用服务器的接口，获取数据
//
////                getSearchResult(editString, "all", 1, "true");
//
//                }
//            };
        });

        mBtnSearch1 = (Button) findViewById(R.id.btn_postal_code_1);
        mBtnSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOne(httpUrl1);
                textView1.setText("查询类型：邮编查询地名");
                try{
                    Thread.sleep(800);
                } catch (Exception e){
                    e.printStackTrace();
                }
                while(location==null){

                }
                textView2.setText("查询结果："+location);
                location=null;
            }
        });

    }
    public void searchOne(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("postcode", Text1)
                            .add("key", "84c4e0bd7605269cd325593e3f5df843")
                            .build();
                    Request request = new Request.Builder()
                            .url(path)
                            .post(body)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response3 = client.newCall(request).execute();
                    responseData3 = response3.body().string();
                    System.out.println(responseData3);
                    parseJSONWithJSONObject1(responseData3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void parseJSONWithJSONObject1(String jsonData) {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            String tempTempJSONObj = jsonObj.getString("result");
            JSONObject jsonObj2 =  new JSONObject(tempTempJSONObj);
            String tempJSONObj = jsonObj2.getString("list");
            if(tempJSONObj.equals("null")){
                location = "查询不到您输入的邮政编码";
                return;
            }
            // 把需要解析的数据传入到 JSONArray 对象中
            JSONArray jsonArray = new JSONArray(tempJSONObj);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            location = jsonObject.getString("Province")+jsonObject.getString("City");
            System.out.println(location);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void searchTwo(final String path){
        //先找到对应id

        new Thread(new Runnable() {
            @Override
            public void run() {
                String provinceId="",cityId="";
                boolean find = false;
                try{
                    //此处改成获取xml数据后用其做个表格
                    OkHttpClient client2 = new OkHttpClient();

                    Request request2 = new Request.Builder()
                            .url("http://v.juhe.cn/postcode/pcd?key=84c4e0bd7605269cd325593e3f5df843")
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response3 = client2.newCall(request2).execute();
                    responseData3 = response3.body().string();
                    JSONObject idObj = new JSONObject(responseData3);
                    String idObjStr = idObj.getString("result");
                    JSONArray idArray = new JSONArray(idObjStr);
                    int i=0;
                    while(!find){
                        JSONObject provinceObj = idArray.getJSONObject(i);
                        if(provinceObj.getString("province").equals(provinceSelected)){
                            find=true;
                            provinceId = provinceObj.getString("id");
                            JSONArray cityArray = new JSONArray(provinceObj.getString("city"));
                            if(provinceSelected.equals("上海市")||provinceSelected.equals("北京市")||
                                    provinceSelected.equals("重庆市")||provinceSelected.equals("天津市")){
                                citySelected = provinceSelected;
                            }
                            for(int j=0;j<cityArray.length();j++){
                                if(cityArray.getJSONObject(j).getString("city").equals(citySelected)){
                                    cityId=cityArray.getJSONObject(j).getString("id");
                                    break;
                                }
                            }
                        }
                        if(i==idArray.length()-1) break;
                        i++;
                    }
                    if(find==false){
                        codeRes = "没有查询结果，请检查输入是否有误";
                    }
                    //若获取了id，则进行下一步
                    try{
                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new FormBody.Builder()
                                .add("key", "84c4e0bd7605269cd325593e3f5df843")
                                .add("pid",provinceId)
                                .add("cid",cityId)
                                .build();
                        Request request = new Request.Builder()
                                .url(path)
                                .post(body)
                                .build();
                        // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                        response3 = client.newCall(request).execute();
                        responseData3 = response3.body().string();
                        System.out.println(responseData3);
                        parseJSONWithJSONObject2(responseData3);
                        secondSearchEnd = true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

    }
    public void parseJSONWithJSONObject2(String jsonData) {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            String tempTempJSONObj = jsonObj.getString("result");
            if(tempTempJSONObj.equals("null")){
                codeList.clear();
                codeList.add("暂不支持查询该城市的邮政编码");
                codeRes = codeList.get(0);
                return;
            }
            JSONObject jsonObj2 =  new JSONObject(tempTempJSONObj);
            String tempJSONObj = jsonObj2.getString("list");
            // 把需要解析的数据传入到 JSONArray 对象中
            JSONArray jsonArray = new JSONArray(tempJSONObj);
            codeList.clear();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                codeList.add(jsonObject.getString("PostNumber"));
            }
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(codeList);
            codeList.clear();
            Iterator i = hashSet.iterator();
            while(i.hasNext()){
                codeList.add(i.next().toString());
            }
            codeRes = "";
            for(int j=0;j<codeList.size();j++){
                codeRes+=codeList.get(j);
                if(j%4==1){
                    codeRes+="\n";
                }else{
                    codeRes+=" ";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}