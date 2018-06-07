package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GymInstructorJSONParse {
    private List<GymInstructor> gymInstructorsList;
    private JSONArray gymInstructors;

    GymInstructorJSONParse(JSONObject gymInstructorsArray){
        try{
            this.gymInstructors = gymInstructorsArray.getJSONArray("data");
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    protected void parseJSON(){

        try {

            String[] names = new String[gymInstructors.length()];
            String[] emails = new String[gymInstructors.length()];
            String[] genders = new String[gymInstructors.length()];

            gymInstructorsList = new ArrayList<>();

            for(int i = 0; i < gymInstructors.length(); i++){
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

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    List<GymInstructor> getGymInstructors(){
        return gymInstructorsList;
    }
}
