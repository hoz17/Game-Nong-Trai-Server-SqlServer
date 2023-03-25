package Model;

public class Inventory {
    private int inventoryID;
    private int userID;
    private int cropID[] = new int[20];
    private int cropAmount[] = new int[20];
    private int seedAmount[] = new int[20];

    public Inventory(int userID, int[] cropID, int[] cropAmount, int[] seedAmount) {
        this.userID = userID;
        this.cropID = cropID;
        this.cropAmount = cropAmount;
        this.seedAmount = seedAmount;
    }

    public Inventory(int userID) {
        this.userID = userID;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public int getUserID() {
        return userID;
    }

    public int[] getCropID() {
        return cropID;
    }

    public int getCropAmount(int cropID) {
        return cropAmount[cropID];
    }

    public int getSeedAmount(int cropID) {
        return seedAmount[cropID];
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

//    public void setCropID(int slot,int cropID) {
//        this.cropID[slot] = cropID;
//    }

    public void setCropAmount(int cropID,int cropAmount) {
        this.cropAmount[cropID] = cropAmount;
    }

    public void setSeedAmount(int cropID,int seedAmount) {
        this.seedAmount[cropID] = seedAmount;
    }
}
