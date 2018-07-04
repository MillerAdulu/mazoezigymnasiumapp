package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.WorkOut;

public class WorkOutAdapter extends RecyclerView.Adapter<WorkOutAdapter.WorkOutViewHolder> {

    private List<WorkOut> workOutList;

    WorkOutAdapter(List<WorkOut> workOutList) {
        this.workOutList = workOutList;
    }

    class WorkOutViewHolder extends RecyclerView.ViewHolder {

        TextView date, location, exercise, reps, sets;

        WorkOutViewHolder (View itemView){
            super(itemView);
            date = itemView.findViewById(R.id.session_date);
            location = itemView.findViewById(R.id.session_location);
            exercise = itemView.findViewById(R.id.session_exercise);
            reps = itemView.findViewById(R.id.session_reps);
            sets = itemView.findViewById(R.id.session_sets);
        }
    }

    @NonNull
    @Override
    public WorkOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View workOutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card, parent, false);
        return new WorkOutViewHolder(workOutView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOutViewHolder holder, int position) {
        holder.date.setText(workOutList.get(position).getDate());
        holder.location.setText(workOutList.get(position).getLocation());
        holder.exercise.setText(workOutList.get(position).getExercise_type());
        holder.reps.setText(workOutList.get(position).getReps());
        holder.sets.setText(workOutList.get(position).getSets());
    }

    public void add(int position, WorkOut workOut) {
        workOutList.add(position, workOut);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return workOutList.size();
    }
}
