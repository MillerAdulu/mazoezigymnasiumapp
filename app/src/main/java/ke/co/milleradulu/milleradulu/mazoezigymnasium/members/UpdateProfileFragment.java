package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.MemberClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileFragment extends Fragment {

  private OnFragmentInteractionListener mListener;

  public static final String TAG = UpdateProfileFragment.class.getSimpleName();
  EditText firstName, lastName, ageText, weightText, targetWeight;
  Button updateProfile;
  ProgressBar profileUpdate;
  String first_name, last_name;
  int age;
  float weight, target_weight;
  SessionManager sessionManager;
  HashMap<String, String> memberData;

  MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);
  Member member;

  public UpdateProfileFragment() {
    // Required empty public constructor
  }

  public static UpdateProfileFragment newInstance() {
    return new UpdateProfileFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_update_profile, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    sessionManager = new SessionManager(getContext());
    memberData = sessionManager.getMemberDetails();

    firstName = view.findViewById(R.id.firstName);
    lastName = view.findViewById(R.id.lastName);
    ageText = view.findViewById(R.id.age);
    weightText = view.findViewById(R.id.weight);
    targetWeight = view.findViewById(R.id.targetWeight);
    profileUpdate = view.findViewById(R.id.edit_profile);
    updateProfile = view.findViewById(R.id.btn_update_profile);

    updateProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateProfile();
      }
    });

    networkCall();


  }

  void networkCall() {
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
        stopLoading();
        updateProfile.setEnabled(true);

      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
        Toast.makeText(
          getContext(),
          "Unable to load your data at this moment",
          Toast.LENGTH_SHORT
        ).show();
        stopLoading();
      }
    });
  }

  private void setValues() {
    firstName.setText(member.getMemberFirstName());
    lastName.setText(member.getMemberLastName());
    ageText.setText(
      String.format(
        Locale.ENGLISH,
        "%d",
        member.getMemberAge()
      )
    );
    weightText.setText(
      String.format(
        Locale.ENGLISH,
        "%.2f",
        member.getMemberWeight()
      )
    );
    targetWeight.setText(
      String.format(
        Locale.ENGLISH,
        "%.2f",
        member.getMemberTargetWeight()
      )
    );
  }

  public void updateProfile() {
    first_name = firstName.getText().toString();
    last_name = lastName.getText().toString();
    age = Integer.parseInt(ageText.getText().toString());
    weight = Float.parseFloat(weightText.getText().toString());
    target_weight = Float.parseFloat(targetWeight.getText().toString());

    showLoading();

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

    APIHelper.enqueWithRetry(memberCall, 3, new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
        if(response.isSuccessful()) {
          stopLoading();
          Toast.makeText(getContext(),
            "Successful Update",
            Toast.LENGTH_SHORT
          ).show();
        } else {
          Log.d(TAG, response.toString());
        }
      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
        stopLoading();
        Toast.makeText(
          getContext(),
          "Unable to update your profile at this moment",
          Toast.LENGTH_SHORT
        ).show();
      }
    });
  }


  void stopLoading() {
    profileUpdate.setVisibility(View.GONE);
  }

  void showLoading() {
    profileUpdate.setVisibility(View.VISIBLE);
  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
        + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
  }
}
