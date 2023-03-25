package Model;

public class Crop {
    private int cropID[] = new int[20];
    private String cropName[] = new String[20];
    private int cropGrowTime[] = new int[20];
    private int cropBuyPrice[] = new int[20];
    private int cropSellPrice[] = new int[20];
    private int waterLevel[] = new int[20];

    public int getCropID(int i) {
        return cropID[i];
    }

    public String getCropName(int i) {
        return cropName[i];
    }

    public int getCropGrowTime(int i) {
        return cropGrowTime[i];
    }

    public int getCropBuyPrice(int i) {
        return cropBuyPrice[i];
    }

    public int getCropSellPrice(int i) {
        return cropSellPrice[i];
    }

    public int getWaterLevel(int i) {
        return waterLevel[i];
    }

    public Crop() {
    }

    public Crop(int[] cropID, String[] cropName, int[] cropGrowTime, int[] cropBuyPrice, int[] cropSellPrice, int[] waterLevel) {
        this.cropID = cropID;
        this.cropName = cropName;
        this.cropGrowTime = cropGrowTime;
        this.cropBuyPrice = cropBuyPrice;
        this.cropSellPrice = cropSellPrice;
        this.waterLevel = waterLevel;
    }
}
