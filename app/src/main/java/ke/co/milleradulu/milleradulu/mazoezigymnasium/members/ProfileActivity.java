package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.MemberClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.Member;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {

  public final static String TAG = ProfileActivity.class.getSimpleName();
  TextView firstName, lastName, email, age, gender, weight, target_weight;
  ImageView profilePicture;
  Member member;
  ProgressBar profileProgress;
  SessionManager sessionManager;
  HashMap<String, String> memberData;
  int memberId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    sessionManager = new SessionManager(getApplicationContext());
    memberData = sessionManager.getMemberDetails();

    memberId = Integer.parseInt(
      memberData.get(SessionManager.KEY_MEMBER_ID)
    );

    profilePicture = findViewById(R.id.profile_picture);
    firstName = findViewById(R.id.first_name);
    lastName = findViewById(R.id.last_name);
    email = findViewById(R.id.email);
    age = findViewById(R.id.age);
    gender = findViewById(R.id.gender);
    weight = findViewById(R.id.weight);
    target_weight = findViewById(R.id.target_weight);
    profileProgress = findViewById(R.id.profile_load);


    MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);
    Call<Member> callMemberProfile = memberClient.member(memberId);
    callMemberProfile.enqueue(new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull retrofit2.Response<Member> response) {
        member = response.body();

        firstName.setText(member.getFirst_name());
        lastName.setText(member.getLast_name());
        email.setText(member.getEmail());

        if(member.getGender() == 0) {
          gender.setText(R.string.male_gender);
        } else {
          gender.setText(R.string.female_gender);
        }

        age.setText(String.format(Locale.ENGLISH ,"%d", member.getAge()));
        weight.setText(String.format(Locale.ENGLISH ,"%.2f", member.getWeight()));
        target_weight.setText(String.format(Locale.ENGLISH ,"%.2f", member.getTarget_weight()));

        Glide.with(ProfileActivity.this)
          .load("https://cdn.pixabay.com/photo/2014/09/25/23/36/man-461195_960_720.jpg")
          .into(profilePicture);

        profileProgress.setVisibility(View.GONE);
      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });

  }
}
