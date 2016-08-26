package th.ac.mahidol.rama.emam.manager;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mahidol.rama.emam.manager.http.ApiService;


public class HttpManager {

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    private Context mContext;
    private ApiService service;

    private HttpManager() {
        mContext = Contextor.getInstance().getContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://webdemo.rama.mahidol.ac.th:8080/EMAMService/")
//                .baseUrl("http://10.0.2.2:8080/EMAMService/service/EMAMService/")
//                .baseUrl("http://10.6.165.86:8080/EMAMService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public ApiService getService(){
        return service;
    }

}
