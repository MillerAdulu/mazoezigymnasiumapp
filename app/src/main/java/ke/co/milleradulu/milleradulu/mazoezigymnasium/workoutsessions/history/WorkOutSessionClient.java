package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WorkOutSessionClient {
    @GET ("/workoutsession/{id}")
    Call<List<WorkOut>> workOutSessions(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST ("/workoutsession/add")
    Call<WorkOut> addSession(
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("location") String location,
            @Field("exercise_type") String exercise_type,
            @Field("reps") int reps,
            @Field("sets") int sets,
            @Field("member") int member
    );
}
