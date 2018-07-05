package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.WorkOutSessionClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.WorkOut;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession.AddWorkOutSessionActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOutHistoryFragment extends Fragment {
  private OnFragmentInteractionListener mListener;

  public static final String TAG = WorkOutHistoryFragment.class.getSimpleName();
  SessionManager sessionManager;
  private RecyclerView workOutHistoryRecyclerView;
  private RecyclerView.Adapter workOutHistoryAdapter;
  List<WorkOut> workOuts;
  ProgressBar historyFetch;
  HashMap<String, String> member;
  TextView noSessions;
  FloatingActionButton fabAddSession;

  public WorkOutHistoryFragment() {
  }

  public static WorkOutHistoryFragment newInstance() {
    return new WorkOutHistoryFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_work_out_history, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    sessionManager = new SessionManager(getContext());
    sessionManager.checkLogin();

    noSessions = view.findViewById(R.id.no_sessions);

    member = sessionManager.getMemberDetails();
    historyFetch = view.findViewById(R.id.progress);
    fabAddSession = view.findViewById(R.id.fab_add_session);

    historyFetch.setVisibility(View.VISIBLE);
    workOutHistoryRecyclerView = view.findViewById(R.id.session_history_recycler_view);
    workOutHistoryRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager workOutsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    workOutHistoryRecyclerView.setLayoutManager(workOutsLayoutManager);

    fabAddSession.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
    fetchHistory();
  }

  void fetchHistory() {
    WorkOutSessionClient workOutSessionClient = APIServiceProvider.createService(WorkOutSessionClient.class);
    Call<List<WorkOut>> workOutCall = workOutSessionClient.workOutSessions(
      Integer.parseInt(
        member.get(
          SessionManager.KEY_MEMBER_ID
        )
      )
    );

    APIHelper.enqueWithRetry(workOutCall, 3, new Callback<List<WorkOut>>() {
      @Override
      public void onResponse(@NonNull Call<List<WorkOut>> call, @NonNull Response<List<WorkOut>> response) {
        historyFetch.setVisibility(View.GONE);
        workOuts = response.body();

        if(workOuts.size() == 0) {
          noSessions.setVisibility(View.VISIBLE);
        }

        workOutHistoryAdapter = new WorkOutAdapter(workOuts);
        workOutHistoryRecyclerView.setAdapter(workOutHistoryAdapter);
        workOutHistoryRecyclerView.setVisibility(View.VISIBLE);
      }

      @Override
      public void onFailure(@NonNull Call<List<WorkOut>> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
        stopLoading();
        Toast.makeText(
          getContext(),
          "Unable fetch your sessions",
          Toast.LENGTH_SHORT
        ).show();
      }
    });

  }



  void stopLoading() {
    historyFetch.setVisibility(View.GONE);
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
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
