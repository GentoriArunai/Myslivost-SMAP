package cz.folprechtova.hides.web;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.folprechtova.hides.dto.Hide;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class RestClient {

    public static final String API_BASE_URL_PROD = "https://www.posedy.cz/api/";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static RestClient instance = new RestClient();
    private static Retrofit retrofit;
    private Service service;

    private RestClient() {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);


        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(Service.class);
    }



    public static Service get() {
        return instance.service;
    }

    public static Retrofit getRetrofitInstance() {
        return retrofit;
    }

    public interface Service {

        @GET("allHides")
        Call<Hide> getAllHides(/*@Query("username") String username, @Query("password") String password*/);

        @POST("sendComment")
        Call<Void> sendComment(@Query("user") String username, @Query("body") String body);
        //tadyto by zavolalo https://www.posedy.cz/api/sendComment?user=Lenka&body="text zprávy"
        //popripade ulozilo na Firebase

        @POST("sendComment")
        Call<Void> setHideOccupied(@Query("hideId") int hideId, @Query("userName") String username, @Query("isOccupied") boolean isOccupied);

        @GET
        Call<List<Hide>> getHides(@Url String url);
    }
}
