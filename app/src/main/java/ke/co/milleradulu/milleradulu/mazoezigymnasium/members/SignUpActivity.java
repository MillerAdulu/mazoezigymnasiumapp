package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    EditText firstName, lastName, emailAddress, userPassword, confirmPassword;
    String first_name, last_name, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.signUpFirstName);
        lastName = findViewById(R.id.signUpLastName);
        emailAddress = findViewById(R.id.signUpEmailAddress);
        userPassword = findViewById(R.id.signUpPassword);
        confirmPassword = findViewById(R.id.signUpConfirmPassword);
        error = findViewById(R.id.signUpError);

    }

    public void signIn(View view) {
        loadSignIn();
    }

    public void signUp(View view) {
        password = userPassword.getText().toString();
        if(!password.equals(confirmPassword.getText().toString())) {
            error.setText(R.string.password_mismatch);
            error.setCursorVisible(true);
            return;
        }

        if(password.length() < 8) {
            error.setText(R.string.short_password);
            error.setCursorVisible(true);
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
                loadSignIn();
            }

            @Override
            public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    void loadSignIn() {
        Intent signIn = new Intent(this, LoginActivity.class);
        startActivity(signIn);
    }
}
