package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions;

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
            @Field("year") int year,
            @Field("month") int month,
            @Field("day") int day,
            @Field("location") int location,
            @Field("exercise_type") int exercise_type,
            @Field("reps") int reps,
            @Field("sets") int sets,
            @Field("member") int member
    );
}
