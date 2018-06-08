package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructor.GymInstructorsActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.memberprofile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openProfile(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        MainActivity.this.startActivity(profileIntent);
    }

    public void openGymInstructors(View view) {
        Intent gymInstructors = new Intent(this, GymInstructorsActivity.class);
        MainActivity.this.startActivity(gymInstructors);
    }
}
