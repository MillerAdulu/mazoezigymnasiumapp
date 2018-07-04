package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymInstructor;

public class GymInstructorAdapter extends RecyclerView.Adapter<GymInstructorAdapter.GymInstructorViewHolder> {

    private List<GymInstructor> gymInstructorList;
    private Context instructorContext;

    GymInstructorAdapter(List<GymInstructor> gymInstructorList, Context context) {
        this.gymInstructorList = gymInstructorList;
        this.instructorContext = context;
    }

    public class GymInstructorViewHolder extends RecyclerView.ViewHolder {
        public TextView names, email, gender;
        public ImageView image;

        GymInstructorViewHolder(View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.gym_instructor_names);
            email = itemView.findViewById(R.id.gym_instructor_email);
            gender = itemView.findViewById(R.id.gym_instructor_gender);
            image = itemView.findViewById(R.id.gym_instructor_image);
        }
    }

    public void add(int position, GymInstructor gymInstructor) {
        gymInstructorList.add(position, gymInstructor);
        notifyItemInserted(position);
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
        holder.gender.setText(
                gymInstructorList.get(position).getGender().equals("0") ? "Male" : "Female"
        );

        Glide.with(instructorContext)
                .load("https://cdn.pixabay.com/photo/2014/09/25/23/36/man-461195_960_720.jpg")
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return gymInstructorList.size();
    }
}
