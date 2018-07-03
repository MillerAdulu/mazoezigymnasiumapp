package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.GymInstructorClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.GymInstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymInstructorsActivity extends AppCompatActivity {

  public static final String TAG = GymInstructorsActivity.class.getSimpleName();
  List<GymInstructor> gymInstructors;
  private RecyclerView gymInstructorsRecyclerView;
  private RecyclerView.Adapter gymInstructorsAdapter;

  SessionManager sessionManager;
  ProgressBar status;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gym_instructors);

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();

    status = findViewById(R.id.instuctors_load);
    status.setVisibility(View.VISIBLE);

    gymInstructorsRecyclerView = findViewById(R.id.gym_instructors_recycler_view);
    gymInstructorsRecyclerView.setHasFixedSize(true);


    RecyclerView.LayoutManager gymInstructorsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    gymInstructorsRecyclerView.setLayoutManager(gymInstructorsLayoutManager);
    gymInstructorsRecyclerView.setItemAnimator(new DefaultItemAnimator());


    GymInstructorClient gymInstructorClient = APIServiceProvider.createService(GymInstructorClient.class);
    Call<List<GymInstructor>> gymInstructorCall = gymInstructorClient.gymInstructors();

    gymInstructorCall.enqueue(new Callback<List<GymInstructor>>() {
      @Override
      public void onResponse(@NonNull Call<List<GymInstructor>> call, @NonNull Response<List<GymInstructor>> response) {
        gymInstructors = response.body();
        gymInstructorsAdapter = new GymInstructorAdapter(gymInstructors, GymInstructorsActivity.this);
        gymInstructorsRecyclerView.setAdapter(gymInstructorsAdapter);
        status.setVisibility(View.GONE);
      }

      @Override
      public void onFailure(@NonNull Call<List<GymInstructor>> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }
}
