package Model;

public class User {
    private int userID;
    private String username;
    private String password;
    private String playerName;
    private int money;
    private int genderSkin;
    private int petID;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setGenderSkin(int genderSkin) {
        this.genderSkin = genderSkin;
    }

    public int getGenderSkin() {
        return genderSkin;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public int getPetID() {
        return petID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User( String username, String password, String playerName, int money, int genderSkin, int petID) {

        this.username = username;
        this.password = password;
        this.playerName = playerName;
        this.money = money;
        this.genderSkin = genderSkin;
        this.petID = petID;
    }
}
