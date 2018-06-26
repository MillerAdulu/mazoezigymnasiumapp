package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;

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
    Button loginButton;
    TextView token;
    Auth0 auth0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = findViewById(R.id.signInEmailAddress);
        loginPassword = findViewById(R.id.signInPassword);

        token = findViewById(R.id.token);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAuth0();
            }
        });

        auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);

    }

    private void loginAuth0() {
        token.setText(R.string.not_logged_in);

        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
                .start(this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                token.setText("Logged in: " + credentials.getAccessToken());
                            }
                        });
                    }
                });
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
