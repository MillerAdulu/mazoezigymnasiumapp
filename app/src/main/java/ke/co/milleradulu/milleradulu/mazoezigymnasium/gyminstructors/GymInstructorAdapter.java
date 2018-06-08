package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;

public class GymInstructorAdapter extends RecyclerView.Adapter<GymInstructorAdapter.GymInstructorViewHolder> {

    private List<GymInstructor> gymInstructorList;

    GymInstructorAdapter(List<GymInstructor> gymInstructorList) {
        this.gymInstructorList = gymInstructorList;
    }

    public class GymInstructorViewHolder extends RecyclerView.ViewHolder {
        public TextView names, email, gender;

        GymInstructorViewHolder(View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.gym_instructor_names);
            email = itemView.findViewById(R.id.gym_instructor_email);
            gender = itemView.findViewById(R.id.gym_instructor_gender);
        }
    }

    public void add(int position, GymInstructor gymInstructor) {
        gymInstructorList.add(position, gymInstructor);
        notifyItemInserted(position);
    }

    public void remove(GymInstructor gymInstructor) {
        int position = gymInstructorList.indexOf(gymInstructor);
        gymInstructorList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public GymInstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gymInstructorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_instructor_card, parent, false);
        return new GymInstructorViewHolder(gymInstructorView);
    }

    @Override
    public void onBindViewHolder(@NonNull GymInstructorViewHolder holder, int position) {
        holder.names.setText(gymInstructorList.get(position).getNames());
        holder.email.setText(gymInstructorList.get(position).getEmail());
        holder.gender.setText(gymInstructorList.get(position).getGender());
    }

    @Override
    public int getItemCount() {
        return gymInstructorList.size();
    }
}
