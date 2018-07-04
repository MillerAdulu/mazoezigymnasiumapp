package ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIHelper {

  private static final int DEFAULT_RETRIES = 3;

  public static <T> void enqueWithRetry(Call<T> call, final int retryCount, final Callback<T> callback) {
    call.enqueue(new RetryableCallback<T>(call, retryCount) {

      public void onFinalResponse(Call<T> call, Response<T> response) {
        Log.d("APIHelper", "reached onFinalResponse");
        callback.onResponse(call, response);
      }

      public void onFinalFailure(Call<T> call, Throwable t) {
        Log.d("APIHelper", "reached onFinalFailure");
        callback.onFailure(call, t);
      }
    });
  }

  public static <T> void enqueueWithRetry(Call<T> call, final Callback<T> callback) {
    enqueWithRetry(call, DEFAULT_RETRIES, callback);
  }

  public static boolean isCallSuccess(Response response) {
    int code = response.code();
    return (code >= 200 && code < 400);
  }
}
