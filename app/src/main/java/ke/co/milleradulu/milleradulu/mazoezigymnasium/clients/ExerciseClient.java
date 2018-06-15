package ke.co.milleradulu.milleradulu.mazoezigymnasium.clients;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.Exercise;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExerciseClient {
    @GET ("/exercise")
    Call<List<Exercise>> exercises();
}
