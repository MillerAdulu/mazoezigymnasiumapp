package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.WorkOut;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.WorkOutSessionClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.addsession.AddWorkOutSessionActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOutHistoryActivity extends AppCompatActivity {

  public static final String TAG = WorkOutHistoryActivity.class.getSimpleName();
  SessionManager sessionManager;
  private RecyclerView workOutHistoryRecyclerView;
  private RecyclerView.Adapter workOutHistoryAdapter;
  List<WorkOut> workOuts;
  ProgressBar historyFetch;
  HashMap<String, String> member;
  TextView noSessions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_work_out_history);

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();

    noSessions = findViewById(R.id.no_sessions);

    member = sessionManager.getMemberDetails();
    historyFetch = findViewById(R.id.progress);

    historyFetch.setVisibility(View.VISIBLE);
    workOutHistoryRecyclerView = findViewById(R.id.session_history_recycler_view);
    workOutHistoryRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager workOutsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    workOutHistoryRecyclerView.setLayoutManager(workOutsLayoutManager);

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
          WorkOutHistoryActivity.this,
          "Unable fetch your sessions",
          Toast.LENGTH_SHORT
        ).show();
      }
    });

  }
  public void addSession(View view) {
    startActivity(
      new Intent(this, AddWorkOutSessionActivity.class)
    );
  }

  void stopLoading() {
    historyFetch.setVisibility(View.GONE);
  }
}
