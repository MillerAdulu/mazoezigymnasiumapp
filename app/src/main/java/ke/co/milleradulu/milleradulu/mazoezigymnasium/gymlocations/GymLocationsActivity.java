package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.ServiceProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymLocationsActivity extends AppCompatActivity {
    public final static String TAG = GymLocationsActivity.class.getSimpleName();
    private RecyclerView gymLocationsRecyclerView;
    List<GymLocation> gymLocations;
    private RecyclerView.Adapter gymLocationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_locations);
        gymLocationsRecyclerView = findViewById(R.id.gym_locations_recycler_view);
        gymLocationsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager gymLocationsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gymLocationsRecyclerView.setLayoutManager(gymLocationsLayoutManager);

        GymLocationClient gymLocationClient = ServiceProvider.createService(GymLocationClient.class);
        Call<List<GymLocation>> gymLocationCall = gymLocationClient.gymLocations();

        gymLocationCall.enqueue(new Callback<List<GymLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<GymLocation>> call, @NonNull Response<List<GymLocation>> response) {
                gymLocations = response.body();
                gymLocationAdapter = new GymLocationAdapter(gymLocations);
                gymLocationsRecyclerView.setAdapter(gymLocationAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<GymLocation>> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}
