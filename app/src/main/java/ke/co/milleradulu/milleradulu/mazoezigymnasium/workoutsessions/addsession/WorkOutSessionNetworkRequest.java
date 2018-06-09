package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class WorkOutSessionNetworkRequest {
    private static final String TAG = "ADD_SESSION_ACTIVITY";
    private static final String MAZOEZI_URL = "https://mazoezigymnasium.herokuapp.com/workoutsession";
    private int location, year, month, day, reps, sets;

    public void setLocation(int location) {
        this.location = location;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void addSession(Context context) {
        RequestQueue addSessionQueue = Volley.newRequestQueue(context);

        StringRequest addSessionRequest = new StringRequest(
                Request.Method.POST,
                MAZOEZI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("location", stringify(location));
                params.put("year", stringify(year));
                params.put("month", stringify(month));
                params.put("day", stringify(day));
                params.put("reps", stringify(reps));
                params.put("sets", stringify(sets));

                return params;
            }

                public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        addSessionQueue.add(addSessionRequest);
    }


    private String stringify(int param) {
        return Integer.toString(param);
    }
}
