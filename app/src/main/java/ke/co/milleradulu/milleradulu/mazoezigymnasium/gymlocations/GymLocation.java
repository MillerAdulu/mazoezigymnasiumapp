package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GymLocation {
    private String gymLocation, gymOpeningTime, gymClosingTime;
    private static final String TAG = "GYM_LOCATIONS_ACTIVITY";
    private static final String MAZOEZI_URL = "https://mazoezigymnasium.herokuapp.com/gymlocation";
    private List<GymLocation> gymLocationsList;
    private ArrayList<String> gymLocationsArrayList;

    public String getGymLocation() {
        return gymLocation;
    }

    public void setGymLocation(String gymLocation) {
        this.gymLocation = gymLocation;
    }

    public String getGymOpeningTime() {
        return gymOpeningTime;
    }

    public void setGymOpeningTime(String gymOpeningTime) {
        this.gymOpeningTime = gymOpeningTime;
    }

    public String getGymClosingTime() {
        return gymClosingTime;
    }

    public void setGymClosingTime(String gymClosingTime) {
        this.gymClosingTime = gymClosingTime;
    }

    public void fetchAllGyms(Context context) {
        RequestQueue gymLocationsQueue = Volley.newRequestQueue(context);

        JsonObjectRequest gymLocationsRequestObject = new JsonObjectRequest(
                Request.Method.GET,
                MAZOEZI_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GymLocationJSONParse gymLocationJSONParse = new GymLocationJSONParse(response);
                        gymLocationJSONParse.parseJSON();
                        gymLocationsList = gymLocationJSONParse.getGymLocations();
                        gymLocationsArrayList = gymLocationJSONParse.gymLocationsForSpinner();
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

    public List<GymLocation> getGymLocationsList() {
        return gymLocationsList;
    }

    public ArrayList<String> getGymLocationsArrayList() {
        return gymLocationsArrayList;
    }
}
