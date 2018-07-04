package ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymLocation;
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
