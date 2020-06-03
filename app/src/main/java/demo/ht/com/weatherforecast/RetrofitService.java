package demo.ht.com.weatherforecast;

import demo.ht.com.weatherforecast.bean.ProvinceBean;
import demo.ht.com.weatherforecast.bean.WeatherBean;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    public String Uri = "http://www.weather.com.cn/";

    //市  API
    @GET("data/city3jdata/china.html")
    Call<ProvinceBean> getProvinceUri();

    //天气 API
    @GET("data/sk/{id}.html")
    Observable<WeatherBean> getWeatherUri(@Path("id") String id);
}



