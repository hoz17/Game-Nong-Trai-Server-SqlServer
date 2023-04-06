package Model;

import java.sql.Time;
import java.sql.Timestamp;

public class Land {
    private int landID;
    private int userID;
    private int slot[] = new int[32];
    private int state[] = new int[32];
    private Integer cropID[] = new Integer[32];
    private Timestamp plantTime[] = new Timestamp[32];
    private int waterLevel[] = new int[32];
    private int landPrice[] = new int[32];

    public int getLandID() {
        return landID;
    }

    public int getUserID() {
        return userID;
    }

    public int[] getSlot() {
        return slot;
    }

    public int getState(int slot) {
        return state[slot];
    }

    public Integer getCropID(int slot) {
        return cropID[slot];
    }

    public Timestamp getPlantTime(int slot) {
        return plantTime[slot];
    }

    public int getWaterLevel(int slot) {
        return waterLevel[slot];
    }

    public int getLandPrice(int slot) {
        return landPrice[slot];
    }

    public void setLandID(int landID) {
        this.landID = landID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setState(int slot, int state) {
        this.state[slot] = state;
    }

    public void setCropID(int slot, Integer cropID) {
        this.cropID[slot] = cropID;
    }

    public void setPlantTime(int slot, Timestamp plantTime) {
        this.plantTime[slot] = plantTime;
    }

    public void setWaterLevel(int slot, int waterLevel) {
        this.waterLevel[slot] = waterLevel;
    }


    public Land(int userID) {
        this.userID = userID;
    }

    public Land(int userID, int[] slot, int[] state, Integer[] cropID, Timestamp[] plantTime, int[] waterLevel) {
        this.userID = userID;
        this.slot = slot;
        this.state = state;
        this.cropID = cropID;
        this.plantTime = plantTime;
        this.waterLevel = waterLevel;
        landPrice[4] = 500;
        for (int i = 5; i < 32; i++) {
            landPrice[i] = landPrice[i - 1] + ((i - 4) * 500);
        }
    }
}
