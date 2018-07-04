package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession;

import android.app.DialogFragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.Exercise;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.ExerciseClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymLocation;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.GymLocationClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.WorkOutSessionClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.WorkOut;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkOutSessionActivity extends AppCompatActivity {

  private static final String TAG = AddWorkOutSessionActivity.class.getSimpleName();
  SessionManager sessionManager;

  private int location;
  private int year;
  private int month;
  private int day;
  private int member;
  ProgressBar loading;

  HashMap<String, String> memberData;
  private int exercise_type;
  EditText addSets, addReps, sessionDate;
  Button btnAddSession;
  List<GymLocation> gymLocationsList;
  List<Exercise> exerciseTypesList;
  Spinner sessionLocationsSpinner, exerciseTypeSpinner;

  List<String> locations = new ArrayList<>();
  List<String> exercises = new ArrayList<>();
  ArrayAdapter<String> locationsAdapter, exercisesAdapter;

  ArrayMap<String, Integer> locationMap = new ArrayMap<>();
  ArrayMap<String, Integer> exerciseMap = new ArrayMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_work_out_session);

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();

    btnAddSession = findViewById(R.id.add_session);
    sessionLocationsSpinner = findViewById(R.id.location_spinner);
    exerciseTypeSpinner = findViewById(R.id.exercise_type_spinner);
    addSets = findViewById(R.id.add_sets);
    addReps = findViewById(R.id.add_reps);
    loading = findViewById(R.id.add_session_load);
    sessionDate = findViewById(R.id.editTextDate);
    memberData = sessionManager.getMemberDetails();
    member = Integer.parseInt(
      memberData.get(
        SessionManager.KEY_MEMBER_ID
      )
    );
    loading.setVisibility(View.VISIBLE);
    loadLocationsSpinner();
    loadExercisesSpinner();
  }

  public void showDatePickerDialog(View view) {
    DialogFragment dialogFragment = new DatePickerFragment();
    dialogFragment.show(getFragmentManager(), getString(R.string.pick_date));
  }

  public void processDatePickerResult(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;

    sessionDate.setText(
      new StringBuilder()
        .append(day)
        .append("/")
        .append(month + 1)
        .append("/")
        .append(year)

    );
  }

  public void addSession(View view) {
    hideKeyboard();
    int sets = Integer.parseInt(addSets.getText().toString());
    int reps = Integer.parseInt(addReps.getText().toString());

    WorkOutSessionClient workOutSessionClient = APIServiceProvider.createService(WorkOutSessionClient.class);
    Call<WorkOut> workOutCall = workOutSessionClient.addSession(
      year,
      month,
      day,
      location,
      exercise_type,
      reps,
      sets,
      member
    );
    workOutCall.enqueue(new Callback<WorkOut>() {
      @Override
      public void onResponse(@NonNull Call<WorkOut> call, @NonNull Response<WorkOut> response) {
        if(response.isSuccessful()) {
          Toast.makeText(AddWorkOutSessionActivity.this, "Session Added Successfully", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(@NonNull Call<WorkOut> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }

  public void loadLocationsSpinner() {
    GymLocationClient gymLocationClient = APIServiceProvider.createService(GymLocationClient.class);
    Call<List<GymLocation>> gymLocationCall = gymLocationClient.gymLocations();

    gymLocationCall.enqueue(new Callback<List<GymLocation>>() {
      @Override
      public void onResponse(@NonNull Call<List<GymLocation>> call, @NonNull Response<List<GymLocation>> response) {
        gymLocationsList = response.body();
        addLocationsToArrayList();
        locationsSpinnerListener();
        loading.setVisibility(View.GONE);
        btnAddSession.setEnabled(true);
      }

      @Override
      public void onFailure(@NonNull Call<List<GymLocation>> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }

  public void loadExercisesSpinner() {
    ExerciseClient exerciseClient = APIServiceProvider.createService(ExerciseClient.class);
    Call<List<Exercise>> exercisesCall = exerciseClient.exercises();

    exercisesCall.enqueue(new Callback<List<Exercise>>() {
      @Override
      public void onResponse(@NonNull Call<List<Exercise>> call, @NonNull Response<List<Exercise>> response) {
        exerciseTypesList = response.body();
        addExercisesToArrayList();
        exercisesSpinnerListener();
        loading.setVisibility(View.GONE);
        btnAddSession.setEnabled(true);
      }

      @Override
      public void onFailure(@NonNull Call<List<Exercise>> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }

  public void addLocationsToArrayList() {

    for (GymLocation gymLocation : gymLocationsList ) {
      locations.add(gymLocation.getLocation());
      locationMap.put(gymLocation.getLocation(), gymLocation.getId());
    }
  }

  public void addExercisesToArrayList() {
    for(Exercise exercise : exerciseTypesList) {
      exercises.add(exercise.getName());
      exerciseMap.put(exercise.getName(), exercise.getId());
    }
  }

  public void locationsSpinnerListener() {
    locationsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, locations);
    sessionLocationsSpinner.setAdapter(locationsAdapter);

    sessionLocationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String locationKey = (String) parent.getItemAtPosition(position);
        location = locationMap.get(locationKey);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "Hey! Select a location!");
      }
    });
  }

  public void exercisesSpinnerListener() {
    exercisesAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, exercises);
    exerciseTypeSpinner.setAdapter(exercisesAdapter);

    exerciseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String exerciseKey = (String) parent.getItemAtPosition(position);
        exercise_type = exerciseMap.get(exerciseKey);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "Hey! Select an exercise!");
      }
    });
  }

  void hideKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }
}
