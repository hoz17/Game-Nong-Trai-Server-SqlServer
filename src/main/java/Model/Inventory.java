package Model;

public class Inventory {
    private int inventoryID;
    private int userID;
    private int cropID;
    private int cropAmount;
    private int seedAmount;

    public int getInventoryID() {
        return inventoryID;
    }

    public int getUserID() {
        return userID;
    }

    public int getCropID() {
        return cropID;
    }

    public int getCropAmount() {
        return cropAmount;
    }

    public int getSeedAmount() {
        return seedAmount;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    public void setCropAmount(int cropAmount) {
        this.cropAmount = cropAmount;
    }

    public void setSeedAmount(int seedAmount) {
        this.seedAmount = seedAmount;
    }
}
