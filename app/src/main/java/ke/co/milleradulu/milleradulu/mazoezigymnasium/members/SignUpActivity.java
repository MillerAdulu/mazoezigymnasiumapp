package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.MainActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.Member;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.MemberClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
  public static final String TAG = SignUpActivity.class.getSimpleName();
  TextView error;
  ProgressBar signingUp;

  EditText firstName, lastName, emailAddress, userPassword, confirmPassword;
  String first_name, last_name, email, password;
  SessionManager sessionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    sessionManager = new SessionManager(getApplicationContext());

    firstName = findViewById(R.id.signUpFirstName);
    lastName = findViewById(R.id.signUpLastName);
    emailAddress = findViewById(R.id.signUpEmailAddress);
    userPassword = findViewById(R.id.signUpPassword);
    confirmPassword = findViewById(R.id.signUpConfirmPassword);
    error = findViewById(R.id.signUpError);
    signingUp = findViewById(R.id.sign_up_load);

  }

  public void signUp(View view) {
    signingUp.setVisibility(View.VISIBLE);
    hideKeyboard();

    password = userPassword.getText().toString();
    if(!password.equals(confirmPassword.getText().toString())) {
      error.setText(R.string.password_mismatch);
      error.setVisibility(View.VISIBLE);
      return;
    }

    if(password.length() < 8) {
      error.setText(R.string.short_password);
      error.setVisibility(View.VISIBLE);
      return;
    }
    if(email.equals("")) {
      error.setText(R.string.required_email);
      error.setVisibility(View.VISIBLE);
      return;
    }

    first_name = firstName.getText().toString();
    last_name = lastName.getText().toString();
    email = emailAddress.getText().toString();

    MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);
    Call<Member> memberCall = memberClient.register(
      first_name,
      last_name,
      email,
      password
    );

    memberCall.enqueue(new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
        Toast.makeText(SignUpActivity.this, R.string.signed_up, Toast.LENGTH_SHORT).show();
        signIn(
          String.format(Locale.ENGLISH, "%d", response.body().getId()),
          response.body().getLast_name()
        );
      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });
  }

  void signIn(String memberId, String lastName) {
    sessionManager.createLoginSession(
      memberId,
      lastName
    );
    signingUp.setVisibility(View.GONE);
    dashboard();
  }

  void dashboard() {
    startActivity(
      new Intent(this, MainActivity.class)
    );
  }

  public void signIn(View view) {
    startActivity(
      new Intent(this, LoginActivity.class)
    );
  }

  void hideKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }
}
