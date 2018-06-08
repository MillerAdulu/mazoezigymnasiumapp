package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GymLocationJSONParse {
    private List<GymLocation> gymLocationsList;
    private JSONArray gymLocationsArray;

    GymLocationJSONParse(JSONObject gymLocationsObject){
        try {
            this.gymLocationsArray = gymLocationsObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void parseJSON(){
        try {

            int arrayLength = gymLocationsArray.length();

            String[] gymLocations = new String[arrayLength];
            String[] gymOpeningTimes = new String[arrayLength];
            String[] gymClosingTimes = new String[arrayLength];

            gymLocationsList = new ArrayList<>();

            for (int i = 0; i < arrayLength; i++){
                GymLocation gymLocationObject = new GymLocation();

                JSONObject jsonObject = gymLocationsArray.getJSONObject(i);

                gymLocations[i] = jsonObject.getString("location");
                gymOpeningTimes[i] = jsonObject.getString("opening_time");
                gymClosingTimes[i] = jsonObject.getString("closing_time");

                gymLocationObject.setGymLocation(gymLocations[i]);
                gymLocationObject.setGymOpeningTime(gymOpeningTimes[i]);
                gymLocationObject.setGymClosingTime(gymClosingTimes[i]);

                gymLocationsList.add(gymLocationObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    List<GymLocation> getGymLocations(){
        return gymLocationsList;
    }
}
