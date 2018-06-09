package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

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

public class WorkOutHistoryActivity extends AppCompatActivity {

    public static final String TAG = "WORK_OUT_ACTIVITY";
    public static final String MAZOEZI_URL = "https://mazoezigymnasium.herokuapp.com/workoutsession/1";

    RequestQueue workOutHistoryRequestQueue;
    RecyclerView workOutHistoryRecyclerView;
    JsonObjectRequest workOutHistoryJSONRequest;
    RecyclerView.Adapter workOutHistoryAdapter;
    List<WorkOut> workOuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_history);

        workOutHistoryRecyclerView = findViewById(R.id.session_history_recycler_view);
        workOutHistoryRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager workOutsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        workOutHistoryRecyclerView.setLayoutManager(workOutsLayoutManager);

        workOutHistoryRequestQueue = Volley.newRequestQueue(this);

        workOutHistoryJSONRequest = new JsonObjectRequest(
                Request.Method.GET,
                MAZOEZI_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        WorkOutJSONParse workOutJSONParse = new WorkOutJSONParse(response);
                        workOutJSONParse.parseJSON();
                        workOuts = workOutJSONParse.getWorkOutList();

                        workOutHistoryAdapter = new WorkOutAdapter(workOuts);
                        workOutHistoryRecyclerView.setAdapter(workOutHistoryAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );

        workOutHistoryRequestQueue.add(workOutHistoryJSONRequest);
    }
}
