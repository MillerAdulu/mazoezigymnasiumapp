package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;

public class GymLocationsActivity extends AppCompatActivity {

    GymLocation gymLocation;
    List<GymLocation> gymLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_locations);

        RecyclerView gymLocationsRecyclerView = findViewById(R.id.gym_locations_recycler_view);
        gymLocationsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager gymLocationsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gymLocationsRecyclerView.setLayoutManager(gymLocationsLayoutManager);

        gymLocation = new GymLocation();
        gymLocation.fetchAllGyms(this);
        gymLocationList = gymLocation.getGymLocationsList();

        RecyclerView.Adapter gymLocationsAdapter = new GymLocationAdapter(gymLocationList);
        gymLocationsRecyclerView.setAdapter(gymLocationsAdapter);

    }
}
