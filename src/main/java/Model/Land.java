package Model;

import java.sql.Time;

public class Land {
    private int landID;
    private int userID;
    private int state;
    private int cropID;
    private Time plantTime;
    private int waterLevel;

    public int getLandID() {
        return landID;
    }

    public void setLandID(int landID) {
        this.landID = landID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    public void setPlantTime(Time plantTime) {
        this.plantTime = plantTime;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public int getUserID() {
        return userID;
    }

    public int getState() {
        return state;
    }

    public int getCropID() {
        return cropID;
    }

    public Time getPlantTime() {
        return plantTime;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

}
