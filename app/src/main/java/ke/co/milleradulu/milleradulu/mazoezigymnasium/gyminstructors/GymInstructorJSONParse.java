package ke.co.milleradulu.milleradulu.mazoezigymnasium.gyminstructors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GymInstructorJSONParse {
    private List<GymInstructor> gymInstructorsList;
    private JSONArray gymInstructors;

    GymInstructorJSONParse(JSONObject gymInstructorsObject) {
        try {
            this.gymInstructors = gymInstructorsObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void parseJSON() {

        try {

            int arrayLength = gymInstructors.length();

            String[] names = new String[arrayLength];
            String[] emails = new String[arrayLength];
            String[] genders = new String[arrayLength];

            gymInstructorsList = new ArrayList<>();

            for(int i = 0; i < arrayLength; i++) {
                GymInstructor gymInstructorObject = new GymInstructor();

                JSONObject jsonObject = gymInstructors.getJSONObject(i);

                names[i] = jsonObject.getString("names");
                emails[i] = jsonObject.getString("email");
                genders[i] = jsonObject.getString("gender");

                gymInstructorObject.setNames(names[i]);
                gymInstructorObject.setEmail(emails[i]);
                gymInstructorObject.setGender(genders[i]);

                gymInstructorsList.add(gymInstructorObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    List<GymInstructor> getGymInstructors(){
        return gymInstructorsList;
    }
}
