package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors.GymInstructorsFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations.GymLocationsFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.ProfileFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.UpdateProfileFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.settings.SettingsActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession.AddWorkOutSessionActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history.WorkOutHistoryFragment;

public class HomeActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener,
  GymLocationsFragment.OnFragmentInteractionListener,
  GymInstructorsFragment.OnFragmentInteractionListener,
  ProfileFragment.OnFragmentInteractionListener,
  WorkOutHistoryFragment.OnFragmentInteractionListener,
  UpdateProfileFragment.OnFragmentInteractionListener {


  SessionManager sessionManager;
  TextView names, email;
  HashMap<String, String> memberData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();
    memberData = sessionManager.getMemberDetails();

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    displayProfile();

//    names = findViewById(R.id.member_name);
//    email = findViewById(R.id.member_email);
//
//    names.setText(
//      memberData.get(
//        SessionManager.KEY_NAME
//      )
//    );
//
//    email.setText(
//      memberData.get(
//        SessionManager.KEY_EMAIL
//      )
//    );

  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if(id == R.id.action_settings) {
      startActivity(
        new Intent(
          this,
          SettingsActivity.class
        )
      );
    } else if(id == R.id.action_logout) {
      sessionManager.logOutMember();
    }
    return super.onOptionsItemSelected(item);

  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_profile) {
      displayProfile();

    } else if (id == R.id.nav_locations) {

      displayGymLocations();

    } else if (id == R.id.nav_instructors) {
      displayInstructors();

    } else if (id == R.id.nav_add_session) {
      startActivity(
        new Intent(
          this,
          AddWorkOutSessionActivity.class
        )
      );

    } else if (id == R.id.nav_history) {
      displayWorkOutHistory();
    } else if (id == R.id.nav_update_profile) {
      displayUpdateProfile();
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void displayGymLocations() {
    GymLocationsFragment gymLocationsFragment = GymLocationsFragment.newInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.add(
      R.id.fragment,
      gymLocationsFragment
    )
      .addToBackStack(null)
      .commit();
  }

  private void displayInstructors() {
    GymInstructorsFragment gymInstructorsFragment = GymInstructorsFragment.newInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.add(
      R.id.fragment,
      gymInstructorsFragment
    )
      .addToBackStack(null)
      .commit();
  }

  private void displayProfile() {
    ProfileFragment profileFragment = ProfileFragment.newInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.add(
      R.id.fragment,
      profileFragment
    )
      .addToBackStack(null)
      .commit();
  }

  void displayWorkOutHistory() {
    WorkOutHistoryFragment workOutHistoryFragment = WorkOutHistoryFragment.newInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.add(
      R.id.fragment,
      workOutHistoryFragment
    )
      .addToBackStack(null)
      .commit();
  }

  void displayUpdateProfile() {
    UpdateProfileFragment updateProfileFragment = UpdateProfileFragment.newInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.add(
      R.id.fragment,
      updateProfileFragment
    )
      .addToBackStack(null)
      .commit();

  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }
}
