package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.MainActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.MemberClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
  public static final String TAG = UpdateProfileActivity.class.getSimpleName();
  EditText firstName, lastName, ageText, weightText, targetWeight;
  Button updateProfile;
  ProgressBar profileUpdate;
  String first_name, last_name, email;
  int age;
  float weight, target_weight;
  SessionManager sessionManager;
  HashMap<String, String> memberData;

  MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);
  Member member;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_profile);

    sessionManager = new SessionManager(getApplicationContext());
    memberData = sessionManager.getMemberDetails();

    firstName = findViewById(R.id.firstName);
    lastName = findViewById(R.id.lastName);
    ageText = findViewById(R.id.age);
    weightText = findViewById(R.id.weight);
    targetWeight = findViewById(R.id.targetWeight);
    profileUpdate = findViewById(R.id.edit_profile);
    updateProfile = findViewById(R.id.btn_update_profile);

    Call<Member> memberCall = memberClient.member(
      Integer.parseInt(
        memberData.get(
          SessionManager.KEY_MEMBER_ID
        )
      )
    );
    memberCall.enqueue(new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
        member = response.body();
        Log.d(TAG, member.toString());
        setValues();
        profileUpdate.setVisibility(View.GONE);
        updateProfile.setEnabled(true);

      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }

  private void setValues() {
    firstName.setText(member.getFirst_name());
    lastName.setText(member.getLast_name());
    ageText.setText(
      String.format(
        Locale.ENGLISH,
        "%d",
        member.getAge()
      )
    );
    weightText.setText(
      String.format(
        Locale.ENGLISH,
        "%.2f",
        member.getWeight()
      )
    );
    targetWeight.setText(
      String.format(
        Locale.ENGLISH,
        "%.2f",
        member.getTarget_weight()
      )
    );
  }

  public void updateProfile(View view) {
    hideKeyboard();
    first_name = firstName.getText().toString();
    last_name = lastName.getText().toString();
    age = Integer.parseInt(ageText.getText().toString());
    weight = Float.parseFloat(weightText.getText().toString());
    target_weight = Float.parseFloat(targetWeight.getText().toString());

    Call<Member> memberCall = memberClient.update(
      Integer.parseInt(
        memberData.get(
          SessionManager.KEY_MEMBER_ID
        )
      ),
      first_name,
      last_name,
      age,
      weight,
      target_weight
    );

    memberCall.enqueue(new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
        if(response.isSuccessful()) {
          Toast.makeText(UpdateProfileActivity.this,
            "Successful Update",
            Toast.LENGTH_SHORT)
            .show();
          Intent dashboard = new Intent(UpdateProfileActivity.this, MainActivity.class);
          startActivity(dashboard);
        } else {
          Log.d(TAG, response.toString());
        }
      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }

  void hideKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }
}
