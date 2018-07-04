package ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models;

public class GymLocation {
    private int gymId;
    private String gymLocation, openingTime, closingTime, gymLatitude, gymLongitude;

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getGymLocation() {
        return gymLocation;
    }

    public void setGymLocation(String gymLocation) {
        this.gymLocation = gymLocation;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getGymLatitude() {
        return gymLatitude;
    }

    public void setGymLatitude(String gymLatitude) {
        this.gymLatitude = gymLatitude;
    }

    public String getGymLongitude() {
        return gymLongitude;
    }

    public void setGymLongitude(String gymLongitude) {
        this.gymLongitude = gymLongitude;
    }
}
