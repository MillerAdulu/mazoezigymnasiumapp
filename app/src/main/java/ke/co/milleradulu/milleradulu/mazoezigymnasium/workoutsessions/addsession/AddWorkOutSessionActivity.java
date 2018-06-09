package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations.GymLocation;

public class AddWorkOutSessionActivity extends AppCompatActivity {

    private String locationId;
    private int location = 1, year, month, day, reps, sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_out_session);

        EditText addSets, addReps;

        addSets = findViewById(R.id.add_sets);
        addReps = findViewById(R.id.add_reps);

        sets = Integer.parseInt(addSets.getText().toString());
        reps = Integer.parseInt(addReps.getText().toString());

        Spinner sessionLocationsSpinner = findViewById(R.id.location_spinner);
        GymLocation gymLocation = new GymLocation();
        gymLocation.fetchAllGyms(this);
        ArrayList<String> gymLocations = gymLocation.getGymLocationsArrayList();
        sessionLocationsSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, gymLocations));

        sessionLocationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        WorkOutSessionNetworkRequest workOutSessionNetworkRequest = new WorkOutSessionNetworkRequest();
        workOutSessionNetworkRequest.setLocation(location);
        workOutSessionNetworkRequest.setYear(year);
        workOutSessionNetworkRequest.setMonth(month);
        workOutSessionNetworkRequest.setDay(day);
        workOutSessionNetworkRequest.setReps(reps);
        workOutSessionNetworkRequest.setSets(sets);
        workOutSessionNetworkRequest.addSession(this);
    }
}
