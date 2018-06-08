package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructor;

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

public class GymInstructorsActivity extends AppCompatActivity {

    public static final String TAG = "GYM_INSTRUCTORS_ACTIVITY";
    public static final String MAZOEZI_URL = "https://mazoezigymnasium.herokuapp.com/gyminstructor";
    List<GymInstructor> gymInstructors;
    RequestQueue gymInstructorsQueue;
    JsonObjectRequest gymInstructorsRequestObject;
    private RecyclerView gymInstructorsRecyclerView;
    private RecyclerView.Adapter gymInstructorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_instructors);

        gymInstructorsRecyclerView = findViewById(R.id.gym_instructors_recycler_view);
        gymInstructorsRecyclerView.setHasFixedSize(true);


        RecyclerView.LayoutManager gymInstructorsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gymInstructorsRecyclerView.setLayoutManager(gymInstructorsLayoutManager);

        gymInstructorsQueue = Volley.newRequestQueue(this);

        gymInstructorsRequestObject = new JsonObjectRequest(
                Request.Method.GET,
                MAZOEZI_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        GymInstructorJSONParse gymInstructorJSONParse = new GymInstructorJSONParse(response);
                        gymInstructorJSONParse.parseJSON();
                        gymInstructors = gymInstructorJSONParse.getGymInstructors();

                        gymInstructorsAdapter = new GymInstructorAdapter(gymInstructors);
                        gymInstructorsRecyclerView.setAdapter(gymInstructorsAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );

        gymInstructorsQueue.add(gymInstructorsRequestObject);
    }
}
