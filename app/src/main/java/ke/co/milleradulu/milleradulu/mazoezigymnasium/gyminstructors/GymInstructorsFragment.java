package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.GymInstructorClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymInstructor;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations.GymLocationsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymInstructorsFragment extends Fragment {

  private OnFragmentInteractionListener mListener;

  public static final String TAG = GymLocationsFragment.class.getSimpleName();
  List<GymInstructor> gymInstructors;
  private RecyclerView gymInstructorsRecyclerView;
  private RecyclerView.Adapter gymInstructorsAdapter;

  SessionManager sessionManager;
  ProgressBar status;


  public GymInstructorsFragment() {
    // Required empty public constructor
  }

  public static GymInstructorsFragment newInstance() {
    return new GymInstructorsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_gym_instructors, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    sessionManager = new SessionManager(getContext());
    sessionManager.checkLogin();

    status = view.findViewById(R.id.instuctors_load);
    showLoading();

    gymInstructorsRecyclerView = view.findViewById(R.id.gym_instructors_recycler_view);
    gymInstructorsRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager gymInstructorsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    gymInstructorsRecyclerView.setLayoutManager(gymInstructorsLayoutManager);
    gymInstructorsRecyclerView.setItemAnimator(new DefaultItemAnimator());

    fetchInstructors();
  }

  // TODO: Rename method, update argument and hook method into UI event
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

  void showLoading() {
    status.setVisibility(View.VISIBLE);
  }

  void stopLoading() {
    status.setVisibility(View.GONE);
  }

  void fetchInstructors() {
    GymInstructorClient gymInstructorClient = APIServiceProvider.createService(GymInstructorClient.class);
    Call<List<GymInstructor>> gymInstructorCall = gymInstructorClient.gymInstructors();

    APIHelper.enqueWithRetry(gymInstructorCall, 3, new Callback<List<GymInstructor>>() {
      @Override
      public void onResponse(@NonNull Call<List<GymInstructor>> call, @NonNull Response<List<GymInstructor>> response) {
        gymInstructors = response.body();
        gymInstructorsAdapter = new GymInstructorAdapter(gymInstructors, getContext());
        gymInstructorsRecyclerView.setAdapter(gymInstructorsAdapter);
        stopLoading();
      }

      @Override
      public void onFailure(@NonNull Call<List<GymInstructor>> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
      }
    });

  }

  public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
  }
}
