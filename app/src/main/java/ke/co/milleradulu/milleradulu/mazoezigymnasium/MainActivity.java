package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors.GymInstructorsActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations.GymLocationsActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.LoginActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.ProfileActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.SettingsActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.SignUpActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.UpdateProfileActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession.AddWorkOutSessionActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history.WorkOutHistoryActivity;

public class MainActivity extends AppCompatActivity {

  SessionManager sessionManager;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();
  }

  public void openProfile(View view) {
    Intent profileIntent = new Intent(this, ProfileActivity.class);
    startActivity(profileIntent);
  }

  public void openGymInstructors(View view) {
    Intent gymInstructors = new Intent(this, GymInstructorsActivity.class);
    startActivity(gymInstructors);
  }

  public void viewHistory(View view) {
    Intent sessionHistory = new Intent(this, WorkOutHistoryActivity.class);
    startActivity(sessionHistory);
  }

  public void addSession(View view) {
    Intent addSession = new Intent(this, AddWorkOutSessionActivity.class);
    startActivity(addSession);
  }

  public void viewGymLocations(View view) {
    Intent viewGymLocations = new Intent(this, GymLocationsActivity.class);
    startActivity(viewGymLocations);
  }

  public void editProfile(View view) {
    Intent editProfile = new Intent(this, UpdateProfileActivity.class);
    startActivity(editProfile);
  }

  public void signUp(View view) {
    Intent signUp = new Intent(this, SignUpActivity.class);
    startActivity(signUp);
  }

  public void signIn(View view) {
    Intent signIn = new Intent(this, LoginActivity.class);
    startActivity(signIn);
  }

  public void settings(View view) {
    Intent settings = new Intent(this, SettingsActivity.class);
    startActivity(settings);
  }

  public void logOut(View view) {
    sessionManager.logOutMember();
  }
}
