package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.MainActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.MemberClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = LoginActivity.class.getSimpleName();
  ProgressBar load;
  EditText emailAddress, loginPassword;
  String email, password;

  SessionManager sessionManager;

  private FirebaseAuth firebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    load = findViewById(R.id.login_load);

    firebaseAuth = FirebaseAuth.getInstance();

    sessionManager = new SessionManager(getApplicationContext());

    emailAddress = findViewById(R.id.signInEmailAddress);
    loginPassword = findViewById(R.id.signInPassword);
  }

  public void login(View view) {
    email = emailAddress.getText().toString();
    password = loginPassword.getText().toString();



        if(email.isEmpty() || password.isEmpty()) {
            return;
        }

        MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);

        Call<Member> memberCall = memberClient.login(
                email,
                password
        );

        showLoading();

    APIHelper.enqueWithRetry(memberCall, 3, new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
        if(response.isSuccessful()) {
          Toast.makeText(LoginActivity.this,
            "Logged in successfully " + response.body().getMemberLastName(),
            Toast.LENGTH_SHORT
          ).show();

          sessionManager.createLoginSession(
            String.format(Locale.ENGLISH, "%d", response.body().getMemberId()),
            response.body().getMemberLastName()

          );

          dashboard();
        }
      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
        stopLoading();

        Toast.makeText(
          LoginActivity.this,
          "Unable to log you in at this moment",
          Toast.LENGTH_SHORT
        ).show();
      }
    });

  }

  void dashboard() {
    stopLoading();
    startActivity(
      new Intent(LoginActivity.this, MainActivity.class)
    );
  }
  public void signUp(View view) {
    Intent signUp = new Intent(this, SignUpActivity.class);
    startActivity(signUp);
  }

  void showLoading() {
    load.setVisibility(View.VISIBLE);
  }

  void stopLoading() {
    load.setVisibility(View.GONE);
  }

  @Override
  protected void onStart() {
    super.onStart();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//    updateUI(currentUser);
  }

  public void createAccount() {
    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
      this,
      new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if(task.isSuccessful()) {
            Log.d(TAG, "Create user with email: success");
            FirebaseUser member = firebaseAuth.getCurrentUser();
//            updateUI(member);
          } else {
            Log.w(TAG, "Create user with email: failure ", task.getException());
            Toast.makeText(
              LoginActivity.this,
              "Authentication failed",
              Toast.LENGTH_SHORT
            ).show();
//            updateUI(null);
          }
        }
      }
    );
  }
}
