package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIServiceProvider {
  private static final String BASE_URL = "https://mazoezigymnasium.herokuapp.com";

  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  private static Retrofit.Builder builder =
    new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(
        GsonConverterFactory.create()
      );

  private static Retrofit retrofit = builder.client(
    httpClient.build()
  ).build();

  public static <S> S createService(
    Class<S> serviceClass
  ) {
    return retrofit.create(serviceClass);
  }
}
