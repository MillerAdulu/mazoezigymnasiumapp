package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors;

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

public class GymInstructorsActivity extends AppCompatActivity {

    public static final String TAG = GymInstructorsActivity.class.getSimpleName();
    List<GymInstructor> gymInstructors;
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

        GymInstructorClient gymInstructorClient = ServiceProvider.createService(GymInstructorClient.class);
        Call<List<GymInstructor>> gymInstructorCall = gymInstructorClient.gymInstructors();

        gymInstructorCall.enqueue(new Callback<List<GymInstructor>>() {
            @Override
            public void onResponse(@NonNull Call<List<GymInstructor>> call, @NonNull Response<List<GymInstructor>> response) {
                gymInstructors = response.body();
                gymInstructorsAdapter = new GymInstructorAdapter(gymInstructors);
                gymInstructorsRecyclerView.setAdapter(gymInstructorsAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<GymInstructor>> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}
