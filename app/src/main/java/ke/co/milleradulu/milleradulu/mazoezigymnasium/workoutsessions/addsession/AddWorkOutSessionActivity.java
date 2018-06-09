package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;

public class AddWorkOutSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_out_session);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), getString(R.string.pick_date));
    }

    public void processDatePickerResult(int year, int month, int day) {}
}
