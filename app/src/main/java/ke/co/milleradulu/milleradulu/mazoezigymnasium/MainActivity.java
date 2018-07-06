package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.HashMap;

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
