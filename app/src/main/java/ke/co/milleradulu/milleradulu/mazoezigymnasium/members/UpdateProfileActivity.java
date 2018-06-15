package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.MainActivity;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.MemberClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    public static final String TAG = UpdateProfileActivity.class.getSimpleName();
    EditText firstName, lastName, ageText, weightText, targetWeight;
    String first_name, last_name, email;
    int age;
    float weight, target_weight;

    MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        ageText = findViewById(R.id.age);
        weightText = findViewById(R.id.weight);
        targetWeight = findViewById(R.id.targetWeight);

        Call<Member> memberCall = memberClient.member(1);
        memberCall.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
                member = response.body();
                Log.d(TAG, member.toString());
                setValues();
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
        first_name = firstName.getText().toString();
        last_name = lastName.getText().toString();
        age = Integer.parseInt(ageText.getText().toString());
        weight = Float.parseFloat(weightText.getText().toString());
        target_weight = Float.parseFloat(targetWeight.getText().toString());

        Call<Member> memberCall = memberClient.update(
                1,
                first_name,
                last_name,
                age,
                weight,
                target_weight,
                "milleradulu@gmail.com",
                0
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
}
