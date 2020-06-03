package demo.ht.com.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import demo.ht.com.weatherforecast.bean.ProvinceBean;
import demo.ht.com.weatherforecast.bean.WeatherBean;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {


    private ArrayList<String> mList_Province; //list 省/直辖市
    private Spinner mSpinner_province;
    private Spinner mSpinner_city;
    public String province_code = "10101";//默认北京
    private ArrayList<String> mList_city;   //list 市

    public static  String city_code = "01";//天气URI 拼接101010100
    private TextView mTv_ap;
    private TextView mTv_city;
    private TextView mTv_sd;
    private TextView mTv_temp;
    private TextView mTv_wd;
    private ArrayList<String> mList_zhiXiaShi; //直辖市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化控件
        initView();
        //网络请求  省的Spinner
        initProvinceRetrofit();

        //省
        mSpinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                //设置字体颜色
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (mList_Province.size() == 0) {
                    return;
                }
                position++;
                if (position < 10) {
                    province_code= 1010 + "" + position;
                } else {
                    province_code = 101+""+position + "";
                }
                //因为他他启动的时候会走,点击的时候也会走所以直接在这里请求 市就好了
                initCityRetrofit();
                Toast.makeText(MainActivity.this, mList_Province.get(--position)+"\n" + province_code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //市
        mSpinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mList_city.size() == 0 && mList_city == null) {
                    return;
                }

                position++;
                if (position < 10) {
                    city_code = 0 + "" + position;
                }else{
                    city_code = position+"";
                }
                initWeatherUri();


                Toast.makeText(MainActivity.this, "" + mList_city.get(--position)+"\n"+city_code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //请求市的数据
    private void initCityRetrofit() {
        if (mList_city == null) {
            mList_city = new ArrayList<>();
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.weather.com.cn/data/city3jdata/provshi/" + province_code + ".html")
                .build();

        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String json = response.body().string(); //获取Json字符串
                Log.i("onResponse", json + "");

                try {   //使用最原生JSONObject获取数据
                    JSONObject jsonObject = new JSONObject(json);
                    Iterator<String> keys = jsonObject.keys();//获取json字符串的key
                    mList_city.clear();//保证数据不重复需要先将list集合清空
                    while (keys.hasNext()) {//循环每一个值
                        String next = keys.next();//获取key
                        String value = (String) jsonObject.get(next);//获取value
                        Log.i("szjnext", next + "\t\t" + value);
                        mList_city.add(value);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //在主线程中设置Spinner数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, mList_city);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        mSpinner_city.setAdapter(adapter);

                        mSpinner_city.setDropDownVerticalOffset(100);
                    }
                });


            }
        });


    }

    private void initWeatherUri() {
        if (mList_zhiXiaShi == null) {
            mList_zhiXiaShi = new ArrayList<>();
            //直辖市 北京10101  上海10102  天津10103  重庆 10104
            mList_zhiXiaShi.add("10101");
            mList_zhiXiaShi.add("10102");
            mList_zhiXiaShi.add("10103");
            mList_zhiXiaShi.add("10104");
        }

        String zhixiashiCode = "01";
        RetrofitService retrofitService = new Retrofit.Builder()
                .baseUrl(RetrofitService.Uri)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
        //        请求天气接口
//        http://www.weather.com.cn/data/sk/省 + 市 + 01.html 非直辖市
//        http://www.weather.com.cn/data/sk/直辖市 + 0100 .html 直辖市
        //判断是否是直辖市
        if (mList_zhiXiaShi.contains(province_code)) {
            zhixiashiCode = "00";
        }
        Log.i("szjjjj直辖市",province_code + city_code + zhixiashiCode);

        Observable<WeatherBean> observable = retrofitService.getWeatherUri(province_code + city_code +zhixiashiCode);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        mTv_ap.setText("气压:"+weatherBean.getWeatherinfo().getAP()+"");
                        mTv_city.setText("城市:"+weatherBean.getWeatherinfo().getCity()+"");
                        mTv_sd.setText("湿度:"+weatherBean.getWeatherinfo().getSD()+"");
                        mTv_temp.setText("温度:"+weatherBean.getWeatherinfo().getTemp()+"");
                        mTv_wd.setText("风力:"+weatherBean.getWeatherinfo().getWD()+"");
                            Log.i("szjonNext",weatherBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("szjonError",e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    private void initView() {
        mSpinner_province = findViewById(R.id.spinner_province);
        mSpinner_city = findViewById(R.id.spinner_city);
        mTv_ap = findViewById(R.id.tv_AP);
        mTv_city = findViewById(R.id.tv_city);
        mTv_sd = findViewById(R.id.tv_SD);
        mTv_temp = findViewById(R.id.tv_temp);
        mTv_wd = findViewById(R.id.tv_WD);
    }

    private void initProvinceRetrofit() {
        if (mList_Province == null) {
            mList_Province = new ArrayList<>();
        }
        //这里用Retroift我很后悔,我写完之后才发现用原生的Json会非常简单 !! 建议大家这里用原生的Json来解析
        //这里强烈建议大家用JSONObject来解析  我写的 市 是用原生的JSONObject来解析的 大家可以参考一下!
        new Retrofit.Builder()
                .baseUrl(RetrofitService.Uri)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService.class)
                .getProvinceUri()
                .enqueue(new Callback<ProvinceBean>() {
                    @Override
                    public void onResponse(Call<ProvinceBean> call, Response<ProvinceBean> response) {
                        Log.i("http返回：", response.body().toString() + "");
                        ProvinceBean body = response.body();
                        addProvinceBean(body);//这一步很傻! 我已经写完了这只是Demo就懒得改了

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, mList_Province);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        mSpinner_province.setAdapter(adapter);

                        mSpinner_province.setDropDownVerticalOffset(100);

                    }
                    @Override
                    public void onFailure(Call<ProvinceBean> call, Throwable t) {
                        Log.i("err错误：", t.getMessage());
                    }
                });



    }


    private void addProvinceBean(ProvinceBean body) {
        mList_Province.clear();
        mList_Province.add(body.get_$10101());
        mList_Province.add(body.get_$10102());
        mList_Province.add(body.get_$10103());
        mList_Province.add(body.get_$10104());
        mList_Province.add(body.get_$10105());
        mList_Province.add(body.get_$10106());
        mList_Province.add(body.get_$10107());
        mList_Province.add(body.get_$10108());
        mList_Province.add(body.get_$10109());
        mList_Province.add(body.get_$10110());
        mList_Province.add(body.get_$10111());
        mList_Province.add(body.get_$10112());
        mList_Province.add(body.get_$10113());
        mList_Province.add(body.get_$10114());
        mList_Province.add(body.get_$10115());
        mList_Province.add(body.get_$10116());
        mList_Province.add(body.get_$10117());
        mList_Province.add(body.get_$10118());
        mList_Province.add(body.get_$10119());
        mList_Province.add(body.get_$10120());
        mList_Province.add(body.get_$10121());
        mList_Province.add(body.get_$10122());
        mList_Province.add(body.get_$10123());
        mList_Province.add(body.get_$10124());
        mList_Province.add(body.get_$10125());
        mList_Province.add(body.get_$10126());
        mList_Province.add(body.get_$10127());
        mList_Province.add(body.get_$10128());
        mList_Province.add(body.get_$10129());
        mList_Province.add(body.get_$10130());
        mList_Province.add(body.get_$10131());
        mList_Province.add(body.get_$10132());
        mList_Province.add(body.get_$10133());
        mList_Province.add(body.get_$10134());
    }


}
