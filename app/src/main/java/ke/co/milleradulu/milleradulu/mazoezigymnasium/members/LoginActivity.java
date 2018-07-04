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

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

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

  ActionCodeSettings actionCodeSettings;
  FirebaseAuth firebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    load = findViewById(R.id.login_load);


    sessionManager = new SessionManager(getApplicationContext());
//    if(sessionManager.getMemberDetails() != null) {
//      dashboard();
//    }

    emailAddress = findViewById(R.id.signInEmailAddress);
    loginPassword = findViewById(R.id.signInPassword);

//    actionCodeSettings = ActionCodeSettings
//      .newBuilder()
//      .setUrl("https://www.milleradulu.co.ke")
//      .setHandleCodeInApp(true)
//      .setIOSBundleId("ke.co.milleradulu.milleradulu.mazoezigymnasium")
//      .setAndroidPackageName(
//        "ke.co.milleradulu.milleradulu.mazoezigymnasium",
//        true,
//        "24")
//      .build();
  }

  public void login(View view) {
    email = emailAddress.getText().toString();
    password = loginPassword.getText().toString();



        if(email.isEmpty() || password.isEmpty()) {
            return;
        }

//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
//          .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//              if(task.isSuccessful()) {
//                Log.d(TAG, "Email Sent");
//              }
//            }
//          });
//
//        Intent intent = getIntent();
//
//        String emailLink = intent.getData().toString();
//
//        if(firebaseAuth.isSignInWithEmailLink(emailLink)){
//          firebaseAuth.signInWithEmailLink(email, emailLink)
//            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//              @Override
//              public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()) {
//                  Log.d(TAG, "Successfully signed in with email");
//                  AuthResult authResult = task.getResult();
//
//                } else {
//                  Log.e(TAG, "Error signing in with email link: " + task.getException().getMessage());
//                }
//              }
//            });
//
//        }
//
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

}
