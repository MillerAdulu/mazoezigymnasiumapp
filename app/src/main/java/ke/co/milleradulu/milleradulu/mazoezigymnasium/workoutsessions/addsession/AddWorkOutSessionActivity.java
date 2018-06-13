package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations.GymLocation;

public class AddWorkOutSessionActivity extends AppCompatActivity {

    private static final String TAG = AddWorkOutSessionActivity.class.getSimpleName();

    private String locationId;
    private int location = 1, year, month, day, reps, sets;
    EditText addSets, addReps;
    List<String> locs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_out_session);

        addSets = findViewById(R.id.add_sets);
        addReps = findViewById(R.id.add_reps);

        Spinner sessionLocationsSpinner = findViewById(R.id.location_spinner);
        GymLocation gymLocation = new GymLocation();

    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), getString(R.string.pick_date));
    }

    public void processDatePickerResult(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void addSession(View view) {
        sets = Integer.parseInt(addSets.getText().toString());
        reps = Integer.parseInt(addReps.getText().toString());

    }
}
