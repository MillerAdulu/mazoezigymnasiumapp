package ke.co.milleradulu.milleradulu.mazoezigymnasium.memberprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;

public class ProfileActivity extends AppCompatActivity {

    TextView firstName, lastName, email, age, gender, weight, target_weight;
    public static final String MAZOEZI_URL = "https://mazoezigymnasium.herokuapp.com/member/profile/2";
    public static final String TAG = "PROFILE SCREEN";
    RequestQueue profileRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        target_weight = findViewById(R.id.target_weight);

        profileRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest profileJSONObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MAZOEZI_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject profileObject = response.getJSONObject("data");

                            Log.d(TAG, profileObject.toString());

                            firstName.setText(profileObject.getString("first_name"));
                            lastName.setText(profileObject.getString("last_name"));
                            email.setText(profileObject.getString("email"));
                            age.setText(profileObject.getString("age"));
                            gender.setText(profileObject.getString("gender"));
                            weight.setText(profileObject.getString("weight"));
                            target_weight.setText(profileObject.getString("target_weight"));

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );

        profileRequestQueue.add(profileJSONObjectRequest);

    }
}
