package ke.co.milleradulu.milleradulu.mazoezigymnasium.members;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class ProfileFragment extends Fragment {
  private OnFragmentInteractionListener mListener;

  public final static String TAG = ProfileFragment.class.getSimpleName();
  TextView firstName, lastName, email, age, gender, weight, target_weight;
  ImageView profilePicture;
  Member member;
  ProgressBar profileProgress;
  SessionManager sessionManager;
  HashMap<String, String> memberData;
  int memberId;

  public ProfileFragment() {
    // Required empty public constructor
  }

  public static ProfileFragment newInstance() {
    return new ProfileFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_profile, container, false);
  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    sessionManager = new SessionManager(getContext());
    memberData = sessionManager.getMemberDetails();

    memberId = Integer.parseInt(
      memberData.get(SessionManager.KEY_MEMBER_ID)
    );

    profilePicture = view.findViewById(R.id.profile_picture);
    firstName = view.findViewById(R.id.first_name);
    lastName = view.findViewById(R.id.last_name);
    email = view.findViewById(R.id.email);
    age = view.findViewById(R.id.age);
    gender = view.findViewById(R.id.gender);
    weight = view.findViewById(R.id.weight);
    target_weight = view.findViewById(R.id.target_weight);
    profileProgress = view.findViewById(R.id.profile_load);


    fetchProfile();
  }

  void showLoading() {
    profileProgress.setVisibility(View.VISIBLE);
  }

  void stopLoading() {
    profileProgress.setVisibility(View.GONE);
  }

  void fetchProfile() {
    showLoading();
    MemberClient memberClient = APIServiceProvider.createService(MemberClient.class);
    Call<Member> callMemberProfile = memberClient.member(memberId);

    APIHelper.enqueWithRetry(callMemberProfile, 3, new Callback<Member>() {
      @Override
      public void onResponse(@NonNull Call<Member> call, @NonNull Response<Member> response) {
        member = response.body();

        firstName.setText(member.getMemberFirstName());
        lastName.setText(member.getMemberLastName());
        email.setText(member.getMemberEmail());

        if(member.getMemberGender() == 0) {
          gender.setText(R.string.male_gender);
        } else {
          gender.setText(R.string.female_gender);
        }

        age.setText(String.format(Locale.ENGLISH ,"%d", member.getMemberAge()));
        weight.setText(String.format(Locale.ENGLISH ,"%.2f", member.getMemberWeight()));
        target_weight.setText(String.format(Locale.ENGLISH ,"%.2f", member.getMemberTargetWeight()));

        Glide.with(getActivity())
          .load("https://cdn.pixabay.com/photo/2014/09/25/23/36/man-461195_960_720.jpg")
          .into(profilePicture);

        stopLoading();
      }

      @Override
      public void onFailure(@NonNull Call<Member> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
        stopLoading();
        Toast.makeText(
          getContext(),
          "Unable to load your profile at this moment",
          Toast.LENGTH_SHORT
        ).show();
      }
    });
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
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
