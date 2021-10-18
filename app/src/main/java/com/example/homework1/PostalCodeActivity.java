package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.homework1.Id;

public class PostalCodeActivity extends AppCompatActivity {
    private Button mBtnBack;
    private Handler handler = new Handler();
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private String Text1;
    private String Text2;
    private String Text3;
    private Button mBtnSearch1;
    private String responseData;
    private Response response;
    private String codeRes;//地名查询邮编的结果
    private String httpUrl1="http://v.juhe.cn/postcode/query";
    private String location;//存储省市
    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postal_code);
        textView1 = (TextView) findViewById(R.id.tv_postal_code_res_1);
        textView2 = (TextView) findViewById(R.id.tv_postal_code_res_2);
        mBtnBack = (Button) findViewById(R.id.btn_postal_code_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostalCodeActivity.this,MainActivity.class);
                startActivity(intent);
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
        editText2 = (EditText) findViewById(R.id.et_postal_code_2);
        editText2.addTextChangedListener(new TextWatcher() {
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
                Text2 = s.toString();
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
        editText3 = (EditText) findViewById(R.id.et_postal_code_3);
        editText3.addTextChangedListener(new TextWatcher() {
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
                Text3 = s.toString();
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
                while(location==null){

                }
                textView2.setText("查询结果："+location);
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
                    response = client.newCall(request).execute();
                    responseData = response.body().string();
                    System.out.println(responseData);
                    parseJSONWithJSONObject1(responseData);
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
        Id idlist=new Id();
        String provinceId,cityId;
        boolean find = false;
        try{
            JSONObject idObj = new JSONObject(idlist.idJsonList);
            String idObjStr = idObj.getString("result");
            JSONArray idArray = new JSONArray(idObjStr);
            int i=0;
            while(!find){
                JSONObject provinceObj = idArray.getJSONObject(i);
                if(provinceObj.getString("province")==Text2){
                    find=true;
                    provinceId = provinceObj.getString("id");
                    JSONArray cityArray = new JSONArray(provinceObj.getString("province"));
                    for(int j=0;j<cityArray.length();j++){
                        if(cityArray.getJSONObject(j).getString("city")==Text3){
                            cityId=cityArray.getJSONObject(j).getString("id");
                            break;
                        }
                    }
                }
                if(i==idArray.length()-1) break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(find==false){
            codeRes = "没有查询结果，请检查输入是否有误";
        }
        //若获取了id，则进行下一步
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}