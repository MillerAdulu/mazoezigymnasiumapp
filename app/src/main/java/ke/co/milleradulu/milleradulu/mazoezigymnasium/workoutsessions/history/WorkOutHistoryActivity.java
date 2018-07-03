package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.WorkOut;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.clients.WorkOutSessionClient;
import retrofit2.Call;
import retrofit2.Callback;

public class WorkOutHistoryActivity extends AppCompatActivity {

  public static final String TAG = WorkOutHistoryActivity.class.getSimpleName();
  SessionManager sessionManager;
  private RecyclerView workOutHistoryRecyclerView;
  private RecyclerView.Adapter workOutHistoryAdapter;
  List<WorkOut> workOuts;
  ProgressBar historyFetch;
  HashMap<String, String> member;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_work_out_history);

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();

    member = sessionManager.getMemberDetails();
    historyFetch = findViewById(R.id.progress);
    historyFetch.setVisibility(View.VISIBLE);
    workOutHistoryRecyclerView = findViewById(R.id.session_history_recycler_view);
    workOutHistoryRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager workOutsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    workOutHistoryRecyclerView.setLayoutManager(workOutsLayoutManager);

    WorkOutSessionClient workOutSessionClient = APIServiceProvider.createService(WorkOutSessionClient.class);
    Call<List<WorkOut>> workOutCall = workOutSessionClient.workOutSessions(
      Integer.parseInt(
        member.get(
          SessionManager.KEY_MEMBER_ID
        )
      )
    );

    workOutCall.enqueue(new Callback<List<WorkOut>>() {
      @Override
      public void onResponse(@NonNull Call<List<WorkOut>> call, @NonNull retrofit2.Response<List<WorkOut>> response) {
        historyFetch.setVisibility(View.GONE);
        workOuts = response.body();
        workOutHistoryAdapter = new WorkOutAdapter(workOuts);
        workOutHistoryRecyclerView.setAdapter(workOutHistoryAdapter);
        workOutHistoryRecyclerView.setVisibility(View.VISIBLE);
      }

      @Override
      public void onFailure(@NonNull Call<List<WorkOut>> call, @NonNull Throwable t) {
        Log.d(TAG, t.toString());
      }
    });

  }
}
