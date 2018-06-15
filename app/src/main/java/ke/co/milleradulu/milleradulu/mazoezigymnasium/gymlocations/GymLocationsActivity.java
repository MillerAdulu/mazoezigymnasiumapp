package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.ServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.GymLocationClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.GymLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymLocationsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public final static String TAG = GymLocationsActivity.class.getSimpleName();
    List<GymLocation> gymLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_locations);

        GymLocationClient gymLocationClient = ServiceProvider.createService(GymLocationClient.class);
        Call<List<GymLocation>> gymLocationCall = gymLocationClient.gymLocations();

        gymLocationCall.enqueue(new Callback<List<GymLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<GymLocation>> call, @NonNull Response<List<GymLocation>> response) {
                gymLocations = response.body();

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locations_map);
                mapFragment.getMapAsync(GymLocationsActivity.this);
            }

            @Override
            public void onFailure(@NonNull Call<List<GymLocation>> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for (GymLocation gymLocation : gymLocations) {

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(
                                    new LatLng(
                                            Double.parseDouble(gymLocation.getLatitude()),
                                            Double.parseDouble(gymLocation.getLongitude())
                                    )
                            )
                            .title(
                                    gymLocation.getLocation()
                            )
                    .snippet(
                            "Opening Time: " + gymLocation.getOpening_time() + "\n" +
                            "Closing Time: " + gymLocation.getClosing_time()
                    )
            );

        }
    }
}
