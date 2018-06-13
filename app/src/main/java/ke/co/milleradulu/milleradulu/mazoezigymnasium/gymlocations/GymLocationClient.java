package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GymLocationClient {
    @GET ("/gymlocation")
    Call<List<GymLocation>> gymLocations();

    @GET ("/gymlocation/{id}")
    Call<GymLocation> gymLocation(
            @Path("id") String id
    );
}
