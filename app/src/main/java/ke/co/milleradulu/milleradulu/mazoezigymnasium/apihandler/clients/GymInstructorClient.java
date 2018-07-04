package ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymInstructor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GymInstructorClient {
    @GET("/gyminstructor")
    Call<List<GymInstructor>> gymInstructors();

    @GET("/gyminstructor/{id}")
    Call<GymInstructor> gymInstructor(
            @Path("id") String id
    );
}
