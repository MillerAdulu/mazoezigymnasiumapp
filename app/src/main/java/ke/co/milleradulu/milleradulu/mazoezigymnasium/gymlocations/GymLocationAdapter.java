package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;

public class GymLocationAdapter extends RecyclerView.Adapter<GymLocationAdapter.GymLocationViewHolder> {

    private List<GymLocation> gymLocationList;

    GymLocationAdapter(List<GymLocation> gymLocationList) {
        this.gymLocationList = gymLocationList;
    }

    class GymLocationViewHolder extends RecyclerView.ViewHolder {
        TextView gymLocation, gymOpeningTime, gymClosingTime;

        GymLocationViewHolder(View itemView) {
            super(itemView);
            gymLocation = itemView.findViewById(R.id.gym_location);
            gymOpeningTime = itemView.findViewById(R.id.gym_opening_time);
            gymClosingTime = itemView.findViewById(R.id.gym_closing_time);
        }
    }

    @NonNull
    @Override
    public GymLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View gymLocationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_location_card, parent, false);
        return new GymLocationViewHolder(gymLocationView);
    }

    @Override
    public void onBindViewHolder(@NonNull GymLocationViewHolder holder, int position){
        holder.gymLocation.setText(gymLocationList.get(position).getGymLocation());
        holder.gymOpeningTime.setText(gymLocationList.get(position).getGymOpeningTime());
        holder.gymClosingTime.setText(gymLocationList.get(position).getGymClosingTime());
    }

    @Override
    public int getItemCount(){
        return gymLocationList.size();
    }

    public void add(int position, GymLocation gymLocation){
        gymLocationList.add(position, gymLocation);
        notifyItemInserted(position);

    }
}
