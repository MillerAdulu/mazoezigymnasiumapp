package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors.GymInstructorsFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations.GymLocationsFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.LoginActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.ProfileFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession.AddWorkOutFragment;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history.WorkOutHistoryFragment;

public class MainActivity extends AppCompatActivity {

  NavigationView navigationView;
  DrawerLayout drawer;
  View navHeader;
  ImageView imgNavHeaderBg, imgProfile;
  TextView txtName, txtEmail;
  Toolbar toolbar;

  HashMap<String, String> memberData;

  public static int navItemIndex = 0;

  private static final String TAG_HOME = "home";
  private static String CURRENT_TAG = TAG_HOME;

  private String[] activityTitles;

  private boolean shouldLoadHomeFragOnBackPress = true;
  private Handler handler;

  SessionManager sessionManager;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    toolbar = findViewById(R.id.toolbar);
    setActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.app_menu);
    handler = new Handler();

    drawer = findViewById(R.id.drawer_layout);
    navigationView = findViewById(R.id.nav_view);

    navHeader = navigationView.getHeaderView(0);
    txtName = navHeader.findViewById(R.id.names);
    imgProfile = navHeader.findViewById(R.id.profile_picture);

    activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

    if(savedInstanceState == null) {
      navItemIndex = 0;
      CURRENT_TAG = TAG_HOME;
    }

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();
    memberData = sessionManager.getMemberDetails();
  }




  private void setToolbarTitle() {
    getSupportActionBar().setTitle(activityTitles[navItemIndex]);
  }

  private void selectNavMenu() {
    navigationView.getMenu().getItem(navItemIndex).setChecked(true);
  }

}
