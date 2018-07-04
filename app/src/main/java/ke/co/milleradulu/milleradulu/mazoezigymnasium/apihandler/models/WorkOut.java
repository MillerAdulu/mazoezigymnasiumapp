package ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models;

public class WorkOut {
    private String sessionDate, sessionLocation, exerciseType, exerciseReps, exerciseSets;

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionLocation() {
        return sessionLocation;
    }

    public void setSessionLocation(String sessionLocation) {
        this.sessionLocation = sessionLocation;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getExerciseReps() {
        return exerciseReps;
    }

    public void setExerciseReps(String exerciseReps) {
        this.exerciseReps = exerciseReps;
    }

    public String getExerciseSets() {
        return exerciseSets;
    }

    public void setExerciseSets(String exerciseSets) {
        this.exerciseSets = exerciseSets;
    }
}
