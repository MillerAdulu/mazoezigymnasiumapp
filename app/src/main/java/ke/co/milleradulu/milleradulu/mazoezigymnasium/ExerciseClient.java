package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExerciseClient {
    @GET ("/exercise")
    Call<List<Exercise>> exercises();
}
