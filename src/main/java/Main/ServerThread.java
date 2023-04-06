package Main;

import DAO.*;
import Model.Crop;
import Model.Inventory;
import Model.Land;
import Model.User;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;


public class ServerThread implements Runnable {
    private User user;
    private Land land;
    private Inventory inventory;
    private Socket socketOfServer;
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
    private UserDAO userDAO;
    private String clientIP;
    private LandDAO landDAO;
    private InventoryDAO inventoryDAO;
    private Crop crop;
    private CropDAO cropDAO;
    private DAO dao;

    public ServerThread(Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
//        System.out.println("Server thread number " + clientNumber + " Started");
        userDAO = new UserDAO();
        landDAO = new LandDAO();
        inventoryDAO = new InventoryDAO();
        cropDAO = new CropDAO();
        crop = new Crop();
        dao = new DAO();
        dao.connection();

        isClosed = false;
        //Trường hợp test máy ở server sẽ lỗi do hostaddress là localhost
        if (this.socketOfServer.getInetAddress().getHostAddress().equals("127.0.0.1")) {
            clientIP = "127.0.0.1";
        } else {
            clientIP = this.socketOfServer.getInetAddress().getHostAddress();
        }

    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClientIP() {
        return clientIP;
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
//            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
            write("server-send-id=" + this.clientNumber);
            this.crop = cropDAO.getCrop();
            String message;
            while (!isClosed) {
                message = is.readLine();
                System.out.println("Receive: "+message);
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split("=");
                //Xác minh
                if (messageSplit[0].equals("client-verify")) {
//                    System.out.println(message);
                    User user1 = userDAO.verifyUser(new User(messageSplit[1], messageSplit[2]));
                    if (user1 == null)
                        write("wrong-user=" + messageSplit[1] + "=" + messageSplit[2]);
                    else
//                        if (!user1.getIsOnline() && !userDAO.checkIsBanned(user1))
                    {
                        write("login-success=" + getStringFromUser(user1));
                        this.user = user1;
                        this.land = landDAO.getPlayerFarmland(this.user.getUserID());
                        this.inventory = inventoryDAO.getPlayerInventory(this.user.getUserID());

//                        userDAO.updateToOnline(this.user.getID());
//                        Server.serverThreadBus.boardCast(clientNumber, "chat-server," + user1.getNickname() + " đang online");
//                        Server.admin.addMessage("[" + user1.getID() + "] " + user1.getNickname() + " đang online");
                        System.out.println("[" + user1.getUserID() + "] " + user1.getUsername() + " đang online");
                    }
//                    else if (!userDAO.checkIsBanned(user1)) {
//                        write("dupplicate-login," + messageSplit[1] + "," + messageSplit[2]);
//                    }
                }
                //Xử lý đăng kí
                if (messageSplit[0].equals("register")) {
                    boolean checkDup = userDAO.checkDuplicated(messageSplit[1]);
                    if (checkDup) write("duplicate-username=");
                    else {
                        User userRegister = new User(messageSplit[1], messageSplit[2], messageSplit[3], Integer.parseInt(messageSplit[4]), Integer.parseInt(messageSplit[5]), Integer.parseInt(messageSplit[6]));
                        userDAO.addUser(userRegister);
                        this.user = userDAO.verifyUser(userRegister);
                        this.land = landDAO.getPlayerFarmland(this.user.getUserID());
                        this.inventory = inventoryDAO.getPlayerInventory(this.user.getUserID());
//                        this.crop = cropDAO.getCrop();
                        //userDAO.updateToOnline(this.user.getID());
                        //Server.serverThreadBus.boardCast(clientNumber, "chat-server,"+this.user.getUsername()+" đang online");
                        System.out.println("[" + this.user.getUserID() + "] " + this.user.getUsername() + " đang online");
                        write("login-success=" + getStringFromUser(this.user));
                    }
                }
                //Xử lý gửi dữ liệu người chơi
                if (messageSplit[0].equals("load-player-data")) {
                    String playerData = "";
                    for (int i = 0; i < 20; i++) {
                        playerData += i + "=" + this.inventory.getCropAmount(i) + "=" + this.inventory.getSeedAmount(i) + "=";
                    }
                    for (int i = 0; i < 32; i++) {
                        playerData += i + "=" + this.land.getState(i) + "=" + this.land.getCropID(i) + "=" + this.land.getPlantTime(i) + "=" + this.land.getWaterLevel(i) + "=";
                    }
                    write("player-data=" + playerData);
                }
                if (messageSplit[0].equals("load-crop-data")) {
                    String cropData = "crop-data=";
                    for (int i = 0; i < 21; i++) {
                        cropData += crop.getCropID(i) + "=" + crop.getCropName(i) + "=" + crop.getCropGrowTime(i) + "=" + crop.getCropBuyPrice(i) + "=" + crop.getCropSellPrice(i) + "=" + crop.getWaterLevel(i) + "=";
                    }
                    write(cropData);
                }
                //Xử lý mua ô đất
                if (messageSplit[0].equals("buy-farmland")) {
                    int slot = Integer.parseInt(messageSplit[1]);
                    int newMoney = this.user.getMoney() - this.land.getLandPrice(slot);
                    int landExecute = landDAO.buyFarmland(this.user.getUserID(), slot);
                    int moneyExecute = userDAO.updateMoney(this.user.getUserID(), newMoney);
                    if (landExecute == 1 && moneyExecute == 1) {
                        write("buy-farmland-complete=" + slot + "=" + newMoney);
                        this.land.setState(slot, 1);
                        this.user.setMoney(newMoney);
                    } else {
                        System.out.println("Lỗi phần mua đất");
                    }
                }
                //Xử lý mua hạt giống
                if (messageSplit[0].equals("buy-seed")) {
                    int cropID = Integer.parseInt(messageSplit[1]);
                    int newSeedAmount = Integer.parseInt(messageSplit[2]) + this.inventory.getSeedAmount(cropID);
                    int newMoney = this.user.getMoney() - (Integer.parseInt(messageSplit[2]) * this.crop.getCropBuyPrice(cropID));
                    int inventoryExecute = inventoryDAO.updateSeed(this.user.getUserID(), cropID, newSeedAmount);
                    int moneyExecute = userDAO.updateMoney(this.user.getUserID(), newMoney);
                    if (inventoryExecute == 1 && moneyExecute == 1) {
                        this.user.setMoney(newMoney);
                        this.inventory.setSeedAmount(cropID, newSeedAmount);
                        write("buy-seed-complete=" + cropID + "=" + newSeedAmount + "=" + newMoney);
                    } else {
                        System.out.println("Lỗi database phần mua hạt giống");
                    }
                }
                //Xử lý bán rau củ
                if (messageSplit[0].equals("sell-crop")) {
                    int cropID = Integer.parseInt(messageSplit[1]);
                    int newCropAmount = this.inventory.getCropAmount(cropID) - Integer.parseInt(messageSplit[2]);
                    int newMoney = this.user.getMoney() + (Integer.parseInt(messageSplit[2]) * this.crop.getCropSellPrice(cropID));
                    int inventoryExecute = inventoryDAO.updateCrop(this.user.getUserID(), cropID, newCropAmount);
                    int moneyExecute = userDAO.updateMoney(this.user.getUserID(), newMoney);
                    if (inventoryExecute == 1 && moneyExecute == 1) {
                        this.user.setMoney(newMoney);
                        this.inventory.setCropAmount(cropID, newCropAmount);
                        write("sell-seed-complete=" + cropID + "=" + newCropAmount + "=" + newMoney);
                    } else {
                        System.out.println("Lỗi database phần bán rau củ");
                    }
                }
                //Xử lý trồng cây
                if (messageSplit[0].equals("plant")) {
                    int slot = Integer.parseInt(messageSplit[1]);
                    int cropID = Integer.parseInt(messageSplit[2]);
                    Timestamp newPlantTime = new Timestamp(System.currentTimeMillis());
                    int newSeedAmount = this.inventory.getSeedAmount(cropID) - 1;
                    int landExecute = landDAO.plantCrop(this.user.getUserID(), newPlantTime, cropID, slot);
                    int inventoryExecute = inventoryDAO.updateSeed(this.user.getUserID(), cropID, newSeedAmount);
                    if (landExecute == 1 && inventoryExecute == 1) {
                        this.land.setCropID(slot, cropID);
                        this.land.setPlantTime(slot, newPlantTime);
                        this.inventory.setSeedAmount(cropID, newSeedAmount);
                        write("plant-complete=" + slot + "=" + cropID + "=" + newPlantTime + "=" + newSeedAmount);
                    } else {
                        System.out.println("Lỗi database phần trồng cây");
                    }
                }
                //Xử lý tưới nước
                if (messageSplit[0].equals("water")) {
                    int slot = Integer.parseInt(messageSplit[1]);
                    int newWaterLevel = this.land.getWaterLevel(slot) + 1;
                    int landExecute = landDAO.waterPlant(this.user.getUserID(), newWaterLevel, slot);
                    if (landExecute == 1) {
                        this.land.setWaterLevel(slot, newWaterLevel);
                        write("water-complete=" + slot + "=" + newWaterLevel);
                    } else {
                        System.out.println("Lỗi phần tưới nước");
                    }
                }
                //Xử lý thu hoạch
                if (messageSplit[0].equals("harvest")) {
                    int cropID = Integer.parseInt(messageSplit[1]);
                    int slot = Integer.parseInt(messageSplit[2]);
                    int newCropAmount = this.inventory.getCropAmount(cropID) + 1;
                    int inventoryExecute = inventoryDAO.updateCrop(this.user.getUserID(), cropID, newCropAmount);
                    int landExecute = landDAO.harvest(this.user.getUserID(), slot);
                    if (inventoryExecute == 1 && landExecute == 1) {
                        this.inventory.setCropAmount(cropID, newCropAmount);
                        this.land.setCropID(slot, -1);
                        write("harvest-complete=" + slot + "=" + cropID + "=" + newCropAmount);
                    } else {
                        System.out.println("Lỗi database phần thu hoạch");
                    }
                }
                //Xử lý phá cây
                if (messageSplit[0].equals("trample")) {
                    int slot = Integer.parseInt(messageSplit[1]);
                    int landExecute = landDAO.trample(this.user.getUserID(), slot);
                    if (landExecute == 1) {
                        this.land.setCropID(slot, -1);
                        write("trample-complete=" + slot);
                    } else {
                        System.out.println("Lỗi phần phá cây");
                    }
                }
                //Xử lý thăm nhà
                if (messageSplit[0].equals("visit")) {
                    int guestID = Integer.parseInt(messageSplit[1]);
                    Land guestIsland = landDAO.getGuestIsland(guestID);
                    String playerData = "guest-data=";
                    for (int i = 0; i < 32; i++) {
                        playerData += i + "=" + guestIsland.getState(i) + "=" + guestIsland.getCropID(i) + "=" + guestIsland.getPlantTime(i) + "=" + guestIsland.getWaterLevel(i) + "=";
                    }
                    write(playerData);
                }
                //Xử lý xem bảng xếp hạng
                if (messageSplit[0].equals("view-leaderboard")) {
                    write("leaderboard=" + userDAO.getLeaderBoard());
                }
                //Xử lý chat thế giới
                if (messageSplit[0].equals("world-chat")) {
                    Server.serverThreadBus.boardCast(this.clientNumber, "chat-message=" + messageSplit[1]);
                }
                //Xử lý xem danh sách thăm nhà
                if (messageSplit[0].equals("view-visit-list")) {
                    write("visit-list=" + userDAO.getVisitList(messageSplit[1]));
                }
                //Xử lý mở túi đồ
//                if (messageSplit[0].equals("open-inventory")) {
//
//                }
            }
        } catch (IOException e) {
            //Thay đổi giá trị cờ để thoát luồng
            isClosed = true;
            //Cập nhật trạng thái của user
            if (this.user != null) {
//                userDAO.updateToOffline(this.user.getUser_ID());
//                userDAO.updateToNotPlaying(this.user.getUser_ID());
//                Server.serverThreadBus.boardCast(clientNumber, "chat-server," + this.user.getUsername() + " đã offline");
                System.out.println("[" + user.getUserID() + "] " + user.getUsername() + " đã offline");
                //Server.admin.addMessage("[" + user.getUser_ID() + "] " + user.getNickname() + " đã offline");
            }

            //remove thread khỏi bus
            Server.serverThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber + " đã thoát");

        } catch (SQLException e) {
//            throw new RuntimeException(e);
        }
    }


    public String getStringFromUser(User user1) {
        return "" + user1.getPlayerName() + "=" + user1.getMoney() + "=" + user1.getGenderSkin() + "=" + user1.getPetID();
    }

    public void write(String message) throws IOException {
        os.write(message);
        System.out.println("Send: "+message);
        os.newLine();
        os.flush();
    }
}
