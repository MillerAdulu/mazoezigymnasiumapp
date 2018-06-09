package ke.co.milleradulu.milleradulu.mazoezigymnasium.workoutsessions.history;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkOutJSONParse {
    private List<WorkOut> workOutList;
    private JSONArray workOutArray;

    WorkOutJSONParse(JSONObject workOutJSONObject) {
        try {
            this.workOutArray = workOutJSONObject.getJSONArray("data");
        } catch (JSONException e ) {
            e.printStackTrace();
        }
    }

    protected void parseJSON(){
        int arraySize = workOutArray.length();

        String dates[] = new String[arraySize];
        String locations[] = new String[arraySize];
        String exercises[] = new String[arraySize];
        String reps[] = new String[arraySize];
        String sets[] = new String[arraySize];

        workOutList = new ArrayList<>();

        for(int i = 0; i < arraySize; i++) {
            WorkOut workOutObject = new WorkOut();

            try {

                JSONObject jsonObject = workOutArray.getJSONObject(i);

                dates[i] = jsonObject.getString("date");
                locations[i] = jsonObject.getString("location");
                exercises[i] = jsonObject.getString("exercise_type");
                reps[i] = jsonObject.getString("reps");
                sets[i] = jsonObject.getString("sets");

                workOutObject.setDate(dates[i]);
                workOutObject.setLocation(locations[i]);
                workOutObject.setExercise(exercises[i]);
                workOutObject.setReps(reps[i]);
                workOutObject.setSets(sets[i]);

                workOutList.add(workOutObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    protected List<WorkOut> getWorkOutList() {
        return workOutList;
    }
}
