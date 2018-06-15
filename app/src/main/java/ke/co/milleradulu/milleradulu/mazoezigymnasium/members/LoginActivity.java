package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.MainActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.MemberClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailAddress, loginPassword;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        memberCall.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            "Logged in successfully " + response.body().getLast_name(),
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent dashboard = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(dashboard);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {

            }
        });
    }
}
