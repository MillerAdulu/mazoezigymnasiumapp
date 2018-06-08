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

    public static final String TAG = "GYM_LOCATIONS_ACTIVITY";
    public static final String MAZOEZI_URL = "https://mazoezigymnasium.herokuapp.com/gymlocation";
    List<GymLocation> gymLocations;
    RequestQueue gymLocationsQueue;
    JsonObjectRequest gymLocationsRequestObject;
    private RecyclerView gymLocationsRecyclerView;
    private RecyclerView.Adapter gymLocationsAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_locations);

        gymLocationsRecyclerView = findViewById(R.id.gym_locations_recycler_view);
        gymLocationsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager gymLocationsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gymLocationsRecyclerView.setLayoutManager(gymLocationsLayoutManager);

        gymLocationsQueue = Volley.newRequestQueue(this);

        gymLocationsRequestObject = new JsonObjectRequest(
                Request.Method.GET,
                MAZOEZI_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GymLocationJSONParse gymLocationJSONParse = new GymLocationJSONParse(response);
                        gymLocationJSONParse.parseJSON();
                        gymLocations = gymLocationJSONParse.getGymLocations();

                        gymLocationsAdapater = new GymLocationAdapter(gymLocations);
                        gymLocationsRecyclerView.setAdapter(gymLocationsAdapater);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );

        gymLocationsQueue.add(gymLocationsRequestObject);
    }
}
