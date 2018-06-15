package ke.co.milleradulu.milleradulu.mazoezigymnasium.memberprofile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.ServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.MemberProfileClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.MemberProfile;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {

    public final static String TAG = ProfileActivity.class.getSimpleName();
    TextView firstName, lastName, email, age, gender, weight, target_weight;
    MemberProfile memberProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        target_weight = findViewById(R.id.target_weight);

        MemberProfileClient memberProfileClient = ServiceProvider.createService(MemberProfileClient.class);
        Call<MemberProfile> callMemberProfile = memberProfileClient.member(1);
        callMemberProfile.enqueue(new Callback<MemberProfile>() {
            @Override
            public void onResponse(@NonNull Call<MemberProfile> call, @NonNull retrofit2.Response<MemberProfile> response) {
                memberProfile = response.body();

                firstName.setText(memberProfile.getFirst_name());
                lastName.setText(memberProfile.getLast_name());
                email.setText(memberProfile.getEmail());
                age.setText(String.format(Locale.ENGLISH ,"%d", memberProfile.getAge()));
                gender.setText(String.format(Locale.ENGLISH ,"%d", memberProfile.getGender()));
                weight.setText(String.format(Locale.ENGLISH ,"%f", memberProfile.getWeight()));
                target_weight.setText(String.format(Locale.ENGLISH ,"%f", memberProfile.getTarget_weight()));

            }

            @Override
            public void onFailure(@NonNull Call<MemberProfile> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

    }
}
